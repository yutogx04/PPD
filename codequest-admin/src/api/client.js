import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 10000,
});

// Add JWT token to every request if available
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default API;
