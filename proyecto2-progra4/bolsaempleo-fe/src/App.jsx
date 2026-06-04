import './App.css';
import { Link, BrowserRouter, Routes, Route } from 'react-router';
import { AppProvider } from '@/AppProvider.jsx';
import { AuthProvider } from '@/context/AuthContext';
import ProtectedRoute from '@/components/ProtectedRoute';
import Home from '@/pages/home/Home.jsx';
import Buscar from '@/pages/buscar/Buscar.jsx';
import Detalle from '@/pages/detalle/Detalle.jsx';
import Login from '@/pages/login/Login.jsx';
import RegistroIndex from '@/pages/registro/RegistroIndex.jsx';
import RegistroEmpresa from '@/pages/registro/RegistroEmpresa.jsx';
import RegistroOferente from '@/pages/registro/RegistroOferente.jsx';
import RegistroExito from '@/pages/registro/RegistroExito.jsx';
import AdminDashboard from '@/pages/dashboard/AdminDashboard';
import EmpresaDashboard from '@/pages/dashboard/EmpresaDashboard';
import OferenteDashboard from '@/pages/dashboard/OferenteDashboard';

function App() {
    return (
        <AuthProvider>
            <AppProvider>
                <BrowserRouter>
                    <Header />
                    <Main />
                    <Footer />
                </BrowserRouter>
            </AppProvider>
        </AuthProvider>
    );
}

function Header() {
    return (
        <header className="header">
            <div className="header-left">
                <span className="logo">BolsaEmpleo</span>
                <nav className="nav-links">
                    <Link to="/">Inicio</Link>
                    <Link to="/puestos/buscar">Buscar puestos</Link>
                    <Link to="/registro/empresa">Registro Empresa</Link>
                    <Link to="/registro/oferente">Registro Oferente</Link>
                </nav>
            </div>
            <div className="header-right">
                <Link to="/login">Login</Link>
            </div>
        </header>
    );
}

function Main() {
    return (
        <div className="main">
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/puestos/buscar" element={<Buscar />} />
                <Route path="/puestos/detalle/:id" element={<Detalle />} />
                <Route path="/login" element={<Login />} />
                <Route path="/registro" element={<RegistroIndex />} />
                <Route path="/registro/empresa" element={<RegistroEmpresa />} />
                <Route path="/registro/oferente" element={<RegistroOferente />} />
                <Route path="/registro/exito" element={<RegistroExito />} />
                <Route path="/dashboard/admin" element={
                    <ProtectedRoute roles={['ADMIN']}><AdminDashboard /></ProtectedRoute>
                } />
                <Route path="/dashboard/empresa" element={
                    <ProtectedRoute roles={['EMPRESA']}><EmpresaDashboard /></ProtectedRoute>
                } />
                <Route path="/dashboard/oferente" element={
                    <ProtectedRoute roles={['OFERENTE']}><OferenteDashboard /></ProtectedRoute>
                } />
            </Routes>
        </div>
    );
}

function Footer() {
    return (
        <footer className="footer">
            <div>
                <strong>Bolsa de Empleo</strong><br />
                <small>Total Soft Inc.</small>
            </div>
            <div>
                <small>Contacto: info@bolsaempleo.local</small>
            </div>
        </footer>
    );
}

export default App;
