import s from './Registro.module.css';
import { useContext } from 'react';
import { AppContext } from '@/AppProvider';
import { Link, useNavigate } from 'react-router';

function RegistroEmpresa() {
    const { registroState, setRegistroState } = useContext(AppContext);
    const navigate = useNavigate();
    const backend = '/api';

    function handleFieldChange(event) {
        const { name, value } = event.target;
        setRegistroState({
            ...registroState,
            empresa: { ...registroState.empresa, [name]: value },
            error: null,
        });
    }

    function handleSave(event) {
        event.preventDefault();
        const request = new Request(backend + '/registro/empresa', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(registroState.empresa),
        });
        (async () => {
            const response = await fetch(request);
            if (!response.ok) {
                const msg = await response.text();
                setRegistroState({ ...registroState, error: msg || 'Error al registrar.' });
                return;
            }
            navigate('/registro/exito?tipo=empresa');
        })();
    }

    const e = registroState.empresa;

    return (
        <div className={s.container}>
            <div className={s.title}>Registro de Empresa</div>

            {registroState.error && (
                <div className={s.error}>{registroState.error}</div>
            )}

            <form onSubmit={handleSave} className={s.form}>
                <h3>Datos de acceso</h3>

                <label className={s.label}>Correo:</label>
                <input type="email" name="correo" value={e.correo} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Contraseña:</label>
                <input type="password" name="clave" value={e.clave} required onChange={handleFieldChange} className={s.input} />

                <h3>Datos de la empresa</h3>

                <label className={s.label}>Nombre:</label>
                <input type="text" name="nombre" value={e.nombre} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Localización:</label>
                <input type="text" name="localizacion" value={e.localizacion} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Teléfono:</label>
                <input type="text" name="telefono" value={e.telefono} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Descripción:</label>
                <input type="text" name="descripcion" value={e.descripcion} onChange={handleFieldChange} className={s.input} />

                <div className={s.botones}>
                    <button type="submit" className={s.btn}>Registrar</button>
                    <Link to="/registro" className={s.btn}>Volver</Link>
                </div>
            </form>
        </div>
    );
}

export default RegistroEmpresa;
