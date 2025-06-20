<template>
  <div class="flight-list-container">
    <h1>Flight List</h1>
    <div class="filters">
      <select v-model="filters.airline">
        <option value="">All Airlines</option>
        <option v-for="a in airlines" :key="a.code" :value="a.code">{{ a.name }}</option>
      </select>
      <select v-model="filters.origin">
        <option value="">All Origin</option>
        <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.code }}</option>
      </select>
      <select v-model="filters.destination">
        <option value="">All Destination</option>
        <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.code }}</option>
      </select>
      <select v-model="filters.flightType">
        <option value="">All Types</option>
        <option v-for="f in flightTypes" :key="f.code" :value="f.code">{{ f.name }}</option>
      </select>
      <input v-model="filters.search" placeholder="Search flight number..." />
    </div>
    <div class="table-responsive">
      <table>
        <thead>
          <tr>
            <th>Flight Number</th>
            <th>Airline</th>
            <th>Aircraft Type</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>Date</th>
            <th>STD</th>
            <th>STA</th>
            <th>Flight Type</th>
            <th>Delay (min)</th>
            <th>Status</th>
            <th>Edit/Delete</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flight in filteredFlights" :key="flight.id" :class="{ 'row-cancelled': flight.status === 'Cancelled' }">
            <td>{{ flight.flightNumber }}</td>
            <td>{{ flight.airline }}</td>
            <td>{{ flight.aircraftType }}</td>
            <td>{{ flight.origin }}</td>
            <td>{{ flight.destination }}</td>
            <td>{{ flight.date }}</td>
            <td>{{ flight.std }}</td>
            <td>{{ flight.sta }}</td>
            <td>{{ flight.flightType }}</td>
            <td :class="{ 'delay-cell': flight.delay > 0 }">{{ flight.delay || 0 }}</td>
            <td>
              <span v-if="flight.status === 'Cancelled'" class="cancelled">Cancelled</span>
              <span v-else>Scheduled</span>
            </td>
            <td>
              <button @click="editFlight(flight)">Edit</button>
              <button @click="deleteFlight(flight.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="editModal" class="modal-backdrop">
      <div class="modal">
        <h2>Edit Flight</h2>
        <form @submit.prevent="saveEdit">
          <div class="form-group"><label>Flight Number <input v-model="editFlightData.flightNumber" required /></label></div>
          <div class="form-group"><label>Airline
            <select v-model="editFlightData.airline" required>
              <option value="">Select</option>
              <option v-for="a in airlines" :key="a.code" :value="a.code">{{ a.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Aircraft Type
            <select v-model="editFlightData.aircraftType" required>
              <option value="">Select</option>
              <option v-for="a in aircraftTypes" :key="a.code" :value="a.code">{{ a.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Origin
            <select v-model="editFlightData.origin" required>
              <option value="">Select</option>
              <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.code }} - {{ s.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Destination
            <select v-model="editFlightData.destination" required>
              <option value="">Select</option>
              <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.code }} - {{ s.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Date <input type="date" v-model="editFlightData.date" required /></label></div>
          <div class="form-group"><label>STD <input type="time" v-model="editFlightData.std" required /></label></div>
          <div class="form-group"><label>STA <input type="time" v-model="editFlightData.sta" required /></label></div>
          <div class="form-group"><label>Flight Type
            <select v-model="editFlightData.flightType" required>
              <option value="">Select</option>
              <option v-for="f in flightTypes" :key="f.code" :value="f.code">{{ f.name }}</option>
            </select>
          </label></div>
          <button type="submit">Save</button>
          <button type="button" @click="editModal = false">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { getFlights, getAirlines, getStations, getFlightTypes } from '../services/flightService';
const flights = ref([]);
const airlines = ref([]);
const stations = ref([]);
const flightTypes = ref([]);
const filters = ref({
  airline: '',
  origin: '',
  destination: '',
  flightType: '',
  search: '',
});
const editModal = ref(false);
const editFlightData = ref({});
onMounted(async () => {
  flights.value = await getFlights();
  airlines.value = await getAirlines();
  stations.value = await getStations();
  flightTypes.value = await getFlightTypes();
});
const filteredFlights = computed(() => {
  return flights.value.filter(f =>
    (!filters.value.airline || f.airline === filters.value.airline) &&
    (!filters.value.origin || f.origin === filters.value.origin) &&
    (!filters.value.destination || f.destination === filters.value.destination) &&
    (!filters.value.flightType || f.flightType === filters.value.flightType) &&
    (!filters.value.search || f.flightNumber.toLowerCase().includes(filters.value.search.toLowerCase()))
  );
});
function deleteFlight(id) {
  const idx = flights.value.findIndex(f => f.id === id);
  if (idx !== -1) flights.value.splice(idx, 1);
}
function editFlight(flight) {
  editFlightData.value = { ...flight };
  editModal.value = true;
}
function saveEdit() {
  const idx = flights.value.findIndex(f => f.id === editFlightData.value.id);
  if (idx !== -1) flights.value[idx] = { ...editFlightData.value };
  editModal.value = false;
}
</script>

<style scoped>
.flight-list-container {
  width: 100%;
  box-sizing: border-box;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}
.filters select, .filters input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.table-responsive {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}
table {
  width: 100%;
  min-width: 900px;
  border-collapse: collapse;
}
td, th {
  white-space: nowrap;
  text-align: center;
  padding: 8px 12px;
}
th {
  background: #f5f5f5;
}
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal {
  background-color: #fff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px #0001;
  width: 50%;
}
.form-group {
  margin-bottom: 1rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}
.form-group input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.form-group button {
  padding: 0.5rem 1rem;
  border: none;
  background-color: #007bff;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
}
.form-group button:hover {
  background-color: #0056b3;
}
.delay-cell {
  color: #d32f2f;
  font-weight: bold;
}
.cancelled {
  color: #888;
  text-decoration: line-through;
  font-weight: bold;
}
.row-cancelled {
  background: #f5f5f5;
  opacity: 0.7;
}
@media (max-width: 900px) {
  .table-responsive {
    margin-left: -16px;
    margin-right: -16px;
  }
  table {
    min-width: 600px;
  }
}
</style> 