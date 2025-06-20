<template>
  <div class="upload-container">
    <h2>Bulk Flight Upload (CSV)</h2>
    <input type="file" accept=".csv" @change="handleFileChange" />
    <p>Sample CSV format:</p>
    <pre>flightNumber,airline,aircraftType,origin,destination,date,std,sta,flightType,delay,status
TK1234,TK,A320,IST,ESB,2024-06-05,10:00,11:15,Passenger,30,Scheduled</pre>
    <div v-if="flights.length">
      <h2>Preview</h2>
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
          </tr>
        </thead>
        <tbody>
          <tr v-for="(flight, i) in flights" :key="i" :class="{ 'row-error': Object.keys(rowErrors[i]||{}).length }">
            <td>{{ flight.flightNumber }}</td>
            <td>{{ flight.airline }}</td>
            <td>{{ flight.aircraftType }}</td>
            <td>{{ flight.origin }}</td>
            <td>{{ flight.destination }}</td>
            <td>{{ flight.date }}</td>
            <td>{{ flight.std }}</td>
            <td>{{ flight.sta }}</td>
            <td>{{ flight.flightType }}</td>
            <td>{{ flight.delay }}</td>
            <td>{{ flight.status }}</td>
          </tr>
        </tbody>
      </table>
      <button @click="uploadFlights">Upload</button>
      <div v-if="success" class="success-toast">Bulk upload successful!</div>
      <div v-if="error" class="error-toast">{{ error }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { addFlight } from '../services/flightService';

const flights = ref([]);
const success = ref(false);
const error = ref('');
const rowErrors = ref([]);

function handleFileChange(e) {
  const file = e.target.files[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = (evt) => {
    const text = evt.target.result;
    parseCSV(text);
  };
  reader.readAsText(file);
}

function parseCSV(text) {
  const lines = text.trim().split('\n');
  const headers = lines[0].split(',');
  flights.value = lines.slice(1).map(line => {
    const values = line.split(',');
    const obj = {};
    headers.forEach((h, i) => {
      obj[h.trim()] = values[i]?.trim() || '';
    });
    return obj;
  });
}

function validateRow(row) {
  const errs = {};
  if (!row.flightNumber || !row.flightNumber.match(/^[A-Z]{2}\d{3,4}$/)) errs.flightNumber = 'Flight Number formatı hatalı';
  if (!row.airline) errs.airline = 'Havayolu eksik';
  if (!row.aircraftType) errs.aircraftType = 'Uçak tipi eksik';
  if (!row.origin) errs.origin = 'Origin eksik';
  if (!row.destination) errs.destination = 'Destination eksik';
  if (!row.date) errs.date = 'Tarih eksik';
  if (!row.std) errs.std = 'STD eksik';
  if (!row.sta) errs.sta = 'STA eksik';
  if (!row.flightType) errs.flightType = 'Uçuş tipi eksik';
  if (row.sta && row.std && row.sta < row.std) errs.sta = 'STA, STD saatinden önce olamaz';
  return errs;
}

function validateAllRows() {
  rowErrors.value = flights.value.map(validateRow);
}

watch(flights, validateAllRows, { deep: true });

async function uploadFlights() {
  error.value = '';
  success.value = false;
  try {
    const validFlights = flights.value.filter(flight => Object.keys(rowErrors.value[flights.value.indexOf(flight)] || {}).length === 0);
    for (const flight of validFlights) {
      await addFlight(flight);
    }
    success.value = true;
    flights.value = [];
  } catch (e) {
    error.value = e.message;
  }
}
</script>

<style scoped>
.upload-container {
  width: 100%;
  box-sizing: border-box;
  padding: 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.upload-container > * {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
}

.upload-container h2,
.upload-container label,
.upload-container p,
.upload-container pre {
  color: #222;
  opacity: 1;
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
.success-toast {
  color: #155724;
  background: #d4edda;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  margin-top: 1rem;
}
.error-toast {
  color: #721c24;
  background: #f8d7da;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  margin-top: 1rem;
}
button {
  margin-top: 1rem;
  padding: 0.75rem 2rem;
  font-size: 1rem;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #1565c0;
}
.row-error {
  background-color: #ffdddd;
}
</style> 