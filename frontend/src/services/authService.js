import apiClient from './api';

export const login = async (username, password) => {
  try {
    const response = await apiClient.post('/api/v1/auth/authenticate', {
      username,
      password
    });
    
    // On successful login, store token and user role
    if (response.data && response.data.token) {
      sessionStorage.setItem('jwt', response.data.token);
      sessionStorage.setItem('user_role', response.data.role);
    }
    
    return response.data;
  } catch (error) {
    // Re-throw the error to be caught by the calling component (e.g., Login.vue)
    console.error("Login failed:", error.response ? error.response.data : error.message);
    throw error;
  }
};

export const logout = () => {
  sessionStorage.removeItem('jwt');
  sessionStorage.removeItem('user_role');
  // The router push is handled in Sidebar.vue, so no need to do it here.
};

export function getUserRole() {
  const token = localStorage.getItem('jwt');
  if (token === 'mock-jwt-token-admin') return 'admin';
  if (token === 'mock-jwt-token-operator') return 'operator';
  if (token === 'mock-jwt-token-viewer') return 'viewer';
  return null;
} 