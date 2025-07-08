<template>
  <div class="flight-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>Flight List</h2>
          <el-button type="primary" @click="openCreateDialog">
            Create Flight
          </el-button>
        </div>
      </template>

      <!-- Filters -->
      <div class="filters-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select v-model="filters.airline" placeholder="All Airlines" clearable style="width: 100%">
              <el-option 
                v-for="airline in airlines" 
                :key="airline.code" 
                :label="airline.name" 
                :value="airline.code" 
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filters.origin" placeholder="All Origins" clearable style="width: 100%">
              <el-option 
                v-for="station in stations" 
                :key="station.code" 
                :label="`${station.name} (${station.code})`" 
                :value="station.code" 
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filters.destination" placeholder="All Destinations" clearable style="width: 100%">
              <el-option 
                v-for="station in stations" 
                :key="station.code" 
                :label="`${station.name} (${station.code})`" 
                :value="station.code" 
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filters.flightType" placeholder="All Types" clearable style="width: 100%">
              <el-option 
                v-for="flightType in flightTypes" 
                :key="flightType.code" 
                :label="flightType.name" 
                :value="flightType.code" 
              />
            </el-select>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 15px;">
          <el-col :span="12">
            <el-input 
              v-model="filters.search" 
              placeholder="Search flight number..." 
              clearable
              style="width: 100%"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </el-col>
        </el-row>
      </div>

      <!-- Flight Table -->
      <el-table 
        :data="paginatedFlights" 
        style="width: 100%" 
        :row-class-name="getRowClassName"
        v-loading="loading"
      >
        <el-table-column prop="flightNumber" label="Flight Number" width="120" />
        <el-table-column prop="airlineName" label="Airline" width="80">
          <template #default="scope">
            <div class="airline-logo">
              <img :src="airlineLogos[scope.row.airlineCode]" :alt="scope.row.airlineName" />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="aircraftTypeName" label="Aircraft Type" width="120" />
        <el-table-column prop="originName" label="Origin" width="100" />
        <el-table-column prop="destinationName" label="Destination" width="100" />
        <el-table-column prop="flightDate" label="Date" width="100" />
        <el-table-column prop="std" label="STD" width="80" />
        <el-table-column prop="sta" label="STA" width="80" />
        <el-table-column label="Flight Type" width="100">
          <template #default="scope">
            <el-tag :type="getFlightTypeTagType(scope.row.flightType)">
              {{ getFlightTypeAbbreviation(scope.row.flightType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Delay (min)" width="100">
          <template #default="scope">
            <span :class="{ 'delay-text': scope.row.delay > 0 }">
              {{ scope.row.delay || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Status" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'Scheduled' ? 'success' : 'danger'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="150" fixed="right">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="openEditDialog(scope.row)"
            >
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleDelete(scope.row.id)"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- Pagination -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredFlights.length"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- Create/Edit Drawer -->
    <el-drawer
      v-model="drawerVisible"
      direction="rtl"
      :size="drawerSize"
      :before-close="handleDrawerClose"
      :show-close="false"
    >
      <template #header>
        <div class="drawer-header">
          <span class="drawer-title">{{ isEditMode ? 'Edit Flight' : 'Create Flight' }}</span>
        </div>
      </template>
      <div class="drawer-content">
        <el-form 
          ref="formRef"
          :model="form" 
          :rules="rules"
          label-width="120px"
          class="drawer-form"
        >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Airline" prop="airline">
              <el-select 
                v-model="form.airline" 
                placeholder="Select Airline"
                style="width: 100%"
                @change="onAirlineChange"
              >
                <el-option 
                  v-for="airline in airlines" 
                  :key="airline.code" 
                  :label="airline.name" 
                  :value="airline.code" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Flight Number" prop="flightNumber">
              <el-input 
                v-model="form.flightNumber"
                :placeholder="form.airline ? `(ex. ${form.airline}1234)` : ''"
                :disabled="!form.airline"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Aircraft Type" prop="aircraftType">
              <el-select 
                v-model="form.aircraftType" 
                placeholder="Select Aircraft Type"
                style="width: 100%"
              >
                <el-option 
                  v-for="aircraft in aircraftTypes" 
                  :key="aircraft.code" 
                  :label="aircraft.name" 
                  :value="aircraft.code" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Flight Type" prop="flightType">
              <el-select 
                v-model="form.flightType" 
                placeholder="Select Flight Type"
                style="width: 100%"
              >
                <el-option 
                  v-for="flightType in flightTypes" 
                  :key="flightType.code" 
                  :label="flightType.name" 
                  :value="flightType.code" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Origin" prop="origin">
              <el-select 
                v-model="form.origin" 
                placeholder="Select Origin"
                style="width: 100%"
              >
                <el-option 
                  v-for="station in stations" 
                  :key="station.code" 
                  :label="station.name" 
                  :value="station.code" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Destination" prop="destination">
              <el-select 
                v-model="form.destination" 
                placeholder="Select Destination"
                style="width: 100%"
              >
                <el-option 
                  v-for="station in stations" 
                  :key="station.code" 
                  :label="station.name" 
                  :value="station.code" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Date" prop="date">
              <el-date-picker
                v-model="form.date"
                type="date"
                placeholder="Select Date"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="STD" prop="std">
              <el-time-picker
                v-model="form.std"
                placeholder="Select Time"
                format="HH:mm"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="STA" prop="sta">
              <el-time-picker
                v-model="form.sta"
                placeholder="Select Time"
                format="HH:mm"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Status" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="Select Status"
                style="width: 100%"
              >
                <el-option label="Scheduled" value="Scheduled" />
                <el-option label="Cancelled" value="Cancelled" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Delay (min)" prop="delay">
              <el-input-number
                v-model="form.delay"
                :min="0"
                :max="1440"
                placeholder="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        </el-form>
      </div>

      <template #footer>
        <div style="flex: auto">
          <el-button @click="drawerVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ isEditMode ? 'Update' : 'Create' }}
          </el-button>
        </div>
      </template>
    </el-drawer>

    <!-- Delete Confirmation Dialog -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="Confirm Delete"
      width="30%"
    >
      <span>Are you sure you want to delete this flight?</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">Cancel</el-button>
          <el-button type="danger" @click="confirmDelete" :loading="deleteLoading">
            Delete
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Edit, Delete, Search } from '@element-plus/icons-vue';
import { getFlights, updateFlight, deleteFlight, getAirlines, getStations, getFlightTypes, getAircraftTypes, addFlight } from '../services/flightService';
import { connectFlightWebSocket, disconnectFlightWebSocket } from '../services/websocketService';
import { airlineLogos } from '../assets/airline-logos';

const flights = ref([]);
const airlines = ref([]);
const stations = ref([]);
const flightTypes = ref([]);
const aircraftTypes = ref([]);
const loading = ref(false);
const submitLoading = ref(false);
const deleteLoading = ref(false);

// Drawer and Dialog states
const drawerVisible = ref(false);
const deleteDialogVisible = ref(false);
const isEditMode = ref(false);
const formRef = ref();
const flightToDelete = ref(null);
const flightToEdit = ref(null);

// Pagination
const currentPage = ref(1);
const pageSize = ref(20);

// Responsive drawer size
const drawerSize = computed(() => {
  return window.innerWidth < 768 ? '100%' : '50%';
});

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
  status: 'Scheduled',
  delay: 0
});

const filters = ref({
  airline: '',
  origin: '',
  destination: '',
  flightType: '',
  search: '',
});

// Watch filters to reset pagination
watch(filters, () => {
  currentPage.value = 1;
}, { deep: true });

// Validation rules
const rules = ref({
  airline: [
    { required: true, message: 'Airline is required', trigger: 'change' }
  ],
  flightNumber: [
    { required: true, message: 'Flight number is required', trigger: 'blur' }
  ],
  aircraftType: [
    { required: true, message: 'Aircraft type is required', trigger: 'change' }
  ],
  origin: [
    { required: true, message: 'Origin is required', trigger: 'change' }
  ],
  destination: [
    { required: true, message: 'Destination is required', trigger: 'change' }
  ],
  date: [
    { required: true, message: 'Date is required', trigger: 'change' }
  ],
  std: [
    { required: true, message: 'STD is required', trigger: 'change' }
  ],
  sta: [
    { required: true, message: 'STA is required', trigger: 'change' }
  ],
  flightType: [
    { required: true, message: 'Flight type is required', trigger: 'change' }
  ],
  status: [
    { required: true, message: 'Status is required', trigger: 'change' }
  ]
});



async function fetchData() {
  loading.value = true;
  try {
    [flights.value, airlines.value, stations.value, flightTypes.value, aircraftTypes.value] = await Promise.all([
      getFlights(),
      getAirlines(),
      getStations(),
      getFlightTypes(),
      getAircraftTypes()
    ]);
  } catch (e) {
    ElMessage.error("Failed to fetch data: " + e.message);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  fetchData();
  connectFlightWebSocket((flightList) => {
    flights.value = flightList;
  });
});

onUnmounted(() => {
  disconnectFlightWebSocket();
});

const filteredFlights = computed(() => {
  if (!flights.value) return [];
  return flights.value.filter(f => {
    // FlightType filter - backend'de name var, filter'da code var
    let flightTypeMatch = true;
    if (filters.value.flightType) {
      const selectedFlightType = flightTypes.value.find(ft => ft.code === filters.value.flightType);
      flightTypeMatch = selectedFlightType && f.flightType === selectedFlightType.name;
    }
    
    return (
      (!filters.value.airline || f.airlineCode === filters.value.airline) &&
      (!filters.value.origin || f.originCode === filters.value.origin) &&
      (!filters.value.destination || f.destinationCode === filters.value.destination) &&
      flightTypeMatch &&
      (!filters.value.search || f.flightNumber.toLowerCase().includes(filters.value.search.toLowerCase()))
    );
  });
});

const paginatedFlights = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredFlights.value.slice(start, end);
});

function getRowClassName({ row }) {
  return row.status === 'Cancelled' ? 'cancelled-row' : '';
}

function getFlightTypeAbbreviation(type) {
  const abbreviations = {
    'Passenger': 'PAX',
    'Cargo': 'CAR',
    'Position': 'POS'
  };
  return abbreviations[type] || type;
}

function getFlightTypeTagType(type) {
  const types = {
    'Passenger': 'primary',
    'Cargo': 'warning',
    'Position': 'info'
  };
  return types[type] || 'default';
}

function onAirlineChange() {
  if (form.value.airline && !form.value.flightNumber) {
    form.value.flightNumber = form.value.airline;
  }
}

function openCreateDialog() {
  isEditMode.value = false;
  resetForm();
  drawerVisible.value = true;
}

function handleSizeChange(newSize) {
  pageSize.value = newSize;
  currentPage.value = 1;
}

function handleCurrentChange(newPage) {
  currentPage.value = newPage;
}

function openEditDialog(flight) {
  isEditMode.value = true;
  flightToEdit.value = flight;
  form.value = {
    flightNumber: flight.flightNumber,
    airline: flight.airline?.code,
    aircraftType: flight.aircraftType?.code,
    origin: flight.origin?.code,
    destination: flight.destination?.code,
    date: flight.flightDate,
    std: flight.std,
    sta: flight.sta,
    flightType: flight.flightType,
    status: flight.status,
    delay: flight.delay || 0
  };
  drawerVisible.value = true;
}

function resetForm() {
  form.value = {
    flightNumber: '',
    airline: '',
    aircraftType: '',
    origin: '',
    destination: '',
    date: '',
    std: '',
    sta: '',
    flightType: '',
    status: 'Scheduled',
    delay: 0
  };
  if (formRef.value) {
    formRef.value.resetFields();
  }
}

async function handleSubmit() {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    submitLoading.value = true;
    
    if (isEditMode.value) {
      await updateFlight(flightToDelete.value.id, form.value);
      ElMessage.success('Flight updated successfully!');
    } else {
      await addFlight(form.value);
      ElMessage.success('Flight created successfully!');
    }
    
    drawerVisible.value = false;
    await fetchData();
  } catch (error) {
    ElMessage.error(error.message || 'Failed to save flight');
  } finally {
    submitLoading.value = false;
  }
}

function handleDelete(flightId) {
  flightToDelete.value = flightId;
  deleteDialogVisible.value = true;
}

async function confirmDelete() {
  try {
    deleteLoading.value = true;
    await deleteFlight(flightToDelete.value);
    ElMessage.success('Flight deleted successfully!');
    deleteDialogVisible.value = false;
    await fetchData();
  } catch (error) {
    ElMessage.error(error.message || 'Failed to delete flight');
  } finally {
    deleteLoading.value = false;
  }
}

function handleDrawerClose(done) {
  done();
}
</script>

<style scoped>
.flight-list-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.filters-section {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.delay-text {
  color: #e6a23c;
  font-weight: bold;
}

.cancelled-row {
  background-color: #fef0f0 !important;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 20px 0;
}

.drawer-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.drawer-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.drawer-content {
  padding: 20px;
  height: calc(100% - 120px);
  overflow-y: auto;
}

.drawer-form {
  height: 100%;
}

  .drawer-form .el-form-item {
    margin-bottom: 18px;
  }

/* Airline logo styles */
.airline-logo {
  display: flex;
  align-items: center;
  justify-content: center;
}

.airline-logo img {
  width: 80px;
  height: 40px;
  object-fit: contain;
  display: block;
  margin: 0 auto;
}

@media (max-width: 768px) {
  .flight-list-container {
    padding: 10px;
  }
  
  .el-col {
    margin-bottom: 10px;
  }
  
  .drawer-content {
    padding: 15px;
  }
  
  .airline-logo img {
    width: 35px;
    height: 25px;
  }
  

}
</style> 