import s from './Login.module.css';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { useAuth } from '@/hooks/useAuth';

function Login() {
    const { login } = useAuth();
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    function handleSubmit(event) {
        event.preventDefault();
        const form = event.target;
        const username = form.username.value;
        const clave = form.password.value;
        (async () => {
            try {
                const data = await login(username, clave);
                if (data.role === 'ADMIN') navigate('/dashboard/admin');
                else if (data.role === 'EMPRESA') navigate('/dashboard/empresa');
                else if (data.role === 'OFERENTE') navigate('/dashboard/oferente');
                else navigate('/');
            } catch (e) {
                setError(e.message || 'Usuario o contraseña incorrectos.');
            }
        })();
    }

    return (
        <div className={s.container}>
            <div className={s.title}>Iniciar Sesión</div>
            {error && <div className={s.error}>{error}</div>}

            <form onSubmit={handleSubmit} className={s.form}>
                <label className={s.label}>Correo:</label>
                <input type="email" name="username" required className={s.input} />

                <label className={s.label}>Contraseña:</label>
                <input type="password" name="password" required className={s.input} />

                <button type="submit" className={s.btn}>Ingresar</button>
            </form>
        </div>
    );
}

export default Login;
