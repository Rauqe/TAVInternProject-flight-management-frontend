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
        <a href="#" @click.prevent="showForgotModal = true">Forgot Password?</a>
      </div>
    </div>
    <div v-if="showForgotModal" class="modal-overlay" @click.self="showForgotModal = false">
      <div class="modal-content">
        <button class="modal-close" @click="showForgotModal = false" aria-label="Close">&times;</button>
        <h2>Forgot Password</h2>
        <form @submit.prevent="handleForgotPassword">
          <div class="input-group">
            <label for="forgot-email">Email</label>
            <input id="forgot-email" type="email" v-model="forgotEmail" required placeholder="Enter your email" />
          </div>
          <div v-if="forgotMsg" class="info-message">{{ forgotMsg }}</div>
          <div class="modal-actions">
            <button type="button" @click="showForgotModal = false" class="btn-secondary">Cancel</button>
            <button type="submit" class="btn-primary">Send Reset Link</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../services/authService';

const username = ref('');
const password = ref('');
const error = ref('');
const isLoading = ref(false);
const router = useRouter();

const showForgotModal = ref(false);
const forgotEmail = ref('');
const forgotMsg = ref('');

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

function handleForgotPassword() {
  // Simulate sending reset link
  forgotMsg.value = `If an account with ${forgotEmail.value} exists, a reset link has been sent.`;
  setTimeout(() => {
    showForgotModal.value = false;
    forgotEmail.value = '';
    forgotMsg.value = '';
  }, 2500);
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
  transition: border-color 0.2s;
}

.input-group input:focus {
  outline: none;
  border-color: #3b82f6;
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

.login-button, .btn-primary, .btn-secondary {
  border: none;
  border-radius: 8px;
  padding: 1rem 1.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  background: #2563eb;
  color: #fff;
  transition: background 0.18s, color 0.18s;
}
.login-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.btn-secondary {
  background: #e5e7eb;
  color: #374151;
}
.btn-secondary:hover {
  background: #d1d5db;
}
.btn-primary:hover, .login-button:hover {
  background: #1e40af;
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.25);
  backdrop-filter: blur(3px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: rgba(255,255,255,0.95);
  padding: 2.5rem 2rem 2rem 2rem;
  border-radius: 18px;
  min-width: 340px;
  max-width: 95vw;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.modal-content h2 {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e3a8a;
  margin-bottom: 1.5rem;
  text-align: center;
  letter-spacing: 0.5px;
}
.input-group input {
  background: #f3f4f6;
  border: 1.5px solid #c7d2fe;
  border-radius: 10px;
  font-size: 1.05rem;
  padding: 1rem 1.1rem;
}
.input-group input:focus {
  border-color: #2563eb;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.7rem;
  margin-top: 1.2rem;
}
.btn-secondary, .btn-primary {
  font-size: 1rem;
  font-weight: 500;
  border-radius: 10px;
  padding: 0.7rem 1.5rem;
}
.btn-primary {
  background: linear-gradient(90deg, #2563eb, #3b82f6);
  color: #fff;
}
.modal-close {
  position: absolute;
  top: 1.1rem;
  right: 1.1rem;
  background: none;
  border: none;
  font-size: 1.6rem;
  color: #64748b;
  cursor: pointer;
  transition: color 0.18s;
}
.info-message {
  color: #2563eb;
  background: #e0e7ff;
  border: 1px solid #a5b4fc;
  border-radius: 8px;
  padding: 0.8rem;
  margin-bottom: 1rem;
  font-size: 0.95rem;
}
</style>