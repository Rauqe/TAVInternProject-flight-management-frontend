import apiClient from './api';

export const getUsers = async () => {
  try {
    const response = await apiClient.get('/api/admin/users');
    return response.data;
  } catch (error) {
    console.error("Failed to fetch users:", error);
    throw error;
  }
};

export const createUser = async (userData) => {
  try {
    const response = await apiClient.post('/api/admin/users', userData);
    return response.data;
  } catch (error) {
    console.error("Failed to create user:", error);
    throw error;
  }
}; 