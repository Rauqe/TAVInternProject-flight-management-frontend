<template>
  <div class="health-status">
    <h1>System Health Status</h1>
    <table>
      <thead>
        <tr>
          <th>Service</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="service in services" :key="service.name">
          <td>{{ service.label }}</td>
          <td>
            <span :class="statusClass(service.status)">
              {{ service.status === 'up' ? 'Up' : 'Down' }}
            </span>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="legend">
      <span class="status-up">●</span> Up
      <span class="status-down">●</span> Down
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const services = ref([
  { name: 'api', label: 'API', status: 'down' },
  { name: 'database', label: 'Database (MySQL)', status: 'down' },
  { name: 'kafka', label: 'Kafka', status: 'down' },
]);

function statusClass(status) {
  return status === 'up' ? 'status-up' : 'status-down';
}

async function fetchHealth() {
  try {
    const res = await axios.get('http://localhost:8081/api/health');
    const data = res.data;
    services.value = [
      { name: 'api', label: 'API', status: data.api ? 'up' : 'down' },
      { name: 'database', label: 'Database (MySQL)', status: data.database ? 'up' : 'down' },
      { name: 'kafka', label: 'Kafka', status: data.kafka ? 'up' : 'down' },
    ];
  } catch (e) {
    services.value = [
      { name: 'api', label: 'API', status: 'down' },
      { name: 'database', label: 'Database (MySQL)', status: 'down' },
      { name: 'kafka', label: 'Kafka', status: 'down' },
    ];
  }
}

onMounted(fetchHealth);
</script>

<style scoped>
.health-status {
  width: 100%;
  box-sizing: border-box;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.health-status > * {
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1.5rem;
}
th, td {
  border: 1px solid #eee;
  padding: 0.5rem 1rem;
  text-align: left;
}
th {
  background: #f5f5f5;
}
.status-up {
  color: #388e3c;
  font-weight: bold;
}
.status-down {
  color: #d32f2f;
  font-weight: bold;
}
.legend {
  margin-top: 1rem;
  font-size: 1.1em;
}
.legend .status-up {
  margin-right: 1rem;
}
</style> 