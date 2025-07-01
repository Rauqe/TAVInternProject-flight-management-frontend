<template>
  <div class="dashboard-elplus">
    <div class="welcome-section">
      <h1>Welcome to the Flight Management Dashboard</h1>
      <p class="welcome-desc">Monitor your airline's operations, performance and system status in real time.</p>
    </div>
    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">Active Flights</div>
          <div class="stat-value">{{ totalFlights }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">On-Time Rate</div>
          <div class="stat-value success">{{ onTimeRate }}%</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">Delay Rate</div>
          <div class="stat-value warning">{{ delayRate }}%</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">Cancel Rate</div>
          <div class="stat-value danger">{{ cancelRate }}%</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="dashboard-row" style="margin-top: 30px;">
      <el-col :span="16">
        <el-card>
          <div class="chart-title">Flight Status Overview</div>
          <div style="height: 260px; display: flex; align-items: center; justify-content: center;">
            <canvas id="flight-status-chart" style="max-width: 400px; max-height: 220px;"></canvas>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="chart-title">System Status</div>
          <el-timeline>
            <el-timeline-item :color="systemStatus.database ? 'green' : 'red'">Database Connection</el-timeline-item>
            <el-timeline-item :color="systemStatus.api ? 'green' : 'red'">API Services</el-timeline-item>
            <el-timeline-item :color="systemStatus.kafka ? 'green' : 'red'">Kafka</el-timeline-item>
            <el-timeline-item color="green">Flight Tracking</el-timeline-item>
            <el-timeline-item color="green">Reporting System</el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="dashboard-row" style="margin-top: 30px;">
      <el-col :span="24">
        <el-card>
          <div class="chart-title">Recent Activity</div>
          <el-table :data="recentActivities" style="width: 100%">
            <el-table-column prop="time" label="Time" width="120" />
            <el-table-column prop="text" label="Activity" />
            <el-table-column prop="type" label="Type" width="120" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getFlights } from '../services/flightService';
import { connectFlightWebSocket, disconnectFlightWebSocket } from '../services/websocketService';
import axios from 'axios';
import Chart from 'chart.js/auto';

export default {
  name: 'Dashboard',
  data() {
    return {
      flights: [],
      systemStatus: { api: false, database: false, kafka: false },
      staticActivities: [
        { id: 1, text: 'Flight TK1234 scheduled for departure', type: 'Flight Schedule', time: '10:30 AM' },
        { id: 2, text: 'Route IST-ESB added to network', type: 'Route Management', time: '09:15 AM' },
        { id: 3, text: 'Monthly performance report generated', type: 'Reporting', time: '08:45 AM' },
        { id: 4, text: 'System maintenance completed', type: 'System', time: 'Yesterday' }
      ],
      recentActivities: [],
      chart: null
    };
  },
  computed: {
    totalFlights() {
      return this.flights.length;
    },
    delayRate() {
      return this.flights.length
        ? Math.round(100 * this.flights.filter(f => f.delay && f.delay > 0).length / this.flights.length)
        : 0;
    },
    cancelRate() {
      return this.flights.length
        ? Math.round(100 * this.flights.filter(f => f.status === 'Cancelled').length / this.flights.length)
        : 0;
    },
    onTimeRate() {
      return 100 - this.delayRate - this.cancelRate;
    },
    chartData() {
      return {
        labels: ['On Time', 'Delayed', 'Cancelled'],
        datasets: [
          {
            data: [
              this.onTimeRate,
              this.delayRate,
              this.cancelRate
            ],
            backgroundColor: ['#67c23a', '#e6a23c', '#f56c6c']
          }
        ]
      };
    }
  },
  methods: {
    async fetchSystemStatus() {
      try {
        const res = await axios.get('http://localhost:8081/api/health');
        this.systemStatus = res.data;
      } catch (e) {
        this.systemStatus = { api: false, database: false, kafka: false };
      }
    },
    updateRecentActivities() {
      let dynamic = [];
      if (this.flights.length > 0) {
        const sorted = this.flights.slice().sort((a, b) => {
          const dateA = a.flightDate && a.std ? new Date(a.flightDate + 'T' + a.std) : new Date(0);
          const dateB = b.flightDate && b.std ? new Date(b.flightDate + 'T' + b.std) : new Date(0);
          if (dateB - dateA !== 0) return dateB - dateA;
          if (a.id && b.id) return b.id - a.id;
          return 0;
        });
        dynamic = sorted.slice(0, 5).map(f => ({
          id: f.id,
          text: `Flight ${f.flightNumber} ${f.origin?.code || f.originCode} → ${f.destination?.code || f.destinationCode}`,
          type: f.status === 'Cancelled' ? 'Cancelled' : (f.delay > 0 ? 'Delayed' : 'Scheduled'),
          time: `${f.flightDate} ${f.std}`
        }));
      }
      this.recentActivities = [...dynamic, ...this.staticActivities].slice(0, 8);
    },
    renderChart() {
      if (this.chart) {
        this.chart.destroy();
      }
      const ctx = document.getElementById('flight-status-chart');
      if (ctx) {
        this.chart = new Chart(ctx, {
          type: 'doughnut',
          data: this.chartData,
          options: {
            responsive: true,
            plugins: {
              legend: {
                display: true,
                position: 'bottom'
              }
            }
          }
        });
      }
    }
  },
  watch: {
    chartData: {
      handler() {
        this.$nextTick(() => {
          this.renderChart();
        });
      },
      deep: true
    }
  },
  async mounted() {
    this.flights = await getFlights();
    this.updateRecentActivities();
    this.fetchSystemStatus();
    this.$nextTick(() => {
      this.renderChart();
    });
    connectFlightWebSocket((flightList) => {
      this.flights = flightList;
      this.updateRecentActivities();
      this.$nextTick(() => {
        this.renderChart();
      });
    });
  },
  beforeUnmount() {
    disconnectFlightWebSocket();
    if (this.chart) this.chart.destroy();
  }
};
</script>

<style scoped>
.dashboard-elplus {
  padding: 30px;
}
.welcome-section {
  margin-bottom: 32px;
  text-align: left;
}
.welcome-section h1 {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}
.welcome-desc {
  color: #888;
  font-size: 1.1rem;
}
.dashboard-row {
  margin-bottom: 20px; 
}
.stat-card {
  text-align: center;
  padding: 18px 0;
}
.stat-title {
  font-size: 16px;
  color: #888;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #222;
}
.stat-value.success {
  color: #67c23a;
}
.stat-value.warning {
  color: #e6a23c;
}
.stat-value.danger {
  color: #f56c6c;
}
.chart-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.dashboard-row .el-col {
  display: flex;
}
.dashboard-row .el-card {
  flex: 1 1 auto;
  min-height: 320px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

@media (max-width: 900px) {
  .dashboard-row .el-col {
    display: block;
  }
  .dashboard-row .el-card {
    min-height: unset;
  }
}
</style> 