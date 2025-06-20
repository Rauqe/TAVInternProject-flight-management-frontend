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
            <td>{{ f.airline }}</td>
            <td>{{ f.origin }}</td>
            <td>{{ f.destination }}</td>
            <td>{{ f.date }}</td>
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
            <td>{{ f.origin }}</td>
            <td>{{ f.destination }}</td>
            <td>{{ f.std }}</td>
            <td>{{ f.sta }}</td>
            <td>{{ f.delay }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="report-section">
      <h2>Summary Statistics</h2>
      <div class="stats">
        <div class="stat">Total Flights: <b>{{ flights.length }}</b></div>
        <div class="stat">Delay Rate: <b>{{ delayRate }}%</b></div>
        <div class="stat">Cancel Rate: <b>{{ cancelRate }}%</b></div>
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
const dailyFlights = computed(() => flights.value.filter(f => f.date === selectedDate.value));
const delayedFlights = computed(() => flights.value
  .filter(f => f.delay && f.delay > 0)
);
const delayRate = computed(() => flights.value.length ? Math.round(100 * flights.value.filter(f => f.delay && f.delay > 0).length / flights.value.length) : 0);
const cancelRate = computed(() => flights.value.length ? Math.round(100 * flights.value.filter(f => f.status === 'Cancelled').length / flights.value.length) : 0);
function exportCSV() {
  const rows = [
    ['Flight Number','Airline','Origin','Destination','Date','STD','STA','Flight Type'],
    ...dailyFlights.value.map(f => [f.flightNumber, f.airline, f.origin, f.destination, f.date, f.std, f.sta, f.flightType])
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
.stats {
  display: flex;
  gap: 2rem;
  margin-bottom: 1rem;
}
.stat {
  background: #f5f5f5;
  padding: 1rem 2rem;
  border-radius: 6px;
  font-size: 1.1em;
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