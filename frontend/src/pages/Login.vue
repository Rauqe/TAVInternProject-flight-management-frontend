<template>
  <div class="login-page">
    <h1>Giriş Yap</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Kullanıcı Adı</label>
        <input id="username" v-model="username" required autocomplete="username" />
      </div>
      <div class="form-group">
        <label for="password">Parola</label>
        <input id="password" type="password" v-model="password" required autocomplete="current-password" />
      </div>
      <button type="submit">Giriş</button>
      <div v-if="error" class="error-toast">{{ error }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../services/authService';

const username = ref('');
const password = ref('');
const error = ref('');
const router = useRouter();

async function handleLogin() {
  error.value = '';
  try {
    const token = await login(username.value, password.value);
    localStorage.setItem('jwt', token);
    router.push('/flights');
  } catch (e) {
    error.value = e.message;
  }
}
</script>

<style scoped>
.login-page {
  max-width: 400px;
  margin: 3rem auto;
  background: #fff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px #0001;
}
.form-group {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
}
button {
  padding: 0.75rem 2rem;
  font-size: 1rem;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #1565c0;
}
.error-toast {
  color: #721c24;
  background: #f8d7da;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  margin-top: 1rem;
}
</style> 