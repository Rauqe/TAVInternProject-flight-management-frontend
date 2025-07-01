<template>
  <div class="reports">
    <h1>Operational Reports</h1>

    <!-- Chart Visualizations -->
    <div class="report-section">
      <FlightStatusChart :data="flightStatusData" />
    </div>
    <div class="report-section">
      <TotalFlightsBarChart :data="airlinePerformanceData" />
    </div>
    <div class="report-section">
      <DelayRateChart :data="airlinePerformanceData" />
    </div>
    <div class="report-section">
      <DailyTrendChart :data="dailyTrendData" />
    </div>

    <div class="report-section">
      <h2>Daily Flight List</h2>
      <div style="margin-bottom:1rem; display: flex; align-items: center; gap: 1rem;">
        <el-date-picker v-model="startDate" type="date" placeholder="Start Date" style="width: 140px" />
        <el-date-picker v-model="endDate" type="date" placeholder="End Date" style="width: 140px" />
        <el-button type="primary" @click="exportCSV">Export CSV</el-button>
      </div>
      <el-table :data="filteredFlights" style="width: 100%" :empty-text="'No flights found'">
        <el-table-column prop="flightNumber" label="Flight Number" />
        <el-table-column prop="airline" label="Airline">
          <template #default="scope">{{ scope.row.airline?.code || scope.row.airline?.name || scope.row.airline }}</template>
        </el-table-column>
        <el-table-column prop="aircraftType" label="Aircraft Type">
          <template #default="scope">{{ scope.row.aircraftType || 'A320' }}</template>
        </el-table-column>
        <el-table-column prop="origin" label="Origin">
          <template #default="scope">{{ scope.row.origin?.code || scope.row.origin?.name || scope.row.origin }}</template>
        </el-table-column>
        <el-table-column prop="destination" label="Destination">
          <template #default="scope">{{ scope.row.destination?.code || scope.row.destination?.name || scope.row.destination }}</template>
        </el-table-column>
        <el-table-column prop="date" label="Date">
          <template #default="scope">{{ scope.row.date || scope.row.flightDate }}</template>
        </el-table-column>
        <el-table-column prop="std" label="STD" />
        <el-table-column prop="sta" label="STA" />
        <el-table-column prop="flightType" label="Flight Type">
          <template #default="scope">{{ scope.row.flightType || 'Passenger' }}</template>
        </el-table-column>
        <el-table-column prop="delay" label="Delay (min)">
          <template #default="scope">{{ scope.row.delay || 0 }}</template>
        </el-table-column>
        <el-table-column prop="status" label="Status">
          <template #default="scope">{{ scope.row.status || 'Scheduled' }}</template>
        </el-table-column>
      </el-table>
    </div>
    <div class="report-section">
      <h2>Delayed Flights</h2>
      <el-table :data="delayedFlights" style="width: 100%" :empty-text="'No delayed flights'">
        <el-table-column prop="flightNumber" label="Flight Number" />
        <el-table-column prop="origin" label="Origin">
          <template #default="scope">{{ scope.row.origin?.name || scope.row.origin?.code || scope.row.origin }}</template>
        </el-table-column>
        <el-table-column prop="destination" label="Destination">
          <template #default="scope">{{ scope.row.destination?.name || scope.row.destination?.code || scope.row.destination }}</template>
        </el-table-column>
        <el-table-column prop="std" label="STD" />
        <el-table-column prop="sta" label="STA" />
        <el-table-column prop="delay" label="Delay (min)" />
      </el-table>
    </div>
    <div class="report-section">
      <h2>Cancelled Flights</h2>
      <el-table :data="cancelledFlights" style="width: 100%" :empty-text="'No cancelled flights'">
        <el-table-column prop="flightNumber" label="Flight Number" />
        <el-table-column prop="origin" label="Origin">
          <template #default="scope">{{ scope.row.origin?.name || scope.row.origin?.code || scope.row.origin }}</template>
        </el-table-column>
        <el-table-column prop="destination" label="Destination">
          <template #default="scope">{{ scope.row.destination?.name || scope.row.destination?.code || scope.row.destination }}</template>
        </el-table-column>
        <el-table-column prop="std" label="STD" />
        <el-table-column prop="sta" label="STA" />
        <el-table-column prop="status" label="Status" />
      </el-table>
    </div>
    <div class="report-section">
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getFlights } from '../services/flightService';
import FlightStatusChart from '../components/charts/FlightStatusChart.vue';
import TotalFlightsBarChart from '../components/charts/TotalFlightsBarChart.vue';
import DelayRateChart from '../components/charts/DelayRateChart.vue';
import DailyTrendChart from '../components/charts/DailyTrendChart.vue';
import { ElButton, ElTable, ElTableColumn, ElDatePicker } from 'element-plus';

const flights = ref([]);
const startDate = ref(new Date().toISOString().split('T')[0]);
const endDate = ref(new Date().toISOString().split('T')[0]);

onMounted(async () => {
  flights.value = await getFlights();
});
const filteredFlights = computed(() => {
  return flights.value.filter(f => {
    const date = f.date || f.flightDate;
    return date >= startDate.value && date <= endDate.value;
  });
});
const delayedFlights = computed(() => flights.value.filter(f => f.delay && f.delay > 0));
const totalFlights = computed(() => flights.value.length);
const delayedCount = computed(() => flights.value.filter(f => f.delay && f.delay > 0).length);
const cancelledCount = computed(() => flights.value.filter(f => f.status === 'Cancelled').length);
const delayRate = computed(() => totalFlights.value ? Math.round((delayedCount.value / totalFlights.value) * 100) : 0);
const cancelRate = computed(() => totalFlights.value ? Math.round((cancelledCount.value / totalFlights.value) * 100) : 0);
const cancelledFlights = computed(() => flights.value.filter(f => f.status === 'Cancelled'));

// Chart Data
const flightStatusData = computed(() => ({
  onTime: totalFlights.value - delayedCount.value - cancelledCount.value,
  delayed: delayedCount.value,
  cancelled: cancelledCount.value
}));

const airlinePerformanceData = computed(() => {
  // Group by airline
  const map = {};
  flights.value.forEach(f => {
    const airline = f.airline?.name || f.airline?.code || f.airline;
    if (!map[airline]) map[airline] = { airline, count: 0, delayed: 0 };
    map[airline].count++;
    if (f.delay && f.delay > 0) map[airline].delayed++;
  });
  return Object.values(map).map(a => ({
    airline: a.airline,
    count: a.count,
    delayRate: a.count ? Math.round((a.delayed / a.count) * 100) : 0
  }));
});

const dailyTrendData = computed(() => {
  // Group by date
  const map = {};
  flights.value.forEach(f => {
    const date = f.date || f.flightDate;
    if (!map[date]) map[date] = { date, totalFlights: 0, delayedFlights: 0 };
    map[date].totalFlights++;
    if (f.delay && f.delay > 0) map[date].delayedFlights++;
  });
  // Sort by date
  return Object.values(map).sort((a, b) => a.date.localeCompare(b.date));
});

function exportCSV() {
  if (!startDate.value || !endDate.value) return;
  const rows = [
    ['flightNumber','airline','aircraftType','origin','destination','date','std','sta','flightType','delay','status'],
    ...flights.value
      .filter(f => {
        const date = f.date || f.flightDate;
        return date >= startDate.value && date <= endDate.value;
      })
      .map(f => [
        f.flightNumber,
        f.airline?.code || f.airline?.name || f.airline,
        f.aircraftType || 'A320',
        f.origin?.code || f.origin?.name || f.origin,
        f.destination?.code || f.destination?.name || f.destination,
        f.date || f.flightDate,
        f.std,
        f.sta,
        f.flightType || 'Passenger',
        f.delay || 0,
        f.status || 'Scheduled' 
      ])
  ];
  const csv = rows.map(r => r.join(',')).join('\n');
  const blob = new Blob([csv], { type: 'text/csv' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `flights_${startDate.value}_to_${endDate.value}.csv`;
  a.click();
  URL.revokeObjectURL(url);
}
</script>

<style scoped>
.reports {
  width: 100%;
  box-sizing: border-box;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.report-section, .stats, .chart {
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
}
.report-section {
  margin-bottom: 2.5rem;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #eee;
  padding: 0.5rem 1rem;
  text-align: left;
}
th {
  background: #f5f5f5;
}
.stat-title {
  font-size: 1rem;
  color: #888;
}
.stat-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1976d2;
}
.chart {
  display: flex;
  gap: 1rem;
  align-items: flex-end;
  margin-bottom: 2rem;
}
.bar {
  height: 32px;
  background: #1976d2;
  color: #fff;
  border-radius: 4px;
  text-align: center;
  line-height: 32px;
  min-width: 40px;
  transition: width 0.5s;
}
.bar.cancel {
  background: #d32f2f;
}
</style>