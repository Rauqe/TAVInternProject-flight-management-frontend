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
        <li v-for="(a, i) in airlines" :key="a.code">
          {{ a.code }} - {{ a.name }}
          <button @click="removeAirline(i)">Sil</button>
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
        <li v-for="(a, i) in aircraftTypes" :key="a.code">
          {{ a.code }} - {{ a.name }}
          <button @click="removeAircraftType(i)">Sil</button>
        </li>
      </ul>
    </div>
    <div class="ref-section">
      <h2>Rotalar (Route)</h2>
      <form @submit.prevent="addRoute">
        <input v-model="newRoute.origin" placeholder="Origin (örn: IST)" maxlength="4" required />
        <input v-model="newRoute.destination" placeholder="Destination (örn: ESB)" maxlength="4" required />
        <button type="submit">Ekle</button>
      </form>
      <ul>
        <li v-for="(r, i) in routes" :key="r.origin + '-' + r.destination">
          {{ r.origin }} → {{ r.destination }}
          <button @click="removeRoute(i)">Sil</button>
        </li>
      </ul>
    </div>
    <div class="ref-section">
      <h2>Uçuş Tipleri (Flight Type)</h2>
      <form @submit.prevent="addFlightType">
        <input v-model="newFlightType.code" placeholder="Kod (örn: PAX)" maxlength="8" required />
        <input v-model="newFlightType.name" placeholder="Ad" required />
        <button type="submit">Ekle</button>
      </form>
      <ul>
        <li v-for="(f, i) in flightTypes" :key="f.code">
          {{ f.code }} - {{ f.name }}
          <button @click="removeFlightType(i)">Sil</button>
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
        <li v-for="(s, i) in stations" :key="s.code">
          {{ s.code }} - {{ s.name }} ({{ s.country }})
          <button @click="removeStation(i)">Sil</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { getUserRole } from '../services/authService';
import { useRouter } from 'vue-router';

const router = useRouter();
const role = getUserRole();
if (role !== 'admin' && role !== 'operator') {
  router.push('/flights');
}

const airlines = ref([
  { code: 'TK', name: 'Turkish Airlines' },
  { code: 'PC', name: 'Pegasus' },
]);
const newAirline = ref({ code: '', name: '' });
function addAirline() {
  if (!newAirline.value.code || !newAirline.value.name) return;
  airlines.value.push({ ...newAirline.value });
  newAirline.value = { code: '', name: '' };
}
function removeAirline(i) {
  airlines.value.splice(i, 1);
}

const aircraftTypes = ref([
  { code: 'A320', name: 'Airbus A320' },
  { code: 'B738', name: 'Boeing 737-800' },
]);
const newAircraft = ref({ code: '', name: '' });
function addAircraftType() {
  if (!newAircraft.value.code || !newAircraft.value.name) return;
  aircraftTypes.value.push({ ...newAircraft.value });
  newAircraft.value = { code: '', name: '' };
}
function removeAircraftType(i) {
  aircraftTypes.value.splice(i, 1);
}

const routes = ref([
  { origin: 'IST', destination: 'ESB' },
  { origin: 'SAW', destination: 'ADB' },
]);
const newRoute = ref({ origin: '', destination: '' });
function addRoute() {
  if (!newRoute.value.origin || !newRoute.value.destination) return;
  routes.value.push({ ...newRoute.value });
  newRoute.value = { origin: '', destination: '' };
}
function removeRoute(i) {
  routes.value.splice(i, 1);
}

const flightTypes = ref([
  { code: 'PAX', name: 'Passenger' },
  { code: 'CRG', name: 'Cargo' },
  { code: 'POS', name: 'Position' },
]);
const newFlightType = ref({ code: '', name: '' });
function addFlightType() {
  if (!newFlightType.value.code || !newFlightType.value.name) return;
  flightTypes.value.push({ ...newFlightType.value });
  newFlightType.value = { code: '', name: '' };
}
function removeFlightType(i) {
  flightTypes.value.splice(i, 1);
}

const stations = ref([
  { code: 'IST', name: 'İstanbul Havalimanı', country: 'Türkiye' },
  { code: 'ESB', name: 'Esenboğa', country: 'Türkiye' },
]);
const newStation = ref({ code: '', name: '', country: '' });
function addStation() {
  if (!newStation.value.code || !newStation.value.name || !newStation.value.country) return;
  stations.value.push({ ...newStation.value });
  newStation.value = { code: '', name: '', country: '' };
}
function removeStation(i) {
  stations.value.splice(i, 1);
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
input {
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
</style> 