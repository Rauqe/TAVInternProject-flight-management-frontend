<template>
  <div class="bulk-upload-container">
    <!-- Header Section -->
    <div class="header-section">
      <div class="header-content">
        <h1 class="page-title">
          <i class="fas fa-cloud-upload-alt"></i>
          Bulk Flight Upload
        </h1>
        <p class="page-description">
          Upload multiple flights at once using CSV format. Drag and drop your file or click to browse.
        </p>
      </div>
    </div>

    <!-- Upload Area -->
    <div class="upload-section">
      <div 
        class="upload-area"
        :class="{ 
          'drag-over': isDragOver, 
          'has-file': selectedFile,
          'uploading': isUploading 
        }"
        @drop="handleDrop"
        @dragover.prevent="isDragOver = true"
        @dragleave.prevent="isDragOver = false"
        @click="triggerFileInput"
      >
        <div class="upload-content">
          <div class="upload-icon">
            <i v-if="!selectedFile" class="fas fa-cloud-upload-alt"></i>
            <i v-else-if="isUploading" class="fas fa-spinner fa-spin"></i>
            <i v-else class="fas fa-file-csv"></i>
          </div>
          
          <div class="upload-text">
            <h3 v-if="!selectedFile">Drop your CSV file here</h3>
            <h3 v-else-if="isUploading">Uploading flights...</h3>
            <h3 v-else>{{ selectedFile?.name }}</h3>
            
            <p v-if="!selectedFile">
              or <span class="browse-link">browse files</span>
            </p>
            <p v-else-if="!isUploading">
              {{ flights.length }} flights ready to upload
            </p>
          </div>

          <input 
            ref="fileInput"
            type="file" 
            accept=".csv" 
            @change="handleFileChange" 
            style="display: none;"
          />
        </div>
      </div>

      <!-- Progress Bar -->
      <div v-if="isUploading" class="progress-section">
        <div class="progress-info">
          <span>Uploading flights...</span>
          <span>{{ uploadedCount }}/{{ totalFlights }}</span>
        </div>
        <div class="progress-bar">
          <div 
            class="progress-fill" 
            :style="{ width: `${(uploadedCount / totalFlights) * 100}%` }"
          ></div>
        </div>
      </div>
    </div>

    <!-- Sample Format -->
    <div class="sample-section">
      <div class="sample-header">
        <h3><i class="fas fa-info-circle"></i> Sample CSV Format</h3>
        <button class="download-sample" @click="downloadSample">
          <i class="fas fa-download"></i> Download Sample
        </button>
      </div>
      <div class="sample-content">
        <pre>flightNumber,airline,aircraftType,origin,destination,date,std,sta,flightType,delay,status
TK1234,TK,A320,IST,ESB,2024-06-05,10:00,11:15,Passenger,30,Scheduled
LH1234,LH,B737,MUC,IST,2024-06-05,14:30,17:45,Passenger,0,On Time</pre>
      </div>
    </div>

    <!-- Preview Table -->
    <div v-if="flights.length && !isUploading" class="preview-section">
      <div class="preview-header">
        <h3><i class="fas fa-table"></i> Flight Preview ({{ flights.length }} flights)</h3>
        <div class="preview-actions">
          <button 
            class="btn-secondary" 
            @click="clearFile"
            :disabled="isUploading"
          >
            <i class="fas fa-times"></i> Clear
          </button>
          <button 
            class="btn-primary" 
            @click="uploadFlights"
            :disabled="isUploading || hasErrors"
          >
            <i class="fas fa-upload"></i> Upload {{ validFlightsCount }} Flights
          </button>
        </div>
      </div>

      <!-- Validation Summary -->
      <div v-if="hasErrors" class="validation-summary">
        <div class="validation-header">
          <i class="fas fa-exclamation-triangle"></i>
          <span>{{ errorCount }} flights have validation errors</span>
        </div>
        <div class="validation-details">
          <span>{{ validFlightsCount }} valid flights will be uploaded</span>
          <span>{{ errorCount }} flights will be skipped</span>
        </div>
      </div>

      <div class="table-container">
        <table class="preview-table">
          <thead>
            <tr>
              <th>Flight Number</th>
              <th>Airline</th>
              <th>Aircraft Type</th>
              <th>Origin</th>
              <th>Destination</th>
              <th>Date</th>
              <th>STD</th>
              <th>STA</th>
              <th>Flight Type</th>
              <th>Delay (min)</th>
              <th>Status</th>
              <th>Validation</th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="(flight, i) in flights" 
              :key="i" 
              :class="{ 
                'row-error': hasRowError(i),
                'row-valid': !hasRowError(i)
              }"
            >
              <td>{{ flight.flightNumber }}</td>
              <td>{{ flight.airline }}</td>
              <td>{{ flight.aircraftType }}</td>
              <td>{{ flight.origin }}</td>
              <td>{{ flight.destination }}</td>
              <td>{{ flight.date }}</td>
              <td>{{ flight.std }}</td>
              <td>{{ flight.sta }}</td>
              <td>{{ flight.flightType }}</td>
              <td>{{ flight.delay }}</td>
              <td>
                <span class="status-badge" :class="getStatusClass(flight.status)">
                  {{ flight.status }}
                </span>
              </td>
              <td>
                <div v-if="hasRowError(i)" class="error-indicator">
                  <i class="fas fa-exclamation-circle"></i>
                  <div class="error-tooltip">
                    <div v-for="(error, field) in rowErrors[i]" :key="field" class="error-item">
                      <strong>{{ field }}:</strong> {{ error }}
                    </div>
                  </div>
                </div>
                <div v-else class="valid-indicator">
                  <i class="fas fa-check-circle"></i>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Success/Error Messages -->
    <div v-if="success" class="message success-message">
      <i class="fas fa-check-circle"></i>
      <div class="message-content">
        <h4>Upload Successful!</h4>
        <p>{{ uploadedCount }} flights have been uploaded successfully.</p>
      </div>
      <button class="message-close" @click="clearMessage">
        <i class="fas fa-times"></i>
      </button>
    </div>

    <div v-if="error" class="message error-message">
      <i class="fas fa-exclamation-circle"></i>
      <div class="message-content">
        <h4>Upload Failed</h4>
        <p>{{ error }}</p>
      </div>
      <button class="message-close" @click="clearMessage">
        <i class="fas fa-times"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { addFlight } from '../services/flightService';

const flights = ref([]);
const selectedFile = ref(null);
const success = ref(false);
const error = ref('');
const rowErrors = ref([]);
const isDragOver = ref(false);
const isUploading = ref(false);
const uploadedCount = ref(0);
const totalFlights = ref(0);
const fileInput = ref(null);

// Computed properties
const validFlightsCount = computed(() => {
  return flights.value.filter((_, i) => !hasRowError(i)).length;
});

const errorCount = computed(() => {
  return flights.value.filter((_, i) => hasRowError(i)).length;
});

const hasErrors = computed(() => {
  return errorCount.value > 0;
});

// Helper functions
function hasRowError(index) {
  return Object.keys(rowErrors.value[index] || {}).length > 0;
}

function getStatusClass(status) {
  const statusMap = {
    'Scheduled': 'status-scheduled',
    'On Time': 'status-ontime',
    'Delayed': 'status-delayed',
    'Cancelled': 'status-cancelled',
    'Completed': 'status-completed'
  };
  return statusMap[status] || 'status-default';
}

function triggerFileInput() {
  if (!selectedFile.value && !isUploading.value) {
    fileInput.value?.click();
  }
}

function handleDrop(e) {
  e.preventDefault();
  isDragOver.value = false;
  
  const files = e.dataTransfer.files;
  if (files.length > 0) {
    handleFile(files[0]);
  }
}

function handleFileChange(e) {
  const file = e.target.files[0];
  if (file) {
    handleFile(file);
  }
}

function handleFile(file) {
  if (!file.name.toLowerCase().endsWith('.csv')) {
    error.value = 'Please select a CSV file';
    return;
  }
  
  selectedFile.value = file;
  error.value = '';
  success.value = false;
  
  const reader = new FileReader();
  reader.onload = (evt) => {
    const text = evt.target.result;
    parseCSV(text);
  };
  reader.readAsText(file);
}

function parseCSV(text) {
  try {
    const lines = text.trim().split('\n');
    if (lines.length < 2) {
      error.value = 'CSV file must contain at least a header row and one data row';
      return;
    }
    
    const headers = lines[0].split(',').map(h => h.trim());
    flights.value = lines.slice(1).map(line => {
      const values = line.split(',');
      const obj = {};
      headers.forEach((h, i) => {
        obj[h] = values[i]?.trim() || '';
      });
      return obj;
    });
    
    validateAllRows();
  } catch (e) {
    error.value = 'Error parsing CSV file: ' + e.message;
  }
}

function validateRow(row) {
  const errs = {};
  
  if (!row.flightNumber || !row.flightNumber.match(/^[A-Z]{2}\d{3,4}$/)) {
    errs.flightNumber = 'Invalid flight number format (e.g., TK1234)';
  }
  if (!row.airline) errs.airline = 'Airline is required';
  if (!row.aircraftType) errs.aircraftType = 'Aircraft type is required';
  if (!row.origin) errs.origin = 'Origin is required';
  if (!row.destination) errs.destination = 'Destination is required';
  if (!row.date) errs.date = 'Date is required';
  if (!row.std) errs.std = 'STD is required';
  if (!row.sta) errs.sta = 'STA is required';
  if (!row.flightType) errs.flightType = 'Flight type is required';
  
  // Validate time format
  const timeRegex = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;
  if (row.std && !timeRegex.test(row.std)) {
    errs.std = 'Invalid time format (HH:MM)';
  }
  if (row.sta && !timeRegex.test(row.sta)) {
    errs.sta = 'Invalid time format (HH:MM)';
  }
  
  // Validate date format
  const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
  if (row.date && !dateRegex.test(row.date)) {
    errs.date = 'Invalid date format (YYYY-MM-DD)';
  }
  
  // Validate STA > STD
  if (row.sta && row.std && row.sta <= row.std) {
    errs.sta = 'STA must be after STD';
  }
  
  return errs;
}

function validateAllRows() {
  rowErrors.value = flights.value.map(validateRow);
}

function clearFile() {
  selectedFile.value = null;
  flights.value = [];
  rowErrors.value = [];
  error.value = '';
  success.value = false;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
}

function clearMessage() {
  success.value = false;
  error.value = '';
}

function downloadSample() {
  const sampleData = `flightNumber,airline,aircraftType,origin,destination,date,std,sta,flightType,delay,status
TK1234,TK,A320,IST,ESB,2024-06-05,10:00,11:15,Passenger,30,Scheduled
LH1234,LH,B737,MUC,IST,2024-06-05,14:30,17:45,Passenger,0,On Time
BA5678,BA,A380,LHR,IST,2024-06-05,09:15,15:30,Passenger,45,Delayed`;
  
  const blob = new Blob([sampleData], { type: 'text/csv' });
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'flight_sample.csv';
  a.click();
  window.URL.revokeObjectURL(url);
}

async function uploadFlights() {
  error.value = '';
  success.value = false;
  isUploading.value = true;
  
  const validFlights = flights.value.filter((_, i) => !hasRowError(i));
  totalFlights.value = validFlights.length;
  uploadedCount.value = 0;
  
  try {
    for (const flight of validFlights) {
      await addFlight(flight);
      uploadedCount.value++;
    }
    
    success.value = true;
    clearFile();
  } catch (e) {
    error.value = e.message || 'Upload failed';
  } finally {
    isUploading.value = false;
  }
}

watch(flights, validateAllRows, { deep: true });
</script>

<style scoped>
.bulk-upload-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* Header Section */
.header-section {
  text-align: center;
  margin-bottom: 32px;
  padding: 32px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  color: white;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.page-title i {
  font-size: 2rem;
}

.page-description {
  font-size: 1.1rem;
  opacity: 0.9;
  margin: 0;
  max-width: 600px;
  margin: 0 auto;
}

/* Upload Section */
.upload-section {
  margin-bottom: 32px;
}

.upload-area {
  border: 3px dashed #e0e0e0;
  border-radius: 16px;
  padding: 48px 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fafafa;
  position: relative;
  overflow: hidden;
}

.upload-area:hover {
  border-color: #667eea;
  background: #f8f9ff;
}

.upload-area.drag-over {
  border-color: #667eea;
  background: #f0f4ff;
  transform: scale(1.02);
}

.upload-area.has-file {
  border-color: #4caf50;
  background: #f1f8e9;
}

.upload-area.uploading {
  border-color: #ff9800;
  background: #fff3e0;
  cursor: not-allowed;
}

.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.upload-icon {
  font-size: 4rem;
  color: #667eea;
  margin-bottom: 16px;
}

.upload-area.has-file .upload-icon {
  color: #4caf50;
}

.upload-area.uploading .upload-icon {
  color: #ff9800;
}

.upload-text h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #333;
}

.upload-text p {
  font-size: 1rem;
  color: #666;
  margin: 0;
}

.browse-link {
  color: #667eea;
  text-decoration: underline;
  cursor: pointer;
}

/* Progress Section */
.progress-section {
  margin-top: 24px;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.progress-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-weight: 500;
  color: #333;
}

.progress-bar {
  height: 8px;
  background: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4caf50, #8bc34a);
  transition: width 0.3s ease;
}

/* Sample Section */
.sample-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 32px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.sample-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.sample-header h3 {
  margin: 0;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.download-sample {
  background: #667eea;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background 0.3s ease;
}

.download-sample:hover {
  background: #5a6fd8;
}

.sample-content {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
}

.sample-content pre {
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
  color: #333;
  line-height: 1.4;
}

/* Preview Section */
.preview-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

.preview-header h3 {
  margin: 0;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.preview-actions {
  display: flex;
  gap: 12px;
}

.btn-primary, .btn-secondary {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #4caf50, #8bc34a);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
}

.btn-primary:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn-secondary {
  background: #f5f5f5;
  color: #666;
  border: 1px solid #ddd;
}

.btn-secondary:hover:not(:disabled) {
  background: #e0e0e0;
}

/* Validation Summary */
.validation-summary {
  background: #fff3cd;
  border: 1px solid #ffeaa7;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 24px;
}

.validation-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #856404;
  margin-bottom: 8px;
}

.validation-details {
  display: flex;
  gap: 24px;
  font-size: 0.9rem;
  color: #856404;
}

/* Table Styles */
.table-container {
  overflow-x: auto;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.preview-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.preview-table th {
  background: #f8f9fa;
  padding: 12px 8px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 2px solid #e0e0e0;
  white-space: nowrap;
}

.preview-table td {
  padding: 12px 8px;
  border-bottom: 1px solid #f0f0f0;
  white-space: nowrap;
}

.preview-table tr.row-error {
  background: #fff5f5;
}

.preview-table tr.row-valid {
  background: #f8fff8;
}

.preview-table tr:hover {
  background: #f8f9ff;
}

/* Status Badges */
.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
  text-transform: uppercase;
}

.status-scheduled {
  background: #e3f2fd;
  color: #1976d2;
}

.status-ontime {
  background: #e8f5e8;
  color: #2e7d32;
}

.status-delayed {
  background: #fff3e0;
  color: #f57c00;
}

.status-cancelled {
  background: #ffebee;
  color: #d32f2f;
}

.status-completed {
  background: #f3e5f5;
  color: #7b1fa2;
}

.status-default {
  background: #f5f5f5;
  color: #666;
}

/* Error/Valid Indicators */
.error-indicator, .valid-indicator {
  position: relative;
  display: inline-block;
}

.error-indicator i {
  color: #f44336;
  cursor: help;
}

.valid-indicator i {
  color: #4caf50;
}

.error-tooltip {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: #333;
  color: white;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 0.8rem;
  white-space: nowrap;
  z-index: 1000;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  margin-bottom: 8px;
}

.error-tooltip::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border: 6px solid transparent;
  border-top-color: #333;
}

.error-indicator:hover .error-tooltip {
  opacity: 1;
  visibility: visible;
}

.error-item {
  margin-bottom: 4px;
}

.error-item:last-child {
  margin-bottom: 0;
}

/* Messages */
.message {
  position: fixed;
  top: 24px;
  right: 24px;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  display: flex;
  align-items: flex-start;
  gap: 16px;
  max-width: 400px;
  z-index: 1000;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.success-message {
  border-left: 4px solid #4caf50;
}

.error-message {
  border-left: 4px solid #f44336;
}

.message i {
  font-size: 1.5rem;
  margin-top: 2px;
}

.success-message i {
  color: #4caf50;
}

.error-message i {
  color: #f44336;
}

.message-content h4 {
  margin: 0 0 8px 0;
  font-size: 1.1rem;
  font-weight: 600;
}

.message-content p {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
}

.message-close {
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.message-close:hover {
  background: #f5f5f5;
  color: #666;
}

/* Responsive Design */
@media (max-width: 768px) {
  .bulk-upload-container {
    padding: 16px;
  }
  
  .page-title {
    font-size: 2rem;
  }
  
  .upload-area {
    padding: 32px 16px;
  }
  
  .preview-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .preview-actions {
    justify-content: center;
  }
  
  .validation-details {
    flex-direction: column;
    gap: 8px;
  }
  
  .message {
    left: 16px;
    right: 16px;
    max-width: none;
  }
}
</style> 