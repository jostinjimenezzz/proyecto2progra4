import { Navigate } from 'react-router';
import { useAuth } from '@/hooks/useAuth';

export default function ProtectedRoute({ roles = [], children }) {
  const { user, loading } = useAuth();

  if (loading) return <div>Cargando...</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (roles.length > 0 && !roles.includes(user.role)) return <Navigate to="/" replace />;

  return children;
}
