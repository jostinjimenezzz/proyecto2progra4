import { useAuth } from '@/hooks/useAuth';

export default function OferenteDashboard() {
  const { user } = useAuth();
  return <div style={{ padding: '2rem' }}>Dashboard Oferente: {user?.username}</div>;
}
