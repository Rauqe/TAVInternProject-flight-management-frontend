<template>
  <div class="flight-archive">
    <h1>Flight Archive</h1>
    <div class="filters">
      <label>Start Date <input type="date" v-model="startDate" /></label>
      <label>End Date <input type="date" v-model="endDate" /></label>
      <button @click="filterArchive">Filter</button>
    </div>
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
        <tr v-for="f in filteredArchive" :key="f.id">
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
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getFlights } from '../services/flightService';
const archive = ref([]);
const startDate = ref('');
const endDate = ref('');

onMounted(async () => {
  const allFlights = await getFlights();
  const today = new Date().toISOString().split('T')[0];
  archive.value = allFlights.filter(f => f.date < today);
  // Set default filter range to cover all archive
  if (archive.value.length) {
    startDate.value = archive.value[0].date;
    endDate.value = archive.value[archive.value.length - 1].date;
  }
});

const filteredArchive = computed(() =>
  archive.value.filter(f => (!startDate.value || f.date >= startDate.value) && (!endDate.value || f.date <= endDate.value))
);
function filterArchive() {
  // Just to trigger computed
  startDate.value = startDate.value;
  endDate.value = endDate.value;
}
</script>

<style scoped>
.flight-archive {
  width: 100%;
  box-sizing: border-box;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.flight-archive > * {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
}
.filters {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
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
</style> 