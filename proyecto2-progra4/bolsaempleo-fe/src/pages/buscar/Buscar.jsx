import s from './Buscar.module.css';
import { useEffect, useContext, useState } from 'react';
import { AppContext } from '@/AppProvider.jsx';
import { Link } from 'react-router';
import { api } from '@/services/api';

function NodoCaracteristica({ nodo, seleccionadas, onToggle }) {
    const [abierto, setAbierto] = useState(false);
    const tieneHijos = nodo.hijos && nodo.hijos.length > 0;

    return (
        <div className={s.nodo}>
            <div className={s.nodoFila}>
                {tieneHijos ? (
                    <button
                        type="button"
                        className={s.btnToggle}
                        onClick={() => setAbierto(!abierto)}
                        aria-label={abierto ? 'Colapsar' : 'Expandir'}
                    >
                        {abierto ? '▼' : '▶'}
                    </button>
                ) : (
                    <span className={s.espaciador} />
                )}

                <label className={s.checkboxLabel}>
                    <input
                        type="checkbox"
                        value={nodo.id}
                        checked={seleccionadas.includes(nodo.id)}
                        onChange={() => onToggle(nodo.id)}
                    />
                    {nodo.nombre}
                </label>
            </div>

            {tieneHijos && abierto && (
                <div className={s.hijos}>
                    {nodo.hijos.map(hijo => (
                        <NodoCaracteristica
                            key={hijo.id}
                            nodo={hijo}
                            seleccionadas={seleccionadas}
                            onToggle={onToggle}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}

// Construye el árbol a partir de la lista plana
function construirArbol(lista) {
    const mapa = {};
    lista.forEach(c => {
        mapa[c.id] = { ...c, hijos: [] };
    });

    const raices = [];
    lista.forEach(c => {
        if (c.idPadre) {
            if (mapa[c.idPadre]) {
                mapa[c.idPadre].hijos.push(mapa[c.id]);
            }
        } else {
            raices.push(mapa[c.id]);
        }
    });

    return raices;
}

function Buscar() {
    const { buscarState, setBuscarState } = useContext(AppContext);

    // El árbol se deriva directamente de las características — no necesita estado propio
    const arbol = construirArbol(buscarState.caracteristicas);

    useEffect(() => {
        if (buscarState.caracteristicas.length === 0) {
            cargarCaracteristicas();
        }
    }, []);

    async function cargarCaracteristicas() {
        try {
            const caracteristicas = await api.get('/caracteristicas');
            setBuscarState({ ...buscarState, caracteristicas });
        } catch (e) {
            alert(e.message);
        }
    }

    function handleToggle(id) {
        const seleccionadas = buscarState.seleccionadas.includes(id)
            ? buscarState.seleccionadas.filter(s => s !== id)
            : [...buscarState.seleccionadas, id];
        setBuscarState({ ...buscarState, seleccionadas });
    }

    async function handleBuscar(event) {
        event.preventDefault();
        const ids = buscarState.seleccionadas;
        const query = ids.length > 0
            ? '?' + ids.map(id => `caracteristicaIds=${id}`).join('&')
            : '';
        try {
            const resultados = await api.get('/puestos/buscar' + query);
            setBuscarState({ ...buscarState, resultados, buscado: true });
        } catch (e) {
            alert(e.message);
        }
    }

    function handleLimpiar() {
        setBuscarState({ ...buscarState, seleccionadas: [], resultados: [], buscado: false });
    }

    return (
        <div className={s.pagina}>
            <div className={s.panelIzquierdo}>
                <div className={s.titulo}>Buscar Puestos</div>

                <form onSubmit={handleBuscar}>
                    <label className={s.label}>Características</label>

                    <div className={s.arbol}>
                        {arbol.map(raiz => (
                            <NodoCaracteristica
                                key={raiz.id}
                                nodo={raiz}
                                seleccionadas={buscarState.seleccionadas}
                                onToggle={handleToggle}
                            />
                        ))}
                    </div>

                    <div className={s.botones}>
                        <button type="submit" className={s.btn}>Buscar</button>
                        <button type="button" className={s.btnSecundario} onClick={handleLimpiar}>
                            Limpiar
                        </button>
                    </div>
                </form>
            </div>

            <div className={s.panelDerecho}>
                <div className={s.titulo}>Resultados</div>

                {!buscarState.buscado && (
                    <p className={s.hint}>Seleccioná características y presioná Buscar.</p>
                )}

                {buscarState.buscado && buscarState.resultados.length === 0 && (
                    <p className={s.hint}>No se encontraron puestos con esas características.</p>
                )}

                {buscarState.resultados.map(p => (
                    <ResultadoCard key={p.id} puesto={p} />
                ))}
            </div>
        </div>
    );
}

function ResultadoCard({ puesto }) {
    return (
        <div className={s.puestoCard}>
            <h3 className={s.cardEmpresa}>{puesto.empresa?.nombre}</h3>
            <p className={s.cardDesc}>{puesto.descripcionGeneral}</p>
            <p className={s.cardSalario}>₡ {puesto.salarioOfrecido}</p>
            <Link to={`/puestos/detalle/${puesto.id}`} className={s.btn}>Ver detalle</Link>
        </div>
    );
}

export default Buscar;
