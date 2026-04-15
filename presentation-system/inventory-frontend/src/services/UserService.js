import { api } from '../utilities/ApiUtils';
 
export const UserService = {
  getAll:   ()             => api.get('/users'),
  getById:  (id)           => api.get(`/users/${id}`),
  create:   (user)         => api.post('/users', user),
  update:   (id, data)     => api.put(`/users/${id}`, data),
  delete:   (id)           => api.delete(`/users/${id}`),
  changePassword: (data)   => api.put('/users/password', data),
};