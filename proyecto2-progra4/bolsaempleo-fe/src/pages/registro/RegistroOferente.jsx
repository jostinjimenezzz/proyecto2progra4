import s from './Registro.module.css';
import { useContext } from 'react';
import { AppContext } from '@/AppProvider';
import { Link, useNavigate } from 'react-router';
import { api } from '@/services/api';

function RegistroOferente() {
    const { registroState, setRegistroState } = useContext(AppContext);
    const navigate = useNavigate();
    function handleFieldChange(event) {
        const { name, value } = event.target;
        setRegistroState({
            ...registroState,
            oferente: { ...registroState.oferente, [name]: value },
            error: null,
        });
    }

    function handleSave(event) {
        event.preventDefault();
        (async () => {
            try {
                await api.post('/auth/registro/oferente', registroState.oferente);
                navigate('/registro/exito?tipo=oferente');
            } catch (e) {
                setRegistroState({ ...registroState, error: e.message || 'Error al registrar.' });
            }
        })();
    }

    const o = registroState.oferente;

    return (
        <div className={s.container}>
            <div className={s.title}>Registro de Oferente</div>

            {registroState.error && (
                <div className={s.error}>{registroState.error}</div>
            )}

            <form onSubmit={handleSave} className={s.form}>
                <h3>Datos de acceso</h3>

                <label className={s.label}>Correo:</label>
                <input type="email" name="correo" value={o.correo} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Contraseña:</label>
                <input type="password" name="clave" value={o.clave} required onChange={handleFieldChange} className={s.input} />

                <h3>Datos personales</h3>

                <label className={s.label}>Identificación:</label>
                <input type="text" name="identificacion" value={o.identificacion} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Nombre:</label>
                <input type="text" name="nombre" value={o.nombre} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Primer apellido:</label>
                <input type="text" name="primerApellido" value={o.primerApellido} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Nacionalidad:</label>
                <input type="text" name="nacionalidad" value={o.nacionalidad} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Teléfono:</label>
                <input type="text" name="telefono" value={o.telefono} required onChange={handleFieldChange} className={s.input} />

                <label className={s.label}>Residencia:</label>
                <input type="text" name="lugarResidencia" value={o.lugarResidencia} required onChange={handleFieldChange} className={s.input} />

                <div className={s.botones}>
                    <button type="submit" className={s.btn}>Registrar</button>
                    <Link to="/registro" className={s.btn}>Volver</Link>
                </div>
            </form>
        </div>
    );
}

export default RegistroOferente;
