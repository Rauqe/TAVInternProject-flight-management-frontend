<template>
  <div class="flight-create">
    <h1>Add Flight</h1>
    <form @submit.prevent="handleSubmit" @keydown.ctrl.s.prevent="handleSubmit" tabindex="0">
      <div class="form-grid">
        <div class="form-group">
          <label for="flightNumber">Flight Number</label>
          <input id="flightNumber" v-model="form.flightNumber" required aria-label="Flight Number" tabindex="1" />
          <div v-if="errors.flightNumber" class="error-msg">{{ errors.flightNumber }}</div>
        </div>
        <div class="form-group">
          <label for="airline">Airline</label>
          <select id="airline" v-model="form.airline" required aria-label="Airline">
            <option value="">Select</option>
            <option v-for="a in airlines" :key="a.code" :value="a.code">{{ a.name }}</option>
          </select>
          <div v-if="errors.airline" class="error-msg">{{ errors.airline }}</div>
        </div>
        <div class="form-group">
          <label for="aircraftType">Aircraft Type</label>
          <select id="aircraftType" v-model="form.aircraftType" required aria-label="Aircraft Type">
            <option value="">Select</option>
            <option v-for="a in aircraftTypes" :key="a.code" :value="a.code">{{ a.name }}</option>
          </select>
          <div v-if="errors.aircraftType" class="error-msg">{{ errors.aircraftType }}</div>
        </div>
        <div class="form-group">
          <label for="origin">Origin (ICAO)</label>
          <input id="origin" v-model="form.origin" maxlength="4" required aria-label="Origin" tabindex="4" />
          <div v-if="errors.origin" class="error-msg">{{ errors.origin }}</div>
        </div>
        <div class="form-group">
          <label for="destination">Destination (ICAO)</label>
          <input id="destination" v-model="form.destination" maxlength="4" required aria-label="Destination" tabindex="5" />
          <div v-if="errors.destination" class="error-msg">{{ errors.destination }}</div>
        </div>
        <div class="form-group">
          <label for="date">Flight Date</label>
          <input id="date" type="date" v-model="form.date" required aria-label="Flight Date" :min="today" tabindex="6" />
          <div v-if="errors.date" class="error-msg">{{ errors.date }}</div>
        </div>
        <div class="form-group">
          <label for="std">STD</label>
          <input id="std" type="time" v-model="form.std" required aria-label="STD" tabindex="7" />
          <div v-if="errors.std" class="error-msg">{{ errors.std }}</div>
        </div>
        <div class="form-group">
          <label for="sta">STA</label>
          <input id="sta" type="time" v-model="form.sta" required aria-label="STA" tabindex="8" />
          <div v-if="errors.sta" class="error-msg">{{ errors.sta }}</div>
        </div>
        <div class="form-group">
          <label>Flight Type</label>
          <div>
            <label><input type="radio" value="Passenger" v-model="form.flightType" required /> Passenger</label>
            <label><input type="radio" value="Cargo" v-model="form.flightType" /> Cargo</label>
            <label><input type="radio" value="Position" v-model="form.flightType" /> Position</label>
          </div>
          <div v-if="errors.flightType" class="error-msg">{{ errors.flightType }}</div>
        </div>
        <div class="form-group">
          <label for="delay">Delay (min)</label>
          <input id="delay" type="number" v-model.number="form.delay" min="0" aria-label="Delay (min)" tabindex="9" />
        </div>
        <div class="form-group">
          <label for="status">Status</label>
          <select id="status" v-model="form.status" aria-label="Status" tabindex="10">
            <option value="Scheduled">Scheduled</option>
            <option value="Cancelled">Cancelled</option>
          </select>
        </div>
      </div>
      <button type="submit">Save</button>
      <div v-if="success" class="success-toast">Flight saved successfully!</div>
      <div v-if="error" class="error-toast" aria-live="assertive">{{ error }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { addFlight, getAirlines, getAircraftTypes } from '../services/flightService';

const form = ref({
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

const airlines = ref([]);
const aircraftTypes = ref([]);

const today = computed(() => new Date().toISOString().split('T')[0]);
const success = ref(false);
const error = ref('');
const errors = ref({});

onMounted(async () => {
  airlines.value = await getAirlines();
  aircraftTypes.value = await getAircraftTypes();
});

function validate() {
  errors.value = {};
  if (!form.value.flightNumber.match(/^[A-Z]{2}\d{3,4}$/)) {
    errors.value.flightNumber = 'Invalid flight number format (e.g. TK1234)';
  }
  if (!form.value.airline) errors.value.airline = 'Airline is required';
  if (!form.value.aircraftType) errors.value.aircraftType = 'Aircraft type is required';
  if (!form.value.origin) errors.value.origin = 'Origin is required';
  if (!form.value.destination) errors.value.destination = 'Destination is required';
  if (!form.value.date) errors.value.date = 'Date is required';
  if (!form.value.std) errors.value.std = 'STD is required';
  if (!form.value.sta) errors.value.sta = 'STA is required';
  if (!form.value.flightType) errors.value.flightType = 'Flight type is required';
  if (form.value.sta && form.value.std && form.value.sta < form.value.std) {
    errors.value.sta = 'STA cannot be before STD';
  }
}

watch(form, validate, { deep: true });

async function handleSubmit() {
  error.value = '';
  success.value = false;
  try {
    validate();
    if (Object.keys(errors.value).length > 0) {
      error.value = 'There are errors in the form. Please fill all fields correctly.';
      return;
    }
    await addFlight(form.value);
    success.value = true;
    Object.keys(form.value).forEach(k => form.value[k] = '');
  } catch (e) {
    error.value = e.message;
  }
}
</script>

<style scoped>
.flight-create {
  width: 100%;
  box-sizing: border-box;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.flight-create > form {
  width: 100%;
  max-width: 700px;
  margin: 0 auto;
}
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
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
button[type="submit"] {
  margin-top: 1rem;
  padding: 0.75rem 2rem;
  font-size: 1rem;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button[type="submit"]:hover {
  background: #1565c0;
}
.error-msg {
  color: #d32f2f;
  font-size: 0.95em;
  margin-top: 0.2rem;
}
</style> 