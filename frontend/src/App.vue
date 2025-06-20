<script setup>
import Sidebar from './components/Sidebar.vue'
import { RouterView } from 'vue-router'
</script>

<template>
  <div id="app" class="app-container">
    <Sidebar />
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
  background: #f8fafc !important;
  color: #222 !important;
  opacity: 1 !important;
}

.app-container {
  display: flex;
  width: 100vw;
  min-height: 100vh;
  background: #f8fafc;
}

.main-content {
  flex: 1;
  min-width: 0;
  box-sizing: border-box;
  padding: 32px 24px;
  background: #f8fafc;
  width: 100%;
  max-width: none;
  margin: 0;
  display: flex;
  flex-direction: column;
}
</style>
