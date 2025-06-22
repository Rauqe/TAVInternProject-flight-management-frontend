<template>
  <div class="sidebar">
    <div class="sidebar-header">
      <span class="sidebar-title">FlightMS</span>
    </div>
    <ul class="sidebar-list">
      <li v-for="item in mainItems" :key="item.path" :class="{active: $route.path === item.path}">
        <RouterLink :to="item.path">
          <span class="icon" v-html="item.icon"></span>
          <span class="label">{{ item.label }}</span>
        </RouterLink>
      </li>
    </ul>
    <div class="sidebar-group">Management</div>
    <ul class="sidebar-list">
      <li v-for="item in adminItems" :key="item.path" :class="{active: $route.path === item.path}">
        <RouterLink :to="item.path">
          <span class="icon" v-html="item.icon"></span>
          <span class="label">{{ item.label }}</span>
        </RouterLink>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router';
const $route = useRoute();
const mainItems = [
  { path: '/', label: 'Dashboard', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><rect x='3' y='8' width='4' height='9' rx='1'/><rect x='9' y='3' width='4' height='14' rx='1'/><rect x='15' y='12' width='4' height='5' rx='1'/></svg>` },
  { path: '/flights', label: 'Flight List', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><path d='M2 16l16-4-7-7-4 16z'/></svg>` },
  { path: '/flights/create', label: 'Add Flight', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><circle cx='10' cy='10' r='8'/><path d='M10 6v8M6 10h8'/></svg>` },
  { path: '/flights/bulk-upload', label: 'Bulk Upload', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><path d='M4 4h12v12H4z'/><path d='M8 8h4v4H8z'/></svg>` },
  { path: '/archive', label: 'Flight Archive', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><rect x='3' y='7' width='14' height='10' rx='2'/><path d='M7 7V5a3 3 0 0 1 6 0v2'/></svg>` },
  { path: '/reports', label: 'Reports', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><rect x='3' y='3' width='14' height='14' rx='2'/><path d='M7 13V7M10 13V10M13 13V4'/></svg>` },
  { path: '/health', label: 'Health Status', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><path d='M2 10h4l2 6 4-12 2 6h4'/></svg>` },
];
const adminItems = [
  { path: '/reference-data', label: 'Reference Data', icon: `<svg width='20' height='20' fill='none' stroke='currentColor'><rect x='4' y='4' width='12' height='12' rx='2'/><path d='M8 8h4v4H8z'/></svg>` },
];
</script>

<style scoped>
.sidebar {
  width: var(--sidebar-width, 250px);
  height: 100vh;
  background: #2c3e50;
  color: #ecf0f1;
  display: flex;
  flex-direction: column;
  padding: 1rem 0;
  flex-shrink: 0; /* Prevent sidebar from shrinking */
  box-shadow: 2px 0 8px #0002;
  z-index: 10;
  font-family: 'Segoe UI', 'Roboto', Arial, sans-serif;
}
.sidebar-header {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 2.2rem 2rem 1.2rem 2rem;
  border-bottom: 1px solid #22305a;
}
.sidebar-logo {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  background: #fff;
}
.sidebar-title {
  font-size: 1.25em;
  font-weight: bold;
  letter-spacing: 1px;
}
.sidebar-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.sidebar-list li {
  margin: 0;
}
.sidebar-list li.active, .sidebar-list li:hover {
  background: #1976d2;
}
.sidebar-list a {
  display: flex;
  align-items: center;
  color: inherit;
  text-decoration: none;
  padding: 1.1rem 2rem;
  font-size: 1.08em;
  transition: background 0.2s;
}
.icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 1rem;
}
.label {
  font-weight: 500;
}
.sidebar-group {
  margin: 2.2rem 2rem 0.5rem 2rem;
  font-size: 0.95em;
  color: #8fa2c7;
  letter-spacing: 0.5px;
  font-weight: 600;
  text-transform: uppercase;
}
@media (max-width: 900px) {
  .sidebar {
    width: 64px;
    min-width: 64px;
  }
  .sidebar-header, .sidebar-group {
    display: none;
  }
  .sidebar-list a {
    padding: 1.1rem 1rem;
    justify-content: center;
  }
  .label {
    display: none;
  }
}
</style> 