<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <div class="logo">✈️</div>
        <h1>FlightMS Portal</h1>
        <p>Welcome back! Please sign in to continue.</p>
      </div>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="username">Username</label>
          <input id="username" type="text" v-model="username" required autocomplete="username" placeholder="Enter your username" />
        </div>
        <div class="input-group">
          <label for="password">Password</label>
          <input id="password" type="password" v-model="password" required autocomplete="current-password" placeholder="Enter your password" />
        </div>
        <div v-if="error" class="error-message">
          {{ error }}
        </div>
        <button type="submit" class="login-button" :disabled="isLoading">
          {{ isLoading ? 'Signing In...' : 'Sign In' }}
        </button>
      </form>
       <div class="login-footer">
        <a href="#">Forgot Password?</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../services/authService';

const username = ref('admin'); // Default for easy testing
const password = ref('password'); // Default for easy testing
const error = ref('');
const isLoading = ref(false);
const router = useRouter();

async function handleLogin() {
  error.value = '';
  isLoading.value = true;
  try {
    await login(username.value, password.value);
    router.push({ name: 'dashboard' });
  } catch (e) {
    if (e.response && (e.response.status === 401 || e.response.status === 403)) {
      error.value = "Invalid username or password.";
    } else {
      error.value = "An unexpected error occurred. Please try again.";
    }
  } finally {
    isLoading.value = false;
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.login-box {
  width: 100%;
  max-width: 420px;
  background: white;
  padding: 3rem;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  text-align: center;
}

.login-header .logo {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.login-header h1 {
  font-size: 1.8rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}

.login-header p {
  color: #666;
  margin-bottom: 2rem;
}

.input-group {
  margin-bottom: 1.5rem;
  text-align: left;
}

.input-group label {
  display: block;
  font-size: 0.9rem;
  color: #555;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.input-group input {
  width: 100%;
  padding: 0.9rem 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  box-sizing: border-box; /* Important for padding and width calculation */
  transition: border-color 0.2s, box-shadow 0.2s;
}

.input-group input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.error-message {
  color: #dc2626;
  background-color: #fef2f2;
  border: 1px solid #dc2626;
  border-radius: 8px;
  padding: 0.8rem;
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
}

.login-button {
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 8px;
  background: linear-gradient(90deg, #1e3a8a, #3b82f6);
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
}

.login-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.login-footer {
  margin-top: 1.5rem;
  font-size: 0.9rem;
}

.login-footer a {
  color: #3b82f6;
  text-decoration: none;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style> 