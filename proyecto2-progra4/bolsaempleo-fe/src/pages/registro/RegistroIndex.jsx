import s from './Registro.module.css';
import { Link } from 'react-router';

function RegistroIndex() {
    return (
        <div className={s.container}>
            <div className={s.title}>Registro</div>
            <div className={s.subtitle}>Seleccioná qué tipo de cuenta querés crear.</div>
            <div className={s.botones}>
                <Link to="/registro/empresa" className={s.btn}>Registrar Empresa</Link>
                <Link to="/registro/oferente" className={s.btn}>Registrar Oferente</Link>
            </div>
        </div>
    );
}

export default RegistroIndex;
