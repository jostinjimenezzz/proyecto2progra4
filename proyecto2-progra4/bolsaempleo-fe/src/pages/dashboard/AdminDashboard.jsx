import { useState, useEffect } from 'react';
import { useAuth } from '@/hooks/useAuth';
import { api } from '@/services/api';
import s from './Dashboard.module.css';

function ArbolCaracteristicas({ caracteristicas }) {
    const raices = caracteristicas.filter(c => !c.idPadre);

    function renderHijos(padreId, nivel = 1) {
        const hijos = caracteristicas.filter(c => c.idPadre?.id === padreId);
        if (hijos.length === 0) return null;
        return (
            <ul style={{ paddingLeft: nivel * 20 + 'px', margin: '4px 0' }}>
                {hijos.map(h => (
                    <li key={h.id} style={{ listStyle: 'none', margin: '4px 0' }}>
                        <span style={{ color: '#555' }}>📄 {h.nombre}</span>
                        {renderHijos(h.id, nivel + 1)}
                    </li>
                ))}
            </ul>
        );
    }

    return (
        <ul style={{ paddingLeft: '0', margin: '10px 0' }}>
            {raices.map(r => (
                <li key={r.id} style={{ listStyle: 'none', margin: '6px 0' }}>
                    <strong>📁 {r.nombre}</strong>
                    {renderHijos(r.id)}
                </li>
            ))}
        </ul>
    );
}

export default function AdminDashboard() {
    const { user, logout } = useAuth();
    const [tab, setTab] = useState('empresas');
    const [empresas, setEmpresas] = useState([]);
    const [oferentes, setOferentes] = useState([]);
    const [caracteristicas, setCaracteristicas] = useState([]);
    const [nombreNueva, setNombreNueva] = useState('');
    const [padreSeleccionado, setPadreSeleccionado] = useState('');
    const [mensaje, setMensaje] = useState(null);

    useEffect(() => {
        cargarEmpresas();
        cargarOferentes();
        cargarCaracteristicas();
    }, []);

    async function cargarEmpresas() {
        try {
            const data = await api.get('/admin/empresas/pendientes');
            setEmpresas(data);
        } catch (e) {
            setMensaje('Error cargando empresas: ' + e.message);
        }
    }

    async function cargarOferentes() {
        try {
            const data = await api.get('/admin/oferentes/pendientes');
            setOferentes(data);
        } catch (e) {
            setMensaje('Error cargando oferentes: ' + e.message);
        }
    }

    async function cargarCaracteristicas() {
        try {
            const data = await api.get('/caracteristicas');
            setCaracteristicas(data);
        } catch (e) {
            setMensaje('Error cargando características: ' + e.message);
        }
    }

    async function aprobarEmpresa(id) {
        try {
            await api.post(`/admin/empresas/${id}/aprobar`, {});
            setMensaje('Empresa aprobada correctamente.');
            cargarEmpresas();
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function aprobarOferente(id) {
        try {
            await api.post(`/admin/oferentes/${id}/aprobar`, {});
            setMensaje('Oferente aprobado correctamente.');
            cargarOferentes();
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function crearCaracteristica(e) {
        e.preventDefault();
        try {
            const params = new URLSearchParams();
            params.append('nombre', nombreNueva);
            if (padreSeleccionado) params.append('idPadre', padreSeleccionado);

            await fetch('/api/admin/caracteristicas/crear', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('bolsa_token'),
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params.toString()
            });

            setMensaje('Característica creada correctamente.');
            setNombreNueva('');
            setPadreSeleccionado('');
            cargarCaracteristicas();
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    return (
        <div className={s.container}>
            <div className={s.header}>
                <h2>Panel de Administrador</h2>
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
                <button className={tab === 'empresas' ? s.tabActivo : s.tab}
                        onClick={() => setTab('empresas')}>
                    Empresas pendientes ({empresas.length})
                </button>
                <button className={tab === 'oferentes' ? s.tabActivo : s.tab}
                        onClick={() => setTab('oferentes')}>
                    Oferentes pendientes ({oferentes.length})
                </button>
                <button className={tab === 'caracteristicas' ? s.tabActivo : s.tab}
                        onClick={() => setTab('caracteristicas')}>
                    Características
                </button>
            </div>

            {tab === 'empresas' && (
                <div className={s.seccion}>
                    <h3>Empresas pendientes de aprobación</h3>
                    {empresas.length === 0 ? (
                        <p className={s.vacio}>No hay empresas pendientes.</p>
                    ) : (
                        <table className={s.tabla}>
                            <thead>
                            <tr>
                                <th>Correo</th>
                                <th>Nombre</th>
                                <th>Localización</th>
                                <th>Teléfono</th>
                                <th>Descripción</th>
                                <th>Acción</th>
                            </tr>
                            </thead>
                            <tbody>
                            {empresas.map(e => (
                                <tr key={e.id}>
                                    <td>{e.correo}</td>
                                    <td>{e.nombre}</td>
                                    <td>{e.localizacion}</td>
                                    <td>{e.telefono}</td>
                                    <td>{e.descripcion}</td>
                                    <td>
                                        <button className={s.btnAprobar}
                                                onClick={() => aprobarEmpresa(e.id)}>
                                            Aprobar
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {tab === 'oferentes' && (
                <div className={s.seccion}>
                    <h3>Oferentes pendientes de aprobación</h3>
                    {oferentes.length === 0 ? (
                        <p className={s.vacio}>No hay oferentes pendientes.</p>
                    ) : (
                        <table className={s.tabla}>
                            <thead>
                            <tr>
                                <th>Correo</th>
                                <th>Nombre</th>
                                <th>Identificación</th>
                                <th>Nacionalidad</th>
                                <th>Teléfono</th>
                                <th>Residencia</th>
                                <th>Acción</th>
                            </tr>
                            </thead>
                            <tbody>
                            {oferentes.map(o => (
                                <tr key={o.id}>
                                    <td>{o.correo}</td>
                                    <td>{o.nombre} {o.primerApellido}</td>
                                    <td>{o.identificacion}</td>
                                    <td>{o.nacionalidad}</td>
                                    <td>{o.telefono}</td>
                                    <td>{o.lugarResidencia}</td>
                                    <td>
                                        <button className={s.btnAprobar}
                                                onClick={() => aprobarOferente(o.id)}>
                                            Aprobar
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            <button className={tab === 'reportes' ? s.tabActivo : s.tab}
                    onClick={() => setTab('reportes')}>
                Reportes
            </button>

            {tab === 'caracteristicas' && (
                <div className={s.seccion}>
                    <h3>Crear nueva característica</h3>
                    <form onSubmit={crearCaracteristica} className={s.formCaracteristica}>
                        <input
                            type="text"
                            placeholder="Nombre de la característica"
                            value={nombreNueva}
                            onChange={e => setNombreNueva(e.target.value)}
                            required
                            className={s.input}
                        />
                        <select
                            value={padreSeleccionado}
                            onChange={e => setPadreSeleccionado(e.target.value)}
                            className={s.input}
                        >
                            <option value="">— Sin padre (categoría raíz) —</option>
                            {caracteristicas.map(c => (
                                <option key={c.id} value={c.id}>{c.nombre}</option>
                            ))}
                        </select>
                        <button type="submit" className={s.btnAprobar}>Crear</button>
                    </form>

                    <h3>Características existentes</h3>
                    <ArbolCaracteristicas caracteristicas={caracteristicas} />
                </div>
            )}
            {tab === 'reportes' && (
                <div className={s.seccion}>
                    <h3>Reportes</h3>

                    <div className={s.reportesGrid}>
                        <div className={s.reporteCard}>
                            <h4>Puestos por mes</h4>
                            <p>Reporte de cantidad de puestos registrados por mes en un año.</p>
                            <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                                <input
                                    type="number"
                                    defaultValue={new Date().getFullYear()}
                                    id="anioReporte"
                                    className={s.input}
                                    style={{ width: '100px' }}
                                />
                                <a href="#"
                                   className={s.btnAprobar}
                                   style={{ textDecoration: 'none', padding: '8px 14px' }}
                                   onClick={e => {
                                       e.preventDefault();
                                       const anio = document.getElementById('anioReporte').value;
                                       window.open(`/Administrador/reportes/puestos-por-mes.pdf?anio=${anio}`, '_blank');
                                   }}>
                                    Ver PDF
                                </a>
                            </div>
                        </div>

                        <div className={s.reporteCard}>
                            <h4>Empresas</h4>
                            <p>Reporte de empresas pendientes y aprobadas.</p>
                            <a href="/Administrador/reportes/empresas.pdf"
                               target="_blank"
                               className={s.btnAprobar}
                               style={{ textDecoration: 'none', padding: '8px 14px' }}>
                                Ver PDF
                            </a>
                        </div>

                        <div className={s.reporteCard}>
                            <h4>Oferentes</h4>
                            <p>Reporte de oferentes pendientes y aprobados.</p>
                            <a href="/Administrador/reportes/oferentes.pdf"
                               target="_blank"
                               className={s.btnAprobar}
                               style={{ textDecoration: 'none', padding: '8px 14px' }}>
                                Ver PDF
                            </a>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}