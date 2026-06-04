import s from './Registro.module.css';
import { Link, useSearchParams } from 'react-router';

function RegistroExito() {
    const [searchParams] = useSearchParams();
    const tipo = searchParams.get('tipo');

    return (
        <div className={s.container}>
            <div className={s.title}>Registro recibido</div>

            {tipo === 'empresa' && (
                <p>Tu solicitud de registro como <strong>Empresa</strong> fue recibida. Un administrador debe aprobarla para que puedas iniciar sesión.</p>
            )}
            {tipo === 'oferente' && (
                <p>Tu solicitud de registro como <strong>Oferente</strong> fue recibida. Un administrador debe aprobarla para que puedas iniciar sesión.</p>
            )}

            <br />
            <Link to="/" className={s.btn}>Volver al inicio</Link>
        </div>
    );
}

export default RegistroExito;
