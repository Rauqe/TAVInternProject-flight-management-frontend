<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Sidebar from './components/Sidebar.vue'
import { RouterView } from 'vue-router'

const route = useRoute();
const showSidebar = computed(() => route.name !== 'login');
</script>

<template>
  <div id="app">
    <Sidebar v-if="showSidebar" />
    <main :class="['content-wrapper', { 'content-wrapper--padded': showSidebar }]">
      <router-view />
    </main>
  </div>
</template>

<style>
/* --- GLOBAL RESET & BASE STYLES --- */

/* color palette from <https://github.com/vuejs/theme> */
:root {
  --vt-c-white: #ffffff;
  --vt-c-white-soft: #f8f8f8;
  --vt-c-white-mute: #f2f2f2;
  --vt-c-black: #181818;
  --vt-c-black-soft: #222222;
  --vt-c-black-mute: #282828;
  --vt-c-indigo: #2c3e50;
  --vt-c-divider-light-1: rgba(60, 60, 60, 0.29);
  --vt-c-divider-light-2: rgba(60, 60, 60, 0.12);
  --vt-c-divider-dark-1: rgba(84, 84, 84, 0.65);
  --vt-c-divider-dark-2: rgba(84, 84, 84, 0.48);
  --vt-c-text-light-1: var(--vt-c-indigo);
  --vt-c-text-light-2: rgba(60, 60, 60, 0.66);
  --vt-c-text-dark-1: var(--vt-c-white);
  --vt-c-text-dark-2: rgba(235, 235, 235, 0.64);
}

/* semantic color variables for this project */
:root {
  --color-background: var(--vt-c-white);
  --color-background-soft: var(--vt-c-white-soft);
  --color-background-mute: var(--vt-c-white-mute);
  --color-border: var(--vt-c-divider-light-2);
  --color-border-hover: var(--vt-c-divider-light-1);
  --color-heading: var(--vt-c-text-light-1);
  --color-text: var(--vt-c-text-light-1);
  --section-gap: 160px;
  --sidebar-width: 250px;
  --primary-color: #1976d2;
}

*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  font-weight: normal;
}

html, body {
  width: 100vw !important;
  min-height: 100vh !important;
  margin: 0 !important;
  padding: 0 !important;
  overflow-x: hidden !important; /* Prevent horizontal scroll */
}

body {
  background: var(--color-background);
  color: var(--color-text);
  font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* Modern Scrollbar Styling */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}
::-webkit-scrollbar-track {
  background: #f1f1f1;
}
::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}
::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* --- APP LAYOUT --- */
#app {
  display: flex;
  width: 100%;
  min-height: 100vh;
}

.content-wrapper {
  flex-grow: 1;
  overflow-y: auto;
  height: 100vh;
}

.content-wrapper--padded > * {
  width: 100%;
  max-width: 1400px; 
  margin: 0 auto;
  padding: 2rem; 
  box-sizing: border-box;
}

</style>
