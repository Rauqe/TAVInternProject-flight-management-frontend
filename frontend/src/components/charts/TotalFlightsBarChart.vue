<template>
  <div class="chart-container">
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

const props = defineProps({
  data: {
    type: Array,
    required: true
  }
});

const chartCanvas = ref(null);
let chart = null;

const createChart = () => {
  if (chart) chart.destroy();
  const ctx = chartCanvas.value.getContext('2d');
  const labels = props.data.map(item => item.airline);
  const flightCounts = props.data.map(item => item.count);
  chart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [
        {
          label: 'Total Flights',
          data: flightCounts,
          backgroundColor: '#3b82f6',
          borderColor: '#2563eb',
          borderWidth: 1
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        title: {
          display: true,
          text: 'Total Flights per Airline',
          font: { size: 16, weight: 'bold' },
          padding: { bottom: 20 }
        },
        legend: { display: false },
        tooltip: { enabled: true }
      },
      scales: {
        x: {
          title: { display: true, text: 'Airlines' }
        },
        y: {
          title: { display: true, text: 'Number of Flights' },
          beginAtZero: true
        }
      }
    }
  });
};

onMounted(createChart);
watch(() => props.data, createChart, { deep: true });
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 350px;
  width: 100%;
}
</style> 