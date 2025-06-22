<template>
  <div class="reference-data-container">
    <h1>Reference Data Management</h1>
    
    <div class="tab-navigation">
      <button 
        v-for="tab in tabs" 
        :key="tab.id" 
        :class="{ active: activeTab === tab.id }" 
        @click="activeTab = tab.id">
        {{ tab.name }}
      </button>
    </div>

    <div class="tab-content">
      <div v-if="activeTab === 'airlines'">
        <h2>Airlines</h2>
        <form @submit.prevent="addAirline">
          <input v-model="newAirline.code" placeholder="Code (e.g., TK)" maxlength="3" required />
          <input v-model="newAirline.name" placeholder="Name" required />
          <button type="submit">Add Airline</button>
        </form>
        <ul>
          <li v-for="a in airlines" :key="a.code">
            <span>{{ a.code }} - {{ a.name }}</span>
            <button @click="removeAirline(a.code)" class="delete-btn">Delete</button>
          </li>
        </ul>
      </div>

      <div v-if="activeTab === 'aircraftTypes'">
        <h2>Aircraft Types</h2>
        <form @submit.prevent="addAircraftType">
          <input v-model="newAircraft.code" placeholder="Code (e.g., A320)" maxlength="5" required />
          <input v-model="newAircraft.name" placeholder="Name" required />
          <button type="submit">Add Aircraft Type</button>
        </form>
        <ul>
          <li v-for="a in aircraftTypes" :key="a.code">
            <span>{{ a.code }} - {{ a.name }}</span>
            <button @click="removeAircraftType(a.code)" class="delete-btn">Delete</button>
          </li>
        </ul>
      </div>

       <div v-if="activeTab === 'stations'">
        <h2>Stations</h2>
        <form @submit.prevent="addStation">
            <input v-model="newStation.code" placeholder="Code (e.g., IST)" maxlength="4" required />
            <input v-model="newStation.name" placeholder="Name" required />
            <input v-model="newStation.country" placeholder="Country" required />
            <button type="submit">Add Station</button>
        </form>
        <ul>
            <li v-for="s in stations" :key="s.code">
                <span>{{ s.code }} - {{ s.name }} ({{ s.country }})</span>
                <button @click="removeStation(s.code)" class="delete-btn">Delete</button>
            </li>
        </ul>
      </div>
      
      <div v-if="activeTab === 'routes'">
        <h2>Routes</h2>
        <form @submit.prevent="addRoute">
          <select v-model="newRoute.originCode" required>
            <option disabled value="">Select Origin</option>
            <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }} ({{ s.code }})</option>
          </select>
          <select v-model="newRoute.destinationCode" required>
            <option disabled value="">Select Destination</option>
            <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.name }} ({{ s.code }})</option>
          </select>
          <button type="submit">Add Route</button>
        </form>
        <ul>
          <li v-for="r in routes" :key="r.id">
            <span>{{ r.origin?.name }} ({{ r.origin?.code }}) → {{ r.destination?.name }} ({{ r.destination?.code }})</span>
            <button @click="removeRoute(r.id)" class="delete-btn">Delete</button>
          </li>
        </ul>
      </div>

      <div v-if="activeTab === 'flightTypes'">
        <h2>Flight Types</h2>
        <form @submit.prevent="addFlightType">
          <input v-model="newFlightType.code" placeholder="Code (e.g., PAX)" maxlength="16" required />
          <input v-model="newFlightType.name" placeholder="Name" required />
          <button type="submit">Add Flight Type</button>
        </form>
        <ul>
          <li v-for="f in flightTypes" :key="f.code">
            <span>{{ f.code }} - {{ f.name }}</span>
            <button @click="removeFlightType(f.code)" class="delete-btn">Delete</button>
          </li>
        </ul>
      </div>

    </div>
    <div v-if="error" class="error-toast">{{ error }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {
  getAirlines, addAirline as addAirlineService, deleteAirline,
  getAircraftTypes, addAircraftType as addAircraftTypeService, deleteAircraftType,
  getStations, addStation as addStationService, deleteStation,
  getRoutes, addRoute as addRouteService, deleteRoute,
  getFlightTypes, addFlightType as addFlightTypeService, deleteFlightType
} from '../services/flightService';

const activeTab = ref('airlines');
const tabs = [
  { id: 'airlines', name: 'Airlines' },
  { id: 'aircraftTypes', name: 'Aircraft Types' },
  { id: 'stations', name: 'Stations' },
  { id: 'routes', name: 'Routes' },
  { id: 'flightTypes', name: 'Flight Types' },
];

const airlines = ref([]);
const aircraftTypes = ref([]);
const stations = ref([]);
const routes = ref([]);
const flightTypes = ref([]);
const error = ref('');

const newAirline = ref({ code: '', name: '' });
const newAircraft = ref({ code: '', name: '' });
const newStation = ref({ code: '', name: '', country: '' });
const newRoute = ref({ originCode: '', destinationCode: '' });
const newFlightType = ref({ code: '', name: '' });

async function refreshAll() {
  try {
    airlines.value = await getAirlines();
    aircraftTypes.value = await getAircraftTypes();
    stations.value = await getStations();
    routes.value = await getRoutes();
    flightTypes.value = await getFlightTypes();
  } catch (e) {
    error.value = e.message;
  }
}
onMounted(refreshAll);

async function addAirline() {
  try {
    await addAirlineService(newAirline.value);
    newAirline.value = { code: '', name: '' };
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function removeAirline(code) {
  try {
    await deleteAirline(code);
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function addAircraftType() {
  try {
    await addAircraftTypeService(newAircraft.value);
    newAircraft.value = { code: '', name: '' };
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function removeAircraftType(code) {
  try {
    await deleteAircraftType(code);
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function addStation() {
  try {
    await addStationService(newStation.value);
    newStation.value = { code: '', name: '', country: '' };
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function removeStation(code) {
  try {
    await deleteStation(code);
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function addRoute() {
  try {
    await addRouteService(newRoute.value);
    newRoute.value = { originCode: '', destinationCode: '' };
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function removeRoute(id) {
  try {
    await deleteRoute(id);
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function addFlightType() {
  try {
    await addFlightTypeService(newFlightType.value);
    newFlightType.value = { code: '', name: '' };
    await refreshAll();
  } catch (e) { error.value = e.message; }
}

async function removeFlightType(code) {
  try {
    await deleteFlightType(code);
    await refreshAll();
  } catch (e) { error.value = e.message; }
}
</script>

<style scoped>
.reference-data-container {
  width: 100%;
}

.tab-navigation {
  display: flex;
  border-bottom: 2px solid #ccc;
  margin-bottom: 2rem;
}

.tab-navigation button {
  padding: 1rem 1.5rem;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 1rem;
  color: #555;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
}

.tab-navigation button.active {
  color: var(--primary-color, #1976d2);
  border-bottom-color: var(--primary-color, #1976d2);
}

.tab-content h2 {
  margin-top: 0;
}

.tab-content form {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  align-items: center;
}

.tab-content input,
.tab-content select {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  flex-grow: 1;
}

.tab-content button[type="submit"] {
  flex-grow: 0;
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color, #1976d2);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.tab-content ul {
  list-style: none;
  padding: 0;
}

.tab-content li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  border-bottom: 1px solid #eee;
}

.tab-content li:nth-child(odd) {
  background-color: #f9f9f9;
}

.delete-btn {
    background-color: #e53935;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 4px;
    cursor: pointer;
}

.error-toast {
    position: fixed;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    background-color: #d32f2f;
    color: white;
    padding: 1rem 2rem;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
</style> 