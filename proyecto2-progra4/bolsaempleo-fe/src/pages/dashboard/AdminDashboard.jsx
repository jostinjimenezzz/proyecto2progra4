import { useAuth } from '@/hooks/useAuth';

export default function AdminDashboard() {
  const { user } = useAuth();
  return <div style={{ padding: '2rem' }}>Dashboard Admin: {user?.username}</div>;
}
