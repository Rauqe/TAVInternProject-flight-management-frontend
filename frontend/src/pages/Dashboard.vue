<template>
  <div class="airline-dashboard">
    <div class="dashboard-header">
      <div class="header-content">
        <h1>Flight Management System</h1>
        <p class="subtitle">TAV Airlines - Operations Dashboard</p>
      </div>
      <div class="header-stats">
        <div class="stat-item">
          <span class="stat-number">{{ totalFlights }}</span>
          <span class="stat-label">Active Flights</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ onTimeRate }}%</span>
          <span class="stat-label">On-Time Performance</span>
        </div>
      </div>
    </div>

    <div class="main-navigation">
      <div class="nav-section">
        <h2>Flight Operations</h2>
        <div class="nav-grid">
          <router-link to="/flights" class="nav-card primary">
            <div class="card-icon">✈️</div>
            <div class="card-content">
              <h3>Flight Management</h3>
              <p>View, edit and manage all flight schedules</p>
            </div>
          </router-link>
          
          <router-link :to="{ path: '/flights', query: { add: 1 } }" class="nav-card secondary">
            <div class="card-icon">➕</div>
            <div class="card-content">
              <h3>Add New Flight</h3>
              <p>Create new flight schedules</p>
            </div>
          </router-link>
        </div>
      </div>

      <div class="nav-section">
        <h2>Reports & Analytics</h2>
        <div class="nav-grid">
          <router-link to="/reports" class="nav-card primary">
            <div class="card-icon">📊</div>
            <div class="card-content">
              <h3>Operational Reports</h3>
              <p>Performance analytics and insights</p>
            </div>
          </router-link>
          
          <router-link to="/archive" class="nav-card secondary">
            <div class="card-icon">📋</div>
            <div class="card-content">
              <h3>Flight Archive</h3>
              <p>Historical flight data and records</p>
            </div>
          </router-link>
        </div>
      </div>
    </div>

    <!-- Performance Overview -->
    <div class="performance-section">
      <h2>Performance Overview</h2>
      <div class="performance-grid">
        <div class="perf-card">
          <div class="perf-header">
            <h3>Flight Status</h3>
          </div>
          <div class="perf-content">
            <div class="perf-item">
              <span class="perf-label">Total Flights</span>
              <span class="perf-value">{{ totalFlights }}</span>
            </div>
            <div class="perf-item">
              <span class="perf-label">On Time</span>
              <span class="perf-value success">{{ onTimeRate }}%</span>
            </div>
            <div class="perf-item">
              <span class="perf-label">Delayed</span>
              <span class="perf-value warning">{{ delayRate }}%</span>
            </div>
            <div class="perf-item">
              <span class="perf-label">Cancelled</span>
              <span class="perf-value danger">{{ cancelRate }}%</span>
            </div>
          </div>
        </div>

        <div class="perf-card">
          <div class="perf-header">
            <h3>System Status</h3>
          </div>
          <div class="perf-content">
            <div class="status-item">
              <span :class="['status-dot', systemStatus.database ? 'online' : 'offline']"></span>
              <span class="status-text">Database Connection</span>
            </div>
            <div class="status-item">
              <span :class="['status-dot', systemStatus.api ? 'online' : 'offline']"></span>
              <span class="status-text">API Services</span>
            </div>
            <div class="status-item">
              <span :class="['status-dot', systemStatus.kafka ? 'online' : 'offline']"></span>
              <span class="status-text">Kafka</span>
            </div>
            <div class="status-item">
              <span class="status-dot online"></span>
              <span class="status-text">Flight Tracking</span>
            </div>
            <div class="status-item">
              <span class="status-dot online"></span>
              <span class="status-text">Reporting System</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent Activity -->
    <div class="activity-section">
      <h2>Recent Activity</h2>
      <div class="activity-list">
        <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
          <div class="activity-time">{{ activity.time }}</div>
          <div class="activity-content">
            <div class="activity-title">{{ activity.text }}</div>
            <div class="activity-type">{{ activity.type }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getFlights } from '../services/flightService';
import { connectFlightWebSocket, disconnectFlightWebSocket } from '../services/websocketService';
import axios from 'axios';

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
      recentActivities: []
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
        // Hem flightDate+std hem id ile sırala, en güncel 5 uçuşu bul
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
      this.recentActivities = dynamic.length > 0 ? dynamic : [this.staticActivities[0]];
    }
  },
  async mounted() {
    this.flights = await getFlights();
    connectFlightWebSocket((flightList) => {
      this.flights = flightList;
      this.updateRecentActivities();
    });
    await this.fetchSystemStatus();
    this.updateRecentActivities();
  },
  beforeUnmount() {
    disconnectFlightWebSocket();
  },
  watch: {
    flights: {
      handler() {
        this.updateRecentActivities();
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.airline-dashboard {
  background: #ffffff;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  height: 100%;
}

/* Header Section */
.dashboard-header {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  color: white;
  padding: 3rem 2rem;
  border-radius: 12px;
  margin-bottom: 3rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h1 {
  font-size: 2.5rem;
  font-weight: 300;
  margin: 0 0 0.5rem 0;
  letter-spacing: -0.5px;
}

.subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
  margin: 0;
  font-weight: 300;
}

.header-stats {
  display: flex;
  gap: 3rem;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2.5rem;
  font-weight: 300;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Main Navigation */
.main-navigation {
  margin-bottom: 3rem;
}

.nav-section {
  margin-bottom: 2.5rem;
}

.nav-section h2 {
  font-size: 1.5rem;
  color: #1f2937;
  margin-bottom: 1.5rem;
  font-weight: 500;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 0.5rem;
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.nav-card {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 2rem;
  text-decoration: none;
  color: inherit;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.nav-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
  transform: translateY(-2px);
}

.nav-card.primary {
  border-left: 4px solid #3b82f6;
}

.nav-card.secondary {
  border-left: 4px solid #6b7280;
}

.card-icon {
  font-size: 2rem;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8fafc;
  border-radius: 8px;
}

.card-content h3 {
  font-size: 1.25rem;
  color: #1f2937;
  margin: 0 0 0.5rem 0;
  font-weight: 500;
}

.card-content p {
  color: #6b7280;
  margin: 0;
  font-size: 0.95rem;
  line-height: 1.5;
}

/* Performance Section */
.performance-section {
  margin-bottom: 3rem;
}

.performance-section h2 {
  font-size: 1.5rem;
  color: #1f2937;
  margin-bottom: 1.5rem;
  font-weight: 500;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 0.5rem;
}

.performance-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
}

.perf-card {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.perf-header {
  background: #f8fafc;
  padding: 1.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.perf-header h3 {
  margin: 0;
  font-size: 1.1rem;
  color: #374151;
  font-weight: 500;
}

.perf-content {
  padding: 1.5rem;
}

.perf-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f3f4f6;
}

.perf-item:last-child {
  border-bottom: none;
}

.perf-label {
  color: #6b7280;
  font-size: 0.95rem;
}

.perf-value {
  font-weight: 600;
  font-size: 1.1rem;
  color: #1f2937;
}

.perf-value.success {
  color: #059669;
}

.perf-value.warning {
  color: #d97706;
}

.perf-value.danger {
  color: #dc2626;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f3f4f6;
}

.status-item:last-child {
  border-bottom: none;
}

.status-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.status-dot.online {
  background: #388e3c;
}

.status-dot.offline {
  background: #d32f2f;
}

.status-text {
  color: #374151;
  font-size: 0.95rem;
}

/* Activity Section */
.activity-section {
  margin-bottom: 2rem;
}

.activity-section h2 {
  font-size: 1.5rem;
  color: #1f2937;
  margin-bottom: 1.5rem;
  font-weight: 500;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 0.5rem;
}

.activity-list {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 1.5rem;
  border-bottom: 1px solid #f3f4f6;
  transition: background 0.2s ease;
}

.activity-item:hover {
  background: #f8fafc;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  color: #6b7280;
  font-size: 0.9rem;
  font-weight: 500;
  min-width: 80px;
}

.activity-content {
  flex: 1;
}

.activity-title {
  color: #1f2937;
  font-weight: 500;
  margin-bottom: 0.25rem;
}

.activity-type {
  color: #6b7280;
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Responsive Design */
@media (max-width: 768px) {
  .airline-dashboard {
    padding: 1rem;
  }
  
  .dashboard-header {
    flex-direction: column;
    text-align: center;
    gap: 2rem;
  }
  
  .header-stats {
    gap: 2rem;
  }
  
  .nav-grid {
    grid-template-columns: 1fr;
  }
  
  .performance-grid {
    grid-template-columns: 1fr;
  }
  
  .nav-card {
    flex-direction: column;
    text-align: center;
  }
}
</style> 