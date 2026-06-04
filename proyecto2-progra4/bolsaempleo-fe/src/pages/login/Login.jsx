import s from './Login.module.css';

function Login() {
    // El login se mantiene en el backend Spring con sesión tradicional.
    // El formulario hace POST directo al /doLogin del backend.
    function handleSubmit(event) {
        event.preventDefault();
        const form = event.target;
        const username = form.username.value;
        const password = form.password.value;

        const body = new URLSearchParams();
        body.append('username', username);
        body.append('password', password);

        (async () => {
            const response = await fetch('/doLogin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: body.toString(),
                redirect: 'follow',
            });
            if (response.ok || response.redirected) {
                window.location.href = response.url || '/';
            } else {
                alert('Usuario o contraseña incorrectos.');
            }
        })();
    }

    return (
        <div className={s.container}>
            <div className={s.title}>Iniciar Sesión</div>

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
