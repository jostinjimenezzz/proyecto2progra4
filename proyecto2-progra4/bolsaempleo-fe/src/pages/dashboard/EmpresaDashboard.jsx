import { useAuth } from '@/hooks/useAuth';

export default function EmpresaDashboard() {
  const { user } = useAuth();
  return <div style={{ padding: '2rem' }}>Dashboard Empresa: {user?.username}</div>;
}
