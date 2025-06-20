<template>
  <div class="dashboard-pro">
    <div class="dashboard-header">
      <div>
     <h2>Welcome, <span class="user-role">Admin</span></h2>
      </div>
      <div class="dashboard-stats">
        <div class="stat-block">
          <div class="stat-label">Total Flights</div>
          <div class="stat-value">{{ totalFlights }}</div>
        </div>
        <div class="stat-block">
          <div class="stat-label">Delay Rate</div>
          <div class="stat-value">%{{ delayRate }}</div>
        </div>
        <div class="stat-block">
          <div class="stat-label">Cancel Rate</div>
          <div class="stat-value">%{{ cancelRate }}</div>
        </div>
      </div>
    </div>
    <div class="dashboard-quicklinks">
      <router-link to="/flights" class="quick-card">
        <div class="quick-icon">🛫</div>
        <div class="quick-title">Flight List</div>
        <div class="quick-desc">View and manage all flights</div>
      </router-link>
      <router-link to="/flights/create" class="quick-card">
        <div class="quick-icon">➕</div>
        <div class="quick-title">Add Flight</div>
        <div class="quick-desc">Create a new flight record</div>
      </router-link>
      <router-link to="/reports" class="quick-card">
        <div class="quick-icon">📊</div>
        <div class="quick-title">Reports</div>
        <div class="quick-desc">Operational and analytical reports</div>
      </router-link>
      <router-link to="/archive" class="quick-card">
        <div class="quick-icon">🗄️</div>
        <div class="quick-title">Flight Archive</div>
        <div class="quick-desc">Past flight records</div>
      </router-link>
    </div>
    <div class="dashboard-bottom">
      <div class="system-status">
        <h3>System Status</h3>
        <ul>
          <li><span class="status-dot up"></span> API Gateway: Up</li>
          <li><span class="status-dot up"></span> Database: Up</li>
          <li><span class="status-dot up"></span> Redis Cache: Up</li>
          <li><span class="status-dot up"></span> Kafka: Up</li>
          <li><span class="status-dot down"></span> Flight Archive: Down</li>
        </ul>
      </div>
      <div class="recent-activity">
        <h3>Recent Activity</h3>
        <ul>
          <li>Flight TK1234 added</li>
          <li>Flight PC5678 updated</li>
          <li>Route IST-ESB added to reference data</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getFlights } from '../services/flightService';
const flights = ref([]);
onMounted(async () => {
  flights.value = await getFlights();
});
const totalFlights = computed(() => flights.value.length);
const delayRate = computed(() =>
  flights.value.length
    ? Math.round(100 * flights.value.filter(f => f.delay && f.delay > 0).length / flights.value.length)
    : 0
);
const cancelRate = computed(() =>
  flights.value.length
    ? Math.round(100 * flights.value.filter(f => f.status === 'Cancelled').length / flights.value.length)
    : 0
);
</script>

<style scoped>
.dashboard-pro {
  width: 100%;
  box-sizing: border-box;
  padding: 2.5rem 2rem;
  display: flex;
  flex-direction: column;
}

.dashboard-header,
.dashboard-quicklinks,
.dashboard-bottom {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.dashboard-header {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 2rem;
}

.dashboard-header h2 {
  font-size: 2.1em;
  margin-bottom: 0.5rem;
}

.user-role {
  color: #1976d2;
  font-weight: 600;
}

.dashboard-stats {
  display: flex;
  gap: 2rem;
}

.stat-block {
  background: #f5f5f5;
  border-radius: 8px;
  padding: 1.2rem 2.2rem;
  text-align: center;
  min-width: 120px;
  box-shadow: 0 2px 8px #0001;
}

.stat-label {
  font-size: 1.05em;
  color: #1976d2;
  margin-bottom: 0.5rem;
}

.stat-value {
  font-size: 1.7em;
  font-weight: bold;
}

.dashboard-quicklinks {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 2rem;
  margin-bottom: 2.5rem;
  width: 100%;
}

.quick-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px #0001;
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  text-decoration: none;
  color: #222;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  transition: box-shadow 0.2s, background 0.2s;
  border: 1px solid #e3e8f0;
}

.quick-card:hover {
  background: #f0f6ff;
  box-shadow: 0 4px 16px #1976d233;
}

.quick-icon {
  font-size: 2.2em;
  margin-bottom: 0.7rem;
}

.quick-title {
  font-size: 1.15em;
  font-weight: 600;
  margin-bottom: 0.3rem;
}

.quick-desc {
  color: #555;
  font-size: 1em;
}

.dashboard-bottom {
  display: flex;
  gap: 2.5rem;
  margin-top: 2.5rem;
  flex-wrap: wrap;
  width: 100%;
}

.system-status, .recent-activity {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px #0001;
  padding: 1.5rem 2rem;
  flex: 1 1 320px;
  min-width: 320px;
  max-width: 100%;
  margin: 0 0 2rem 0;
}

.system-status h3, .recent-activity h3 {
  margin-bottom: 1rem;
  font-size: 1.15em;
  color: #1976d2;
}

.status-dot {
  display: inline-block;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 0.5rem;
}

.status-dot.up {
  background: #4caf50;
}

.status-dot.down {
  background: #d32f2f;
}
</style> 