<template>
  <div class="flight-list-container">
    <div class="flight-list-header">
      <h1>Flight List</h1>
      <button class="create-flight-btn" @click="drawerOpen = true">+ Create Flight</button>
    </div>
    <div class="filters">
      <select v-model="filters.airline">
        <option value="">All Airlines</option>
        <option v-for="a in airlines" :key="a.code" :value="a.code">{{ a.name }}</option>
      </select>
      <select v-model="filters.origin">
        <option value="">All Origin</option>
        <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }} ({{s.code}})</option>
      </select>
      <select v-model="filters.destination">
        <option value="">All Destination</option>
        <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }} ({{s.code}})</option>
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
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flight in filteredFlights" :key="flight.id" :class="{ 'row-cancelled': flight.status === 'Cancelled' }">
            <td>{{ flight.flightNumber }}</td>
            <td>{{ flight.airline?.name }}</td>
            <td>{{ flight.aircraftType?.name }}</td>
            <td>{{ flight.origin?.name }}</td>
            <td>{{ flight.destination?.name }}</td>
            <td>{{ flight.flightDate }}</td>
            <td>{{ flight.std }}</td>
            <td>{{ flight.sta }}</td>
            <td>{{ getFlightTypeAbbreviation(flight.flightType) }}</td>
            <td :class="{ 'delay-cell': flight.delay > 0 }">{{ flight.delay || 0 }}</td>
            <td>
              <span :class="flight.status.toLowerCase()">{{ flight.status }}</span>
            </td>
            <td>
              <button @click="editFlight(flight)" class="edit-btn">Edit</button>
              <button @click="handleDelete(flight.id)" class="delete-btn">Delete</button>
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
              <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Destination
            <select v-model="editFlightData.destination" required>
              <option value="">Select</option>
              <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }}</option>
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
          <div class="form-group"><label>Status
            <select v-model="editFlightData.status" required>
                <option>Scheduled</option>
                <option>Cancelled</option>
            </select>
          </label></div>
          <div class="form-group"><label>Delay <input type="number" v-model.number="editFlightData.delay" /></label></div>
          <button type="submit">Save Changes</button>
          <button type="button" @click="editModal = false">Cancel</button>
        </form>
        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
      </div>
    </div>
    <div v-if="drawerOpen" class="drawer-backdrop" @click.self="drawerOpen = false">
      <div class="drawer">
        <h2>Add Flight</h2>
        <form @submit.prevent="handleCreateSubmit">
           <div class="form-group"><label>Airline
            <select v-model="createForm.airline" required @change="onAirlineChange">
              <option value="">Select</option>
              <option v-for="a in airlines" :key="a.code" :value="a.code">{{ a.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Flight Number
            <input
              v-model="createForm.flightNumber"
              required
              :disabled="!createForm.airline"
              :placeholder="createForm.airline ? '(ex. ' + createForm.airline + '1234)' : ''"
              class="flight-number-input"
            />
          </label></div>
        
          <div class="form-group"><label>Aircraft Type
            <select v-model="createForm.aircraftType" required>
              <option value="">Select</option>
              <option v-for="a in aircraftTypes" :key="a.code" :value="a.code">{{ a.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Origin
            <select v-model="createForm.origin" required>
              <option value="">Select</option>
              <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Destination
            <select v-model="createForm.destination" required>
              <option value="">Select</option>
              <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Date <input type="date" v-model="createForm.date" required /></label></div>
          <div class="form-group"><label>STD <input type="time" v-model="createForm.std" required /></label></div>
          <div class="form-group"><label>STA <input type="time" v-model="createForm.sta" required /></label></div>
          <div class="form-group"><label>Flight Type
            <select v-model="createForm.flightType" required>
              <option value="">Select</option>
              <option v-for="f in flightTypes" :key="f.code" :value="f.code">{{ f.name }}</option>
            </select>
          </label></div>
          <div class="form-group"><label>Status
            <select v-model="createForm.status" required>
                <option>Scheduled</option>
                <option>Cancelled</option>
            </select>
          </label></div>
          <div class="form-group"><label>Delay <input type="number" v-model.number="createForm.delay" /></label></div>
          <button type="submit">Save</button>
          <button type="button" @click="drawerOpen = false">Cancel</button>
        </form>
        <div v-if="createErrorMsg" class="error-msg">{{ createErrorMsg }}</div>
        <div v-if="createSuccessMsg" class="success-msg">{{ createSuccessMsg }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { getFlights, updateFlight, deleteFlight, getAirlines, getStations, getFlightTypes, getAircraftTypes, addFlight } from '../services/flightService';
import { connectFlightWebSocket, disconnectFlightWebSocket } from '../services/websocketService';

const flights = ref([]);
const airlines = ref([]);
const stations = ref([]);
const flightTypes = ref([]);
const aircraftTypes = ref([]);
const errorMsg = ref('');
const drawerOpen = ref(false);
const createForm = ref({
  flightNumber: '',
  airline: '',
  aircraftType: '',
  origin: '',
  destination: '',
  date: '',
  std: '',
  sta: '',
  flightType: '',
  delay: 0,
  status: 'Scheduled',
});
const createErrorMsg = ref('');
const createSuccessMsg = ref('');

const filters = ref({
  airline: '',
  origin: '',
  destination: '',
  flightType: '',
  search: '',
});

const editModal = ref(false);
const editFlightData = ref({});

async function fetchData() {
    try {
        [flights.value, airlines.value, stations.value, flightTypes.value, aircraftTypes.value] = await Promise.all([
            getFlights(),
            getAirlines(),
            getStations(),
            getFlightTypes(),
            getAircraftTypes()
        ]);
    } catch (e) {
        errorMsg.value = "Failed to fetch data: " + e.message;
    }
}
onMounted(() => {
  fetchData();
  connectFlightWebSocket((flightList) => {
    flights.value = flightList;
  });
});
onUnmounted(() => {
  disconnectFlightWebSocket();
});

const filteredFlights = computed(() => {
  if (!flights.value) return [];
  return flights.value.filter(f =>
    (!filters.value.airline || (f.airline && f.airline.code === filters.value.airline)) &&
    (!filters.value.origin || (f.origin && f.origin.code === filters.value.origin)) &&
    (!filters.value.destination || (f.destination && f.destination.code === filters.value.destination)) &&
    (!filters.value.flightType || f.flightType === filters.value.flightType) &&
    (!filters.value.search || f.flightNumber.toLowerCase().includes(filters.value.search.toLowerCase()))
  );
});

async function handleDelete(id) {
    if (confirm('Are you sure you want to delete this flight?')) {
        try {
            await deleteFlight(id);
            fetchData(); // Refresh list
        } catch (e) {
            errorMsg.value = "Failed to delete flight: " + e.message;
}
    }
}

function editFlight(flight) {
  errorMsg.value = '';
  editFlightData.value = {
    id: flight.id,
    flightNumber: flight.flightNumber,
    airline: flight.airline?.code,
    aircraftType: flight.aircraftType?.code,
    origin: flight.origin?.code,
    destination: flight.destination?.code,
    date: flight.flightDate,
    std: flight.std,
    sta: flight.sta,
    flightType: flight.flightType,
    delay: flight.delay || 0,
    status: flight.status,
  };
  editModal.value = true;
}

async function saveEdit() {
    errorMsg.value = '';
    try {
        await updateFlight(editFlightData.value.id, editFlightData.value);
  editModal.value = false;
        fetchData(); // Refresh list
    } catch(e) {
        errorMsg.value = "Failed to update flight: " + e.message;
    }
}

async function handleCreateSubmit() {
  createErrorMsg.value = '';
  createSuccessMsg.value = '';
  try {
    await addFlight(createForm.value);
    createSuccessMsg.value = 'Flight created successfully!';
    drawerOpen.value = false;
    fetchData();
    Object.keys(createForm.value).forEach(k => createForm.value[k] = k === 'delay' ? 0 : (k === 'status' ? 'Scheduled' : ''));
  } catch (e) {
    createErrorMsg.value = e.message || 'Failed to create flight.';
  }
}

function onAirlineChange() {
  createForm.value.flightNumber = '';
}

function getFlightTypeAbbreviation(flightType) {
  const type = flightTypes.value.find(t => t.code === flightType);
  return type ? type.code : flightType;
}
</script>

<style scoped>
.flight-list-container {
  width: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}
.flight-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}
.create-flight-btn {
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
  margin-right: 65px;
}
.create-flight-btn:hover {
  background: #1565c0;
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
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
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
  padding: 12px 15px;
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
  z-index: 100;
}
.modal {
  background-color: #fff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px #0001;
  width: 600px;
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
  color: #d32f2f;
  font-weight: bold;
}
.scheduled {
    color: #388e3c;
    font-weight: bold;
}
.edit-btn, .delete-btn {
    padding: 5px 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    color: white;
}
.edit-btn {
    background-color: #1976d2;
}
.delete-btn {
    background-color: #d32f2f;
    margin-left: 5px;
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
.error-msg {
    color: #d32f2f;
    margin-top: 1rem;
}
.drawer-backdrop {
  position: fixed;
  top: 0;
  right: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.2);
  z-index: 200;
  display: flex;
  justify-content: flex-end;
  align-items: stretch;
}
.drawer {
  background: #fff;
  display: flex;
  flex-direction: column;
  width: 650px;
  max-width: 100vw;
  height: 100vh;
  box-shadow: -2px 0 8px #0002;
  padding: 3rem 1rem 2rem 4rem;
  overflow-y: auto;
  position: relative;
  animation: slideInDrawer 0.3s cubic-bezier(.4,0,.2,1);
  text-align: left;
}
@keyframes slideInDrawer {
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
}
.drawer h2 {
  text-align: center;
  width: 100%;
  margin-bottom: 1.5rem;
}
.drawer .form-group {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.drawer label {
  width: 100%;
  text-align: left;
}
.drawer input,
.drawer select {
  text-align: center;
}
.success-msg {
  color: #388e3c;
  background: #e8f5e9;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  margin-top: 1rem;
}
.flight-number-input::placeholder {
  color: #b0b0b0;
  opacity: 0.7;
  font-style: italic;
}
</style> 