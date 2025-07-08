import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8081', // Your Spring Boot backend URL
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor to add the JWT token to every request
apiClient.interceptors.request.use(
  config => {
    const token = sessionStorage.getItem('jwt');
    console.log('Token from sessionStorage:', token);
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
      console.log('Authorization header set:', config.headers['Authorization']);
    } else {
      console.log('No token found in sessionStorage');
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

export default apiClient; 