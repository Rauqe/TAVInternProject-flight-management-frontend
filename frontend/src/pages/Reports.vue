<template>
  <div class="reports">
    <h1>Operational Reports</h1>
    <div class="report-section">
      <h2>Daily Flight List</h2>
      <input type="date" v-model="selectedDate" />
      <button @click="exportCSV">Export CSV</button>
      <table>
        <thead>
          <tr>
            <th>Flight Number</th>
            <th>Airline</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>Date</th>
            <th>STD</th>
            <th>STA</th>
            <th>Flight Type</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="f in dailyFlights" :key="f.id">
            <td>{{ f.flightNumber }}</td>
            <td>{{ f.airline?.name || f.airline?.code || f.airline }}</td>
            <td>{{ f.origin?.name || f.origin?.code || f.origin }}</td>
            <td>{{ f.destination?.name || f.destination?.code || f.destination }}</td>
            <td>{{ f.date || f.flightDate }}</td>
            <td>{{ f.std }}</td>
            <td>{{ f.sta }}</td>
            <td>{{ f.flightType }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="report-section">
      <h2>Delayed Flights</h2>
      <table>
        <thead>
          <tr>
            <th>Flight Number</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>STD</th>
            <th>STA</th>
            <th>Delay (min)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="f in delayedFlights" :key="f.id">
            <td>{{ f.flightNumber }}</td>
            <td>{{ f.origin?.name || f.origin?.code || f.origin }}</td>
            <td>{{ f.destination?.name || f.destination?.code || f.destination }}</td>
            <td>{{ f.std }}</td>
            <td>{{ f.sta }}</td>
            <td>{{ f.delay }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="report-section">
      <h2>Summary Statistics</h2>
      <div class="summary-statistics">
        <div class="stat-box">
          <div class="stat-title">Total Flights</div>
          <div class="stat-value">{{ totalFlights }}</div>
        </div>
        <div class="stat-box">
          <div class="stat-title">Delay Rate</div>
          <div class="stat-value">{{ delayRate }}%</div>
        </div>
        <div class="stat-box">
          <div class="stat-title">Cancel Rate</div>
          <div class="stat-value">{{ cancelRate }}%</div>
        </div>
      </div>
      <div class="chart">
        <div class="bar" :style="{width: delayRate + '%'}">Delay</div>
        <div class="bar cancel" :style="{width: cancelRate + '%'}">Cancel</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getFlights } from '../services/flightService';
const flights = ref([]);
const selectedDate = ref(new Date().toISOString().split('T')[0]);
onMounted(async () => {
  flights.value = await getFlights();
});
const dailyFlights = computed(() => flights.value.filter(f => (f.date || f.flightDate) === selectedDate.value));
const delayedFlights = computed(() => flights.value.filter(f => f.delay && f.delay > 0));
const totalFlights = computed(() => flights.value.length);
const delayedCount = computed(() => flights.value.filter(f => f.delay && f.delay > 0).length);
const cancelledCount = computed(() => flights.value.filter(f => f.status === 'Cancelled').length);
const delayRate = computed(() => totalFlights.value ? Math.round((delayedCount.value / totalFlights.value) * 100) : 0);
const cancelRate = computed(() => totalFlights.value ? Math.round((cancelledCount.value / totalFlights.value) * 100) : 0);
function exportCSV() {
  const rows = [
    ['Flight Number','Airline','Origin','Destination','Date','STD','STA','Flight Type'],
    ...dailyFlights.value.map(f => [
      f.flightNumber,
      f.airline?.name || f.airline?.code || f.airline,
      f.origin?.name || f.origin?.code || f.origin,
      f.destination?.name || f.destination?.code || f.destination,
      f.date || f.flightDate,
      f.std,
      f.sta,
      f.flightType
    ])
  ];
  const csv = rows.map(r => r.join(',')).join('\n');
  const blob = new Blob([csv], { type: 'text/csv' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'daily_flights.csv';
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
.summary-statistics {
  display: flex;
  gap: 2rem;
  margin-bottom: 1.5rem;
}
.stat-box {
  background: #f5f5f5;
  border-radius: 8px;
  padding: 1rem 2rem;
  text-align: center;
  min-width: 120px;
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