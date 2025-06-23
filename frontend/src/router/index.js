import { createRouter, createWebHistory } from 'vue-router'
import FlightCreate from '../pages/FlightCreate.vue'
import FlightList from '../pages/FlightList.vue'
import FlightBulkUpload from '../pages/FlightBulkUpload.vue'
import Login from '../pages/Login.vue'
import ReferenceData from '../pages/ReferenceData.vue'
import Reports from '../pages/Reports.vue'
import FlightArchive from '../pages/FlightArchive.vue'
import HealthStatus from '../pages/HealthStatus.vue'
import Dashboard from '../pages/Dashboard.vue'
import UserManagement from '../pages/UserManagement.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: Dashboard,
      meta: { requiresAuth: true }
    },
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/flights/create',
      name: 'flight-create',
      component: FlightCreate,
      meta: { requiresAuth: true }
    },
    {
      path: '/flights',
      name: 'flight-list',
      component: FlightList,
      meta: { requiresAuth: true }
    },
    {
      path: '/flights/bulk-upload',
      name: 'flight-bulk-upload',
      component: FlightBulkUpload,
      meta: { requiresAuth: true }
    },
    {
      path: '/reference-data',
      name: 'reference-data',
      component: ReferenceData,
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/reports',
      name: 'reports',
      component: Reports,
      meta: { requiresAuth: true }
    },
    {
      path: '/archive',
      name: 'archive',
      component: FlightArchive,
      meta: { requiresAuth: true }
    },
    {
      path: '/health',
      name: 'health',
      component: HealthStatus,
      meta: { requiresAuth: true }
    },
    {
      path: '/user-management',
      name: 'user-management',
      component: UserManagement,
      meta: { requiresAuth: true, requiresAdmin: true }
    },
  ],
})

router.beforeEach((to, from, next) => {
  const isLoggedIn = !!sessionStorage.getItem('user_role');
  const userRole = sessionStorage.getItem('user_role');

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isLoggedIn) {
      next({ name: 'login' });
    } else {
      if (to.matched.some(record => record.meta.requiresAdmin)) {
        if (userRole === 'ADMIN') {
          next();
        } else {
          next({ name: 'dashboard' }); // Or a dedicated 'unauthorized' page
        }
      } else {
        next();
      }
    }
  } else {
    if (isLoggedIn && to.name === 'login') {
      next({ name: 'dashboard' });
    } else {
      next();
    }
  }
});

export default router
