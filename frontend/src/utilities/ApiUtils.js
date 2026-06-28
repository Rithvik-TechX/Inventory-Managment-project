import { API_BASE_URL, TOKEN_KEY, USER_KEY } from './Constants';
 
const getToken = () => localStorage.getItem(TOKEN_KEY);
 
const headers = (extra = {}) => ({
  'Content-Type': 'application/json',
  ...(getToken() ? { Authorization: `Bearer ${getToken()}` } : {}),
  ...extra,
});
 
const handleResponse = async (res) => {
  // If token is expired or invalid, clear storage and redirect to login
  if ((res.status === 401 || res.status === 403) && getToken()) {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    window.location.href = '/login';
    throw new Error('Session expired. Please login again.');
  }
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || data.message || `HTTP ${res.status}`);
  return data;
};
 
export const api = {
  get: (path) =>
    fetch(`${API_BASE_URL}${path}`, { headers: headers() }).then(handleResponse),
 
  post: (path, body) =>
    fetch(`${API_BASE_URL}${path}`, {
      method: 'POST',
      headers: headers(),
      body: JSON.stringify(body),
    }).then(handleResponse),
 
  put: (path, body) =>
    fetch(`${API_BASE_URL}${path}`, {
      method: 'PUT',
      headers: headers(),
      body: JSON.stringify(body),
    }).then(handleResponse),

  patch: (path, body) =>
    fetch(`${API_BASE_URL}${path}`, {
      method: 'PATCH',
      headers: headers(),
      body: JSON.stringify(body),
    }).then(handleResponse),
 
  delete: (path) =>
    fetch(`${API_BASE_URL}${path}`, {
      method: 'DELETE',
      headers: headers(),
    }).then(handleResponse),
};
