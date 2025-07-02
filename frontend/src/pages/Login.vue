<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <el-icon style="font-size: 40px; color: #1976d2;"><User /></el-icon>
        <h1>FlightMS Portal</h1>
        <p>Welcome back! Please sign in to continue.</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleLogin" label-position="top">
        <el-form-item label="Username" prop="username">
          <el-input v-model="form.username" autocomplete="username" placeholder="Enter your username" />
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="form.password" type="password" autocomplete="current-password" placeholder="Enter your password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="isLoading" style="width: 100%">
            {{ isLoading ? 'Signing In...' : 'Sign In' }}
          </el-button>
        </el-form-item>
        <el-form-item v-if="error">
          <el-alert :title="error" type="error" show-icon />
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <el-link type="primary" @click="showForgotModal = true">Forgot Password?</el-link>
      </div>
    </el-card>
    <el-dialog v-model="showForgotModal" title="Forgot Password" width="350px" :close-on-click-modal="true">
      <el-form @submit.prevent="handleForgotPassword">
        <el-form-item label="Email">
          <el-input v-model="forgotEmail" type="email" placeholder="Enter your email" />
        </el-form-item>
        <el-form-item v-if="forgotMsg">
          <el-alert :title="forgotMsg" type="info" show-icon />
        </el-form-item>
        <div style="display: flex; justify-content: flex-end; gap: 8px;">
          <el-button @click="showForgotModal = false">Cancel</el-button>
          <el-button type="primary" native-type="submit">Send Reset Link</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../services/authService';
import { ElMessage, ElForm, ElFormItem, ElInput, ElButton, ElCard, ElAlert, ElLink, ElDialog, ElIcon } from 'element-plus';
import { User } from '@element-plus/icons-vue';

const form = ref({ username: '', password: '' });
const rules = {
  username: [ { required: true, message: 'Username is required', trigger: 'blur' } ],
  password: [ { required: true, message: 'Password is required', trigger: 'blur' } ]
};
const formRef = ref();
const error = ref('');
const isLoading = ref(false);
const router = useRouter();

const showForgotModal = ref(false);
const forgotEmail = ref('');
const forgotMsg = ref('');

async function handleLogin() {
  error.value = '';
  await formRef.value.validate();
  isLoading.value = true;
  try {
    await login(form.value.username, form.value.password);
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
}
.login-card {
  width: 100%;
  max-width: 420px;
  border-radius: 16px;
  text-align: center;
  box-shadow: 0 2px 16px 0 rgba(0,0,0,0.08);
  padding: 2.5rem 2rem 2rem 2rem;
}
.login-header {
  margin-bottom: 1.5rem;
}
.login-header h1 {
  font-size: 1.8rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}
.login-header p {
  color: #666;
  margin-bottom: 1.5rem;
}
.login-footer {
  margin-top: 1.5rem;
  font-size: 0.95rem;
}
</style>