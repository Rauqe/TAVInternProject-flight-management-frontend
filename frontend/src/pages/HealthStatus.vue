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
        <tr>
          <td>API Gateway</td>
          <td><span :class="statusClass('up')">Up</span></td>
        </tr>
        <tr>
          <td>Database (MySQL)</td>
          <td><span :class="statusClass('up')">Up</span></td>
        </tr>
        <tr>
          <td>Redis Cache</td>
          <td><span :class="statusClass('up')">Up</span></td>
        </tr>
        <tr>
          <td>Kafka</td>
          <td><span :class="statusClass('up')">Up</span></td>
        </tr>
        <tr>
          <td>Flight Archive Service</td>
          <td><span :class="statusClass('down')">Down</span></td>
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
function statusClass(status) {
  return status === 'up' ? 'status-up' : 'status-down';
}
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