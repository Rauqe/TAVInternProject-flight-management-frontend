<template>
  <div class="flight-create">
    <el-card class="form-card">
      <template #header>
        <div class="card-header">
          <h2>Add New Flight</h2>
        </div>
      </template>
      
      <el-form 
        ref="formRef"
        :model="form" 
        :rules="rules"
        label-width="140px"
        @submit.prevent="handleSubmit"
      >
        <el-row :gutter="20">
          <!-- Flight Number -->
          <el-col :span="12">
            <el-form-item label="Flight Number" prop="flightNumber">
              <el-input 
                v-model="form.flightNumber" 
                placeholder="e.g. TK1234"
                maxlength="6"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          
          <!-- Airline -->
          <el-col :span="12">
            <el-form-item label="Airline" prop="airline">
              <el-select 
                v-model="form.airline" 
                placeholder="Select Airline"
                style="width: 100%"
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
        </el-row>

        <el-row :gutter="20">
          <!-- Aircraft Type -->
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
          
          <!-- Flight Date -->
          <el-col :span="12">
            <el-form-item label="Flight Date" prop="date">
              <el-date-picker
                v-model="form.date"
                type="date"
                placeholder="Select Date"
                :disabled-date="disabledDate"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <!-- Origin -->
          <el-col :span="12">
            <el-form-item label="Origin (ICAO)" prop="origin">
              <el-input 
                v-model="form.origin" 
                placeholder="e.g. IST"
                maxlength="4"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          
          <!-- Destination -->
          <el-col :span="12">
            <el-form-item label="Destination (ICAO)" prop="destination">
              <el-input 
                v-model="form.destination" 
                placeholder="e.g. JFK"
                maxlength="4"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <!-- STD -->
          <el-col :span="12">
            <el-form-item label="STD" prop="std">
              <el-time-picker
                v-model="form.std"
                placeholder="Select Time"
                format="HH:mm"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          
          <!-- STA -->
          <el-col :span="12">
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
          <!-- Flight Type -->
          <el-col :span="12">
            <el-form-item label="Flight Type" prop="flightType">
              <el-radio-group v-model="form.flightType">
                <el-radio label="Passenger">Passenger</el-radio>
                <el-radio label="Cargo">Cargo</el-radio>
                <el-radio label="Position">Position</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          
          <!-- Delay -->
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

        <el-row :gutter="20">
          <!-- Status -->
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
        </el-row>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            <el-icon><Plus /></el-icon>
            Save Flight
          </el-button>
          <el-button @click="resetForm">
            <el-icon><Refresh /></el-icon>
            Reset
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Success Message -->
    <el-dialog
      v-model="success"
      title="Success"
      width="30%"
      :show-close="false"
    >
      <span>
        <el-icon color="#67C23A" size="24"><CircleCheckFilled /></el-icon>
        Flight saved successfully!
      </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="success = false">
            OK
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus, Refresh, CircleCheckFilled } from '@element-plus/icons-vue';
import { addFlight, getAirlines, getAircraftTypes } from '../services/flightService';

const formRef = ref();
const loading = ref(false);

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
const success = ref(false);

// Validation rules
const rules = ref({
  flightNumber: [
    { required: true, message: 'Flight number is required', trigger: 'blur' },
    { 
      pattern: /^[A-Z]{2}\d{3,4}$/, 
      message: 'Invalid flight number format (e.g. TK1234)', 
      trigger: 'blur' 
    }
  ],
  airline: [
    { required: true, message: 'Airline is required', trigger: 'change' }
  ],
  aircraftType: [
    { required: true, message: 'Aircraft type is required', trigger: 'change' }
  ],
  origin: [
    { required: true, message: 'Origin is required', trigger: 'blur' },
    { min: 3, max: 4, message: 'ICAO code must be 3-4 characters', trigger: 'blur' }
  ],
  destination: [
    { required: true, message: 'Destination is required', trigger: 'blur' },
    { min: 3, max: 4, message: 'ICAO code must be 3-4 characters', trigger: 'blur' }
  ],
  date: [
    { required: true, message: 'Flight date is required', trigger: 'change' }
  ],
  std: [
    { required: true, message: 'STD is required', trigger: 'change' }
  ],
  sta: [
    { required: true, message: 'STA is required', trigger: 'change' }
  ],
  flightType: [
    { required: true, message: 'Flight type is required', trigger: 'change' }
  ]
});

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7; // Disable past dates
};

onMounted(async () => {
  try {
    airlines.value = await getAirlines();
    aircraftTypes.value = await getAircraftTypes();
  } catch (error) {
    ElMessage.error('Failed to load reference data');
  }
});

// Custom validation for STA vs STD
watch([() => form.value.sta, () => form.value.std], ([sta, std]) => {
  if (sta && std && sta < std) {
    ElMessage.warning('STA cannot be before STD');
  }
});

async function handleSubmit() {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    
    // Additional validation for STA vs STD
    if (form.value.sta && form.value.std && form.value.sta < form.value.std) {
      ElMessage.error('STA cannot be before STD');
      return;
    }
    
    loading.value = true;
    await addFlight(form.value);
    success.value = true;
    resetForm();
    ElMessage.success('Flight saved successfully!');
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message);
    } else {
      ElMessage.error('Failed to save flight');
    }
  } finally {
    loading.value = false;
  }
}

function resetForm() {
  if (formRef.value) {
    formRef.value.resetFields();
  }
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
    delay: 0,
    status: 'Scheduled',
  };
}
</script>

<style scoped>
.flight-create {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.form-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

.el-form {
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .el-col {
    margin-bottom: 10px;
  }
  
  .flight-create {
    padding: 10px;
  }
}
</style> 