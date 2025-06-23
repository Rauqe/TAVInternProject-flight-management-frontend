<template>
  <div class="sidebar">
    <div class="sidebar-header">
      <span class="sidebar-title">FlightMS</span>
    </div>
    
    <nav class="sidebar-nav">
      <ul class="nav-list">
        <li v-for="item in menuItems" :key="item.path" :class="{ active: $route.path === item.path }">
          <RouterLink :to="item.path" class="nav-link">
            <span class="nav-icon" v-html="item.icon"></span>
            <span class="nav-label">{{ item.text }}</span>
          </RouterLink>
        </li>
      </ul>
    </nav>

    <div class="sidebar-footer">
      <button @click="logout" class="logout-btn">
        <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
        </svg>
        <span>Logout</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const $route = useRoute();

const userRole = computed(() => sessionStorage.getItem('user_role'));
const isAdmin = computed(() => userRole.value === 'ADMIN');

const menuItems = computed(() => {
  const baseItems = [
    {
      text: 'Dashboard',
      path: '/',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 5a2 2 0 012-2h4a2 2 0 012 2v6H8V5z"></path></svg>'
    },
    {
      text: 'Flight List',
      path: '/flights',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>'
    },
    {
      text: 'Create Flight',
      path: '/flights/create',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path></svg>'
    },
    {
      text: 'Reports',
      path: '/reports',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path></svg>'
    },
    {
      text: 'Archive',
      path: '/archive',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4"></path></svg>'
    }
  ];

  // Admin-only items
  if (isAdmin.value) {
    baseItems.splice(3, 0, {
      text: 'Bulk Upload',
      path: '/flights/bulk-upload',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path></svg>'
    });
    baseItems.splice(4, 0, {
      text: 'Reference Data',
      path: '/reference-data',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>'
    });
    baseItems.splice(5, 0, {
      text: 'User Management',
      path: '/user-management',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path></svg>'
    });
    baseItems.splice(6, 0, {
      text: 'Health Status',
      path: '/health',
      icon: '<svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>'
    });
  }

  return baseItems;
});

function logout() {
  sessionStorage.removeItem('user_role');
  sessionStorage.removeItem('jwt');
  router.push('/login');
}
</script>

<style scoped>
.sidebar {
  width: 250px;
  height: 100vh;
  background: #1e293b;
  color: #e2e8f0;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #334155;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid #334155;
}

.sidebar-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #f8fafc;
}

.sidebar-nav {
  flex: 1;
  padding: 1rem 0;
}

.nav-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-list li {
  margin: 0;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  color: #cbd5e1;
  text-decoration: none;
  transition: all 0.2s ease;
  border-left: 3px solid transparent;
}

.nav-link:hover {
  background: #334155;
  color: #f8fafc;
}

.nav-list li.active .nav-link {
  background: #3b82f6;
  color: #ffffff;
  border-left-color: #60a5fa;
}

.nav-icon {
  width: 20px;
  height: 20px;
  margin-right: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-label {
  font-weight: 500;
  font-size: 0.875rem;
}

.sidebar-footer {
  padding: 1rem 1.5rem;
  border-top: 1px solid #334155;
}

.logout-btn {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 0.75rem;
  background: none;
  border: none;
  color: #cbd5e1;
  cursor: pointer;
  border-radius: 0.375rem;
  transition: all 0.2s ease;
  font-size: 0.875rem;
  font-weight: 500;
}

.logout-btn:hover {
  background: #dc2626;
  color: #ffffff;
}

.logout-btn svg {
  margin-right: 0.75rem;
}

@media (max-width: 768px) {
  .sidebar {
    width: 64px;
  }
  
  .sidebar-header {
    padding: 1rem 0.5rem;
  }
  
  .sidebar-title {
    display: none;
  }
  
  .nav-link {
    padding: 0.75rem;
    justify-content: center;
  }
  
  .nav-icon {
    margin-right: 0;
  }
  
  .nav-label {
    display: none;
  }
  
  .logout-btn {
    padding: 0.75rem;
    justify-content: center;
  }
  
  .logout-btn span {
    display: none;
  }
}
</style> 