import { useState, useEffect } from 'react';
import { useAuth } from '@/hooks/useAuth';
import { api } from '@/services/api';
import s from './Dashboard.module.css';

export default function OferenteDashboard() {
    const { user, logout } = useAuth();
    const [tab, setTab] = useState('habilidades');
    const [caracteristicas, setCaracteristicas] = useState([]);
    const [habilidades, setHabilidades] = useState([]);
    const [mensaje, setMensaje] = useState(null);
    const [infoCv, setInfoCv] = useState({ tieneCv: false });

    // Navegación jerárquica
    const [padreActual, setPadreActual] = useState(null);
    const [ruta, setRuta] = useState([]);

    // Form agregar habilidad
    const [caracteristicaSeleccionada, setCaracteristicaSeleccionada] = useState(null);
    const [nivelSeleccionado, setNivelSeleccionado] = useState(1);

    useEffect(() => {
        cargarCaracteristicas();
        cargarHabilidades();
        cargarInfoCv();
    }, []);

    async function cargarCaracteristicas() {
        try {
            const data = await api.get('/oferente/caracteristicas');
            setCaracteristicas(data);
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function cargarHabilidades() {
        try {
            const data = await api.get('/oferente/habilidades');
            setHabilidades(data);
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function cargarInfoCv() {
        try {
            const data = await api.get('/oferente/cv/info');
            setInfoCv(data);
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function agregarHabilidad() {
        if (!caracteristicaSeleccionada) return;
        try {
            await api.post('/oferente/habilidades/agregar', {
                caracteristicaId: caracteristicaSeleccionada.id,
                nivel: nivelSeleccionado
            });
            setMensaje('Habilidad agregada correctamente.');
            setCaracteristicaSeleccionada(null);
            setNivelSeleccionado(1);
            cargarHabilidades();
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function eliminarHabilidad(caracteristicaId) {
        try {
            await fetch(`/api/oferente/habilidades/${caracteristicaId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('bolsa_token')
                }
            });
            setMensaje('Habilidad eliminada.');
            cargarHabilidades();
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function subirCv(e) {
        e.preventDefault();
        const archivo = e.target.archivo.files[0];
        if (!archivo) return;

        const formData = new FormData();
        formData.append('archivo', archivo);

        try {
            await fetch('/api/oferente/cv/subir', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('bolsa_token')
                },
                body: formData
            });
            setMensaje('CV subido correctamente.');
            cargarInfoCv();
            e.target.reset();
        } catch (e) {
            setMensaje('Error subiendo CV: ' + e.message);
        }
    }

    // Navegación jerárquica
    const hijos = caracteristicas.filter(c => c.idPadre === padreActual);
    const tieneHijos = (id) => caracteristicas.some(c => c.idPadre === id);
    const yaAgregada = (id) => habilidades.some(h => h.caracteristicaId === id);

    function entrarCategoria(cat) {
        setRuta([...ruta, cat]);
        setPadreActual(cat.id);
        setCaracteristicaSeleccionada(null);
    }

    function volverA(index) {
        if (index === -1) {
            setRuta([]);
            setPadreActual(null);
        } else {
            const nuevaRuta = ruta.slice(0, index + 1);
            setRuta(nuevaRuta);
            setPadreActual(nuevaRuta[nuevaRuta.length - 1].id);
        }
        setCaracteristicaSeleccionada(null);
    }

    return (
        <div className={s.container}>
            <div className={s.header}>
                <h2>Panel de Oferente</h2>
                <div className={s.userInfo}>
                    <span>{user?.username}</span>
                    <button onClick={logout} className={s.btnLogout}>Cerrar sesión</button>
                </div>
            </div>

            {mensaje && (
                <div className={s.mensaje} onClick={() => setMensaje(null)}>
                    {mensaje} <span className={s.cerrar}>✕</span>
                </div>
            )}

            <div className={s.tabs}>
                <button className={tab === 'habilidades' ? s.tabActivo : s.tab}
                        onClick={() => setTab('habilidades')}>
                    Mis habilidades ({habilidades.length})
                </button>
                <button className={tab === 'cv' ? s.tabActivo : s.tab}
                        onClick={() => setTab('cv')}>
                    Mi CV
                </button>
            </div>

            {tab === 'habilidades' && (
                <div className={s.seccion}>
                    <div style={{ display: 'flex', gap: '24px' }}>

                        {/* Panel izquierdo: navegación */}
                        <div style={{ flex: 1 }}>
                            <h3>Explorar características</h3>

                            {/* Breadcrumb */}
                            <div className={s.breadcrumb}>
                                <span className={s.breadcrumbLink} onClick={() => volverA(-1)}>
                                    Raíz
                                </span>
                                {ruta.map((r, i) => (
                                    <span key={r.id}>
                                        {' › '}
                                        <span className={s.breadcrumbLink} onClick={() => volverA(i)}>
                                            {r.nombre}
                                        </span>
                                    </span>
                                ))}
                            </div>

                            <ul className={s.listaCaracteristicas}>
                                {hijos.map(c => (
                                    <li key={c.id} className={s.itemCaracteristica}>
                                        {tieneHijos(c.id) ? (
                                            <button className={s.btnCategoria}
                                                    onClick={() => entrarCategoria(c)}>
                                                📁 {c.nombre} ›
                                            </button>
                                        ) : (
                                            <div className={s.itemHoja}>
                                                <span>📄 {c.nombre}</span>
                                                {yaAgregada(c.id) ? (
                                                    <span className={s.yaAgregada}>✓ Agregada</span>
                                                ) : (
                                                    <button className={s.btnSeleccionar}
                                                            onClick={() => setCaracteristicaSeleccionada(c)}>
                                                        + Seleccionar
                                                    </button>
                                                )}
                                            </div>
                                        )}
                                    </li>
                                ))}
                                {hijos.length === 0 && (
                                    <p className={s.vacio}>No hay elementos aquí.</p>
                                )}
                            </ul>

                            {/* Form agregar habilidad */}
                            {caracteristicaSeleccionada && (
                                <div className={s.formHabilidad}>
                                    <p>Agregar: <strong>{caracteristicaSeleccionada.nombre}</strong></p>
                                    <label className={s.label}>Nivel (1-5):</label>
                                    <select value={nivelSeleccionado}
                                            onChange={e => setNivelSeleccionado(parseInt(e.target.value))}
                                            className={s.input}>
                                        {[1,2,3,4,5].map(n => (
                                            <option key={n} value={n}>Nivel {n}</option>
                                        ))}
                                    </select>
                                    <div style={{ display: 'flex', gap: '8px', marginTop: '8px' }}>
                                        <button className={s.btnAprobar} onClick={agregarHabilidad}>
                                            Confirmar
                                        </button>
                                        <button className={s.btnSecundario}
                                                onClick={() => setCaracteristicaSeleccionada(null)}>
                                            Cancelar
                                        </button>
                                    </div>
                                </div>
                            )}
                        </div>

                        {/* Panel derecho: mis habilidades */}
                        <div style={{ flex: 1 }}>
                            <h3>Mis habilidades</h3>
                            {habilidades.length === 0 ? (
                                <p className={s.vacio}>No tienes habilidades registradas.</p>
                            ) : (
                                <table className={s.tabla}>
                                    <thead>
                                    <tr>
                                        <th>Característica</th>
                                        <th>Nivel</th>
                                        <th>Acción</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {habilidades.map(h => (
                                        <tr key={h.caracteristicaId}>
                                            <td>{h.nombre}</td>
                                            <td>{h.nivel}</td>
                                            <td>
                                                <button className={s.btnDesactivar}
                                                        onClick={() => eliminarHabilidad(h.caracteristicaId)}>
                                                    Eliminar
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                </div>
            )}

            {tab === 'cv' && (
                <div className={s.seccion}>
                    <h3>Mi Currículum</h3>

                    {infoCv.tieneCv ? (
                        <div className={s.cvInfo}>
                            <p>✅ CV subido: <strong>{infoCv.nombreOriginal}</strong></p>
                            <div style={{ display: 'flex', gap: '10px' }}>
                                <a href="/api/oferente/cv/ver" target="_blank"
                                   className={s.btnAprobar}
                                   style={{ textDecoration: 'none', padding: '8px 14px' }}>
                                    Ver CV
                                </a>
                            </div>
                        </div>
                    ) : (
                        <p className={s.vacio}>No tienes CV subido.</p>
                    )}

                    <h4 style={{ marginTop: '24px' }}>
                        {infoCv.tieneCv ? 'Reemplazar CV' : 'Subir CV'}
                    </h4>
                    <form onSubmit={subirCv} className={s.formCv}>
                        <input type="file" name="archivo" accept=".pdf" required className={s.input} />
                        <button type="submit" className={s.btnAprobar}>Subir PDF</button>
                    </form>
                </div>
            )}
        </div>
    );
}
