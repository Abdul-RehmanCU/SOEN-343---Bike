import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  register: async (userData) => {
    const response = await api.post('/auth/register', userData);
    return response;
  },
  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials);
    return response;
  },
};

export const rideHistoryService = {
  getUserRideHistory: (userId, filters = {}) => {
    const params = new URLSearchParams();
    if (filters.startDate) params.append('startDate', filters.startDate);
    if (filters.endDate) params.append('endDate', filters.endDate);
    if (filters.status) params.append('status', filters.status);
    if (filters.bikeType) params.append('bikeType', filters.bikeType);
    if (filters.page) params.append('page', filters.page);
    if (filters.size) params.append('size', filters.size);
    
    return api.get(`/history/user/${userId}?${params.toString()}`);
  },
  getAllRideHistories: (filters = {}) => {
    const params = new URLSearchParams();
    if (filters.startDate) params.append('startDate', filters.startDate);
    if (filters.endDate) params.append('endDate', filters.endDate);
    if (filters.status) params.append('status', filters.status);
    if (filters.bikeType) params.append('bikeType', filters.bikeType);
    
    return api.get(`/history/all?${params.toString()}`);
  },
  getRideHistoryById: (id) => api.get(`/history/${id}`),
  getRideStatistics: (userId) => api.get(`/history/user/${userId}/statistics`),
};

export default api;