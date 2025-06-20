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

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/flights/create',
      name: 'flight-create',
      component: FlightCreate,
    },
    {
      path: '/flights',
      name: 'flight-list',
      component: FlightList,
    },
    {
      path: '/flights/bulk-upload',
      name: 'flight-bulk-upload',
      component: FlightBulkUpload,
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/reference-data',
      name: 'reference-data',
      component: ReferenceData,
    },
    {
      path: '/reports',
      name: 'reports',
      component: Reports,
    },
    {
      path: '/archive',
      name: 'archive',
      component: FlightArchive,
    },
    {
      path: '/health',
      name: 'health',
      component: HealthStatus,
    },
    {
      path: '/',
      name: 'dashboard',
      component: Dashboard,
    },
  ],
})

export default router
