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
  
  const labels = props.data.map(item => item.airline);
  const flightCounts = props.data.map(item => item.count);
  const delayRates = props.data.map(item => item.delayRate);
  
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
          borderWidth: 1,
          yAxisID: 'y',
          order: 1
        },
        {
          label: 'Delay Rate (%)',
          data: delayRates,
          backgroundColor: 'rgba(220,38,38,0.15)',
          borderColor: '#dc2626',
          borderWidth: 4,
          pointBackgroundColor: '#dc2626',
          pointBorderColor: '#fff',
          pointRadius: 6,
          pointHoverRadius: 8,
          fill: false,
          yAxisID: 'y1',
          type: 'line',
          order: 2
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      interaction: {
        mode: 'index',
        intersect: false,
      },
      plugins: {
        title: {
          display: true,
          text: 'Airline Performance Overview',
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
        },
        tooltip: {
          enabled: true,
          callbacks: {
            label: function(context) {
              if (context.dataset.label === 'Total Flights') {
                return `Total Flights: ${context.parsed.y}`;
              } else if (context.dataset.label === 'Delay Rate (%)') {
                return `Delay Rate: ${context.parsed.y}%`;
              }
              return context.parsed.y;
            }
          }
        }
      },
      scales: {
        x: {
          display: true,
          title: {
            display: true,
            text: 'Airlines'
          }
        },
        y: {
          type: 'linear',
          display: true,
          position: 'left',
          title: {
            display: true,
            text: 'Number of Flights'
          }
        },
        y1: {
          type: 'linear',
          display: true,
          position: 'right',
          title: {
            display: true,
            text: 'Delay Rate (%)'
          },
          grid: {
            drawOnChartArea: false,
          },
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
  height: 400px;
  width: 100%;
}
</style> 