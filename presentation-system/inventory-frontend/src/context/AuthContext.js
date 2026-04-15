import { createContext, useContext, useState } from 'react';
import { StorageUtils } from '../utilities/StorageUtils';
import { api } from '../utilities/ApiUtils';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user,    setUser]    = useState(() => StorageUtils.getUser());
  const [token,   setToken]   = useState(() => StorageUtils.getToken());
  const [loading, setLoading] = useState(false);

  const login = async (username, password) => {
    setLoading(true);
    try {
      const data = await api.post('/auth/login', { username, password });
      const jwt = data.jwt || data.token;
      StorageUtils.setToken(jwt);
      StorageUtils.setUser({ username: data.username, role: data.role });
      setToken(jwt);
      setUser({ username: data.username, role: data.role });
      return { success: true };
    } catch (err) {
      return { success: false, error: err.message };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    StorageUtils.clearAll();
    setUser(null);
    setToken(null);
  };

  const hasRole = (required) => {
    if (!user) return false;
    const hierarchy = { ADMIN: 3, MANAGER: 2, STAFF: 1 };
    return (hierarchy[user.role] || 0) >= (hierarchy[required] || 0);
  };

  return (
    <AuthContext.Provider value={{
      user, token, loading,
      login, logout, hasRole,
      isAuthenticated: !!token,
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuthContext = () => useContext(AuthContext);
