<template>
  <div class="chart-container">
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { Chart, registerables } from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';

Chart.register(...registerables, ChartDataLabels);

const props = defineProps({
  data: {
    type: Object,
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
  const values = [
    props.data.onTime || 0,
    props.data.delayed || 0,
    props.data.cancelled || 0
  ];
  const total = values.reduce((a, b) => a + b, 0) || 1;

  chart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['On Time', 'Delayed', 'Cancelled'],
      datasets: [{
        data: values,
        backgroundColor: [
          '#10b981', // Green for on time
          '#f59e0b', // Yellow for delayed
          '#ef4444'  // Red for cancelled
        ],
        borderWidth: 2,
        borderColor: '#ffffff'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            padding: 20,
            usePointStyle: true,
            font: {
              size: 12
            }
          }
        },
        title: {
          display: true,
          text: 'Flight Status Distribution',
          font: {
            size: 16,
            weight: 'bold'
          },
          padding: {
            bottom: 20
          }
        },
        datalabels: {
          color: '#fff',
          font: {
            weight: 'bold',
            size: 14
          },
          formatter: (value, context) => {
            const percent = Math.round((value / total) * 100);
            return percent > 0 ? percent + '%' : '';
          }
        }
      }
    },
    plugins: [ChartDataLabels]
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
  height: 300px;
  width: 100%;
}
</style> 