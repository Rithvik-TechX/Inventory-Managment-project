import { api } from '../utilities/ApiUtils';
 
export const AuthService = {
  login:  (username, password) => api.post('/auth/login',  { username, password }),
  logout: ()                   => api.post('/auth/logout', {}),
  me:     ()                   => api.get('/auth/me'),
};