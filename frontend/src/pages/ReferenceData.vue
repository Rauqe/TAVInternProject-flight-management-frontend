<template>
  <div class="reference-data">
    <h1>Referans Veri Yönetimi</h1>
    <div class="ref-section">
      <h2>Havayolları (Airline)</h2>
      <form @submit.prevent="addAirline">
        <input v-model="newAirline.code" placeholder="Kod (örn: TK)" maxlength="3" required />
        <input v-model="newAirline.name" placeholder="Ad" required />
        <button type="submit">Ekle</button>
      </form>
      <ul>
        <li v-for="a in airlines" :key="a.code">
          {{ a.code }} - {{ a.name }}
          <button @click="removeAirline(a.code)">Sil</button>
        </li>
      </ul>
    </div>
    <div class="ref-section">
      <h2>Uçak Tipleri (Aircraft Type)</h2>
      <form @submit.prevent="addAircraftType">
        <input v-model="newAircraft.code" placeholder="Kod (örn: A320)" maxlength="5" required />
        <input v-model="newAircraft.name" placeholder="Ad" required />
        <button type="submit">Ekle</button>
      </form>
      <ul>
        <li v-for="a in aircraftTypes" :key="a.code">
          {{ a.code }} - {{ a.name }}
          <button @click="removeAircraftType(a.code)">Sil</button>
        </li>
      </ul>
    </div>
    <div class="ref-section">
      <h2>Rotalar (Route)</h2>
      <form @submit.prevent="addRoute">
        <select v-model="newRoute.originCode" required>
          <option value="">Origin</option>
          <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.code }}</option>
        </select>
        <select v-model="newRoute.destinationCode" required>
          <option value="">Destination</option>
          <option v-for="s in stations" :key="s.code" :value="s.code">{{ s.code }}</option>
        </select>
        <button type="submit">Ekle</button>
      </form>
      <ul>
        <li v-for="r in routes" :key="r.id">
          {{ r.origin.code }} → {{ r.destination.code }}
          <button @click="removeRoute(r.id)">Sil</button>
        </li>
      </ul>
    </div>
    <div class="ref-section">
      <h2>Uçuş Tipleri (Flight Type)</h2>
      <form @submit.prevent="addFlightType">
        <input v-model="newFlightType.code" placeholder="Kod (örn: PAX)" maxlength="16" required />
        <input v-model="newFlightType.name" placeholder="Ad" required />
        <button type="submit">Ekle</button>
      </form>
      <ul>
        <li v-for="f in flightTypes" :key="f.code">
          {{ f.code }} - {{ f.name }}
          <button @click="removeFlightType(f.code)">Sil</button>
        </li>
      </ul>
    </div>
    <div class="ref-section">
      <h2>Havalimanları (Station)</h2>
      <form @submit.prevent="addStation">
        <input v-model="newStation.code" placeholder="Kod (örn: IST)" maxlength="4" required />
        <input v-model="newStation.name" placeholder="Ad" required />
        <input v-model="newStation.country" placeholder="Ülke" required />
        <button type="submit">Ekle</button>
      </form>
      <ul>
        <li v-for="s in stations" :key="s.code">
          {{ s.code }} - {{ s.name }} ({{ s.country }})
          <button @click="removeStation(s.code)">Sil</button>
        </li>
      </ul>
    </div>
    <div v-if="error" class="error-msg">{{ error }}</div>
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
.reference-data {
  width: 100%;
  box-sizing: border-box;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.reference-data > * {
  width: 100%;
  max-width: 700px;
  margin: 0 auto;
}
.ref-section {
  margin-bottom: 2rem;
}
form {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}
input, select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 0.5rem 1.2rem;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #1565c0;
}
ul {
  list-style: none;
  padding: 0;
}
li {
  margin-bottom: 0.5rem;
}
.error-msg {
  color: #d32f2f;
  font-size: 1.1em;
  margin-top: 1rem;
  text-align: center;
}
</style> 