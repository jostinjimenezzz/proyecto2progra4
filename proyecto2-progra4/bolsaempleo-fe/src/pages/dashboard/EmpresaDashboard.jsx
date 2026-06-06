import { useState, useEffect } from 'react';
import { useAuth } from '@/hooks/useAuth';
import { api } from '@/services/api';
import s from './Dashboard.module.css';

// Modal de detalle del candidato
function ModalCandidato({ candidatoId, onCerrar }) {
    const [detalle, setDetalle] = useState(null);
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        (async () => {
            try {
                const data = await api.get(`/empresa/candidatos/${candidatoId}/detalle`);
                setDetalle(data);
            } catch (e) {
                alert('Error cargando detalle: ' + e.message);
                onCerrar();
            } finally {
                setCargando(false);
            }
        })();
    }, [candidatoId]);

    return (
        <div className={s.modalOverlay} onClick={onCerrar}>
            <div className={s.modal} onClick={e => e.stopPropagation()}>
                <div className={s.modalHeader}>
                    <h3 className={s.modalTitulo}>Detalle del candidato</h3>
                    <button className={s.modalCerrar} onClick={onCerrar}>✕</button>
                </div>

                {cargando ? (
                    <p>Cargando...</p>
                ) : detalle ? (
                    <div>
                        <div className={s.modalSeccion}>
                            <h4>Datos personales</h4>
                            <table className={s.tablaDetalle}>
                                <tbody>
                                <tr><td><strong>Nombre</strong></td><td>{detalle.nombre} {detalle.primerApellido}</td></tr>
                                <tr><td><strong>Identificación</strong></td><td>{detalle.identificacion}</td></tr>
                                <tr><td><strong>Nacionalidad</strong></td><td>{detalle.nacionalidad}</td></tr>
                                <tr><td><strong>Teléfono</strong></td><td>{detalle.telefono}</td></tr>
                                <tr><td><strong>Residencia</strong></td><td>{detalle.lugarResidencia}</td></tr>
                                </tbody>
                            </table>
                        </div>

                        <div className={s.modalSeccion}>
                            <h4>Habilidades</h4>
                            {detalle.habilidades.length === 0 ? (
                                <p className={s.vacio}>Sin habilidades registradas.</p>
                            ) : (
                                <table className={s.tabla}>
                                    <thead>
                                    <tr>
                                        <th>Característica</th>
                                        <th>Nivel</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {detalle.habilidades.map((h, i) => (
                                        <tr key={i}>
                                            <td>{h.nombre}</td>
                                            <td>{h.nivel}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            )}
                        </div>

                        <div className={s.modalSeccion}>
                            <h4>Currículum</h4>
                            {detalle.tieneCv ? (
                                <a
                                    href={`/api/empresa/candidatos/${detalle.id}/cv`}
                                    target="_blank"
                                    className={s.btnAprobar}
                                    style={{ textDecoration: 'none', padding: '8px 14px' }}
                                >
                                    Ver CV (PDF)
                                </a>
                            ) : (
                                <p className={s.vacio}>El candidato no ha subido su CV.</p>
                            )}
                        </div>
                    </div>
                ) : null}
            </div>
        </div>
    );
}

export default function EmpresaDashboard() {
    const { user, logout } = useAuth();
    const [tab, setTab] = useState('puestos');
    const [puestos, setPuestos] = useState([]);
    const [caracteristicas, setCaracteristicas] = useState([]);
    const [mensaje, setMensaje] = useState(null);
    const [mostrarForm, setMostrarForm] = useState(false);
    const [candidatos, setCandidatos] = useState([]);
    const [puestoSeleccionado, setPuestoSeleccionado] = useState(null);
    const [candidatoDetalleId, setCandidatoDetalleId] = useState(null);

    // Form nuevo puesto
    const [descripcion, setDescripcion] = useState('');
    const [salario, setSalario] = useState('');
    const [tipo, setTipo] = useState('PUBLICO');
    const [requisitos, setRequisitos] = useState([]);

    useEffect(() => {
        cargarPuestos();
        cargarCaracteristicas();
    }, []);

    async function cargarPuestos() {
        try {
            const data = await api.get('/empresa/puestos');
            setPuestos(data);
        } catch (e) {
            setMensaje('Error cargando puestos: ' + e.message);
        }
    }

    async function cargarCaracteristicas() {
        try {
            const data = await api.get('/empresa/caracteristicas');
            setCaracteristicas(data);
        } catch (e) {
            setMensaje('Error cargando características: ' + e.message);
        }
    }

    async function desactivarPuesto(id) {
        try {
            await api.post(`/empresa/puestos/${id}/desactivar`, {});
            setMensaje('Puesto desactivado.');
            cargarPuestos();
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function crearPuesto(e) {
        e.preventDefault();
        try {
            await api.post('/empresa/puestos/crear', {
                descripcionGeneral: descripcion,
                salarioOfrecido: parseFloat(salario),
                tipoPublicacion: tipo,
                requisitos: requisitos
            });
            setMensaje('Puesto creado correctamente.');
            setMostrarForm(false);
            setDescripcion('');
            setSalario('');
            setTipo('PUBLICO');
            setRequisitos([]);
            cargarPuestos();
        } catch (e) {
            setMensaje('Error: ' + e.message);
        }
    }

    async function buscarCandidatos(puesto) {
        try {
            const data = await api.get(`/empresa/candidatos/${puesto.id}`);
            setCandidatos(data);
            setPuestoSeleccionado(puesto);
            setTab('candidatos');
        } catch (e) {
            setMensaje('Error buscando candidatos: ' + e.message);
        }
    }

    function agregarRequisito() {
        setRequisitos([...requisitos, { caracteristicaId: '', nivelDeseado: 1 }]);
    }

    function actualizarRequisito(index, campo, valor) {
        const nuevos = [...requisitos];
        nuevos[index][campo] = campo === 'nivelDeseado' ? parseInt(valor) : parseInt(valor);
        setRequisitos(nuevos);
    }

    function eliminarRequisito(index) {
        setRequisitos(requisitos.filter((_, i) => i !== index));
    }

    return (
        <div className={s.container}>
            <div className={s.header}>
                <h2>Panel de Empresa</h2>
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
                <button className={tab === 'puestos' ? s.tabActivo : s.tab}
                        onClick={() => setTab('puestos')}>
                    Mis puestos ({puestos.length})
                </button>
                {puestoSeleccionado && (
                    <button className={tab === 'candidatos' ? s.tabActivo : s.tab}
                            onClick={() => setTab('candidatos')}>
                        Candidatos — {puestoSeleccionado.descripcionGeneral.substring(0, 30)}...
                    </button>
                )}
            </div>

            {tab === 'puestos' && (
                <div className={s.seccion}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <h3>Mis puestos publicados</h3>
                        <button className={s.btnAprobar} onClick={() => setMostrarForm(!mostrarForm)}>
                            {mostrarForm ? 'Cancelar' : '+ Publicar nuevo puesto'}
                        </button>
                    </div>

                    {mostrarForm && (
                        <form onSubmit={crearPuesto} className={s.formPuesto}>
                            <h4>Nuevo puesto</h4>

                            <label className={s.label}>Descripción general:</label>
                            <textarea
                                value={descripcion}
                                onChange={e => setDescripcion(e.target.value)}
                                required
                                className={s.input}
                                rows={3}
                            />

                            <label className={s.label}>Salario ofrecido:</label>
                            <input
                                type="number"
                                value={salario}
                                onChange={e => setSalario(e.target.value)}
                                required
                                className={s.input}
                                min="0"
                                step="0.01"
                            />

                            <label className={s.label}>Tipo de publicación:</label>
                            <select value={tipo} onChange={e => setTipo(e.target.value)} className={s.input}>
                                <option value="PUBLICO">Público</option>
                                <option value="PRIVADO">Privado</option>
                            </select>

                            <h4>Características requeridas</h4>
                            {requisitos.map((r, i) => (
                                <div key={i} className={s.requisitoRow}>
                                    <select
                                        value={r.caracteristicaId}
                                        onChange={e => actualizarRequisito(i, 'caracteristicaId', e.target.value)}
                                        required
                                        className={s.input}
                                    >
                                        <option value="">— Seleccionar característica —</option>
                                        {caracteristicas.map(c => (
                                            <option key={c.id} value={c.id}>{c.nombre}</option>
                                        ))}
                                    </select>
                                    <select
                                        value={r.nivelDeseado}
                                        onChange={e => actualizarRequisito(i, 'nivelDeseado', e.target.value)}
                                        className={s.inputNivel}
                                    >
                                        {[1,2,3,4,5].map(n => (
                                            <option key={n} value={n}>Nivel {n}</option>
                                        ))}
                                    </select>
                                    <button type="button" className={s.btnEliminar}
                                            onClick={() => eliminarRequisito(i)}>✕</button>
                                </div>
                            ))}
                            <button type="button" className={s.btnSecundario} onClick={agregarRequisito}>
                                + Agregar característica
                            </button>

                            <br /><br />
                            <button type="submit" className={s.btnAprobar}>Publicar puesto</button>
                        </form>
                    )}

                    {puestos.length === 0 ? (
                        <p className={s.vacio}>No tienes puestos publicados.</p>
                    ) : (
                        <table className={s.tabla}>
                            <thead>
                            <tr>
                                <th>Descripción</th>
                                <th>Salario</th>
                                <th>Tipo</th>
                                <th>Estado</th>
                                <th>Características</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            {puestos.map(p => (
                                <tr key={p.id}>
                                    <td>{p.descripcionGeneral}</td>
                                    <td>₡ {p.salarioOfrecido}</td>
                                    <td>{p.tipoPublicacion}</td>
                                    <td>
                                        <span style={{
                                            color: p.activo ? '#27ae60' : '#e74c3c',
                                            fontWeight: 'bold'
                                        }}>
                                            {p.activo ? 'Activo' : 'Inactivo'}
                                        </span>
                                    </td>
                                    <td>
                                        {p.requisitos?.map((r, i) => (
                                            <div key={i} style={{ fontSize: '12px' }}>
                                                {r.nombreCaracteristica} (nivel {r.nivelDeseado})
                                            </div>
                                        ))}
                                    </td>
                                    <td style={{ display: 'flex', gap: '6px', flexDirection: 'column' }}>
                                        <button className={s.btnAprobar}
                                                onClick={() => buscarCandidatos(p)}>
                                            Buscar candidatos
                                        </button>
                                        {p.activo && (
                                            <button className={s.btnDesactivar}
                                                    onClick={() => desactivarPuesto(p.id)}>
                                                Desactivar
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {tab === 'candidatos' && puestoSeleccionado && (
                <div className={s.seccion}>
                    <h3>Candidatos para: {puestoSeleccionado.descripcionGeneral}</h3>
                    {candidatos.length === 0 ? (
                        <p className={s.vacio}>No se encontraron candidatos.</p>
                    ) : (
                        <table className={s.tabla}>
                            <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Identificación</th>
                                <th>Nacionalidad</th>
                                <th>Teléfono</th>
                                <th>Residencia</th>
                                <th>Coincidencia</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            {candidatos.map(c => (
                                <tr key={c.id}>
                                    <td>{c.nombre} {c.primerApellido}</td>
                                    <td>{c.identificacion}</td>
                                    <td>{c.nacionalidad}</td>
                                    <td>{c.telefono}</td>
                                    <td>{c.lugarResidencia}</td>
                                    <td>
                                        <span style={{
                                            fontWeight: 'bold',
                                            color: parseInt(c.porcentaje) >= 75 ? '#27ae60' :
                                                parseInt(c.porcentaje) >= 50 ? '#f39c12' : '#e74c3c'
                                        }}>
                                            {c.porcentaje} ({c.cumplidos}/{c.total})
                                        </span>
                                    </td>
                                    <td style={{ display: 'flex', gap: '6px', flexDirection: 'column' }}>
                                        <button
                                            className={s.btnAprobar}
                                            onClick={() => setCandidatoDetalleId(c.id)}
                                        >
                                            Ver detalle
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {/* Modal detalle candidato */}
            {candidatoDetalleId && (
                <ModalCandidato
                    candidatoId={candidatoDetalleId}
                    onCerrar={() => setCandidatoDetalleId(null)}
                />
            )}
        </div>
    );
}