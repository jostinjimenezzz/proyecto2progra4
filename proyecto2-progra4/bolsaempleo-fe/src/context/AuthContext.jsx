import { createContext, useEffect, useMemo, useState } from 'react';
import { api } from '@/services/api';
import { clearToken, getToken, setToken } from '@/utils/storage';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setTokenState] = useState(getToken());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadUser = async () => {
      if (!token) {
        setLoading(false);
        return;
      }
      try {
        const me = await api.get('/usuarios/me');
        setUser(me);
      } catch {
        clearToken();
        setTokenState(null);
      } finally {
        setLoading(false);
      }
    };
    loadUser();
  }, [token]);

  const login = async (username, clave) => {
    const data = await api.post('/auth/login', { username, clave });
    setToken(data.token);
    setTokenState(data.token);
    setUser({ username: data.username, role: data.role });
    return data;
  };

  const logout = () => {
    clearToken();
    setTokenState(null);
    setUser(null);
  };

  const value = useMemo(() => ({ user, token, loading, login, logout }), [user, token, loading]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
