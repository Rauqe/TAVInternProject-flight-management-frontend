<script setup>
import Sidebar from './components/Sidebar.vue'
import { RouterView } from 'vue-router'
</script>

<template>
  <div id="app">
    <Sidebar />
    <main class="content-wrapper">
      <router-view />
    </main>
  </div>
</template>

<style>
@import './assets/base.css';

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

#app {
  display: flex;
  height: 100vh; /* Full viewport height */
  overflow: hidden; /* Prevent body scroll */
  border: 3px solid blue;
}

.content-wrapper {
  flex-grow: 1;
  overflow-y: auto; /* Make only the content area scrollable */
  height: 100vh;
  display: flex;
  justify-content: center; /* Force horizontal centering */
}

.content-wrapper > * {
  width: 100%;
  max-width: 1400px;
  padding: 2rem;
  box-sizing: border-box;
}

/* Other global styles */
:root {
  --sidebar-width: 250px;
  --primary-color: #1976d2;
}

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
  background: #f8fafc !important;
  color: #222 !important;
  opacity: 1 !important;
}
</style>
