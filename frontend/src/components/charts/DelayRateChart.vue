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
  const delayRates = props.data.map(item => item.delayRate);
  chart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          label: 'Delay Rate (%)',
          data: delayRates,
          borderColor: '#dc2626',
          backgroundColor: 'rgba(220,38,38,0.15)',
          borderWidth: 4,
          pointBackgroundColor: '#dc2626',
          pointBorderColor: '#fff',
          pointRadius: 7,
          pointHoverRadius: 10,
          fill: false,
          tension: 0.3
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        title: {
          display: true,
          text: 'Delay Rate per Airline',
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
          title: { display: true, text: 'Delay Rate (%)' },
          beginAtZero: true,
          max: 100
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