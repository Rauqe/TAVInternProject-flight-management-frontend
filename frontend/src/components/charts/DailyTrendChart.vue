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
  if (chart) {
    chart.destroy();
  }

  const ctx = chartCanvas.value.getContext('2d');
  const labels = props.data.map(item => item.date);
  const totalFlights = props.data.map(item => item.totalFlights);
  const delayedFlights = props.data.map(item => item.delayedFlights);

  chart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          label: 'Total Flights',
          data: totalFlights,
          borderColor: '#3b82f6',
          backgroundColor: 'rgba(59,130,246,0.1)',
          fill: true,
          tension: 0.3
        },
        {
          label: 'Delayed Flights',
          data: delayedFlights,
          borderColor: '#f59e0b',
          backgroundColor: 'rgba(245,158,11,0.1)',
          fill: true,
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
          text: 'Daily Flight & Delay Trend',
          font: {
            size: 16,
            weight: 'bold'
          },
          padding: {
            bottom: 20
          }
        },
        legend: {
          position: 'top',
        }
      },
      scales: {
        x: {
          display: true,
          title: {
            display: true,
            text: 'Date'
          }
        },
        y: {
          display: true,
          title: {
            display: true,
            text: 'Flights'
          }
        }
      }
    }
  });
};

onMounted(() => {
  createChart();
});

watch(() => props.data, () => {
  createChart();
}, { deep: true });
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 350px;
  width: 100%;
}
</style> 