import s from './Buscar.module.css';
import { useEffect, useContext } from 'react';
import { AppContext } from '@/AppProvider';
import { Link } from 'react-router';

function Buscar() {
    const { buscarState, setBuscarState } = useContext(AppContext);
    const backend = '/api';

    useEffect(() => {
        if (buscarState.caracteristicas.length === 0)
            handleCargarCaracteristicas();
    }, []);

    function handleCargarCaracteristicas() {
        const request = new Request(backend + '/caracteristicas', { method: 'GET', headers: {} });
        (async () => {
            const response = await fetch(request);
            if (!response.ok) { alert('Error: ' + response.status); return; }
            const caracteristicas = await response.json();
            setBuscarState({ ...buscarState, caracteristicas: caracteristicas });
        })();
    }

    function handleCheckChange(event) {
        const id = parseInt(event.target.value);
        const checked = event.target.checked;
        let seleccionadas = [...buscarState.seleccionadas];
        if (checked) {
            seleccionadas.push(id);
        } else {
            seleccionadas = seleccionadas.filter(s => s !== id);
        }
        setBuscarState({ ...buscarState, seleccionadas: seleccionadas });
    }

    function handleBuscar(event) {
        event.preventDefault();
        const ids = buscarState.seleccionadas;
        const query = ids.length > 0 ? '?caracteristicaIds=' + ids.join('&caracteristicaIds=') : '';
        const request = new Request(backend + '/puestos/buscar' + query, { method: 'GET', headers: {} });
        (async () => {
            const response = await fetch(request);
            if (!response.ok) { alert('Error: ' + response.status); return; }
            const resultados = await response.json();
            setBuscarState({ ...buscarState, resultados: resultados, buscado: true });
        })();
    }

    function handleLimpiar() {
        setBuscarState({ ...buscarState, seleccionadas: [], resultados: [], buscado: false });
    }

    return (
        <div className={s.container}>
            <div className={s.title}>Buscar puestos por características</div>

            <form onSubmit={handleBuscar}>
                <label className={s.label}>Seleccioná características:</label>
                <div className={s.checkboxGroup}>
                    {buscarState.caracteristicas.map(c => (
                        <label key={c.id} className={s.checkboxLabel}>
                            <input
                                type="checkbox"
                                value={c.id}
                                checked={buscarState.seleccionadas.includes(c.id)}
                                onChange={handleCheckChange}
                            />
                            {c.nombre}
                        </label>
                    ))}
                </div>
                <div className={s.botones}>
                    <button type="submit" className={s.btn}>Buscar</button>
                    <button type="button" className={s.btn} onClick={handleLimpiar}>Limpiar</button>
                </div>
            </form>

            {buscarState.buscado && (
                <div className={s.resultados}>
                    <h3>Resultados</h3>
                    {buscarState.resultados.length === 0 ? (
                        <p>No se encontraron puestos con esas características.</p>
                    ) : (
                        buscarState.resultados.map(p => (
                            <ResultadoCard key={p.id} puesto={p} />
                        ))
                    )}
                </div>
            )}
        </div>
    );
}

function ResultadoCard({ puesto }) {
    return (
        <div className={s.puestoCard}>
            <h3 className={s.cardEmpresa}>{puesto.empresa?.nombre}</h3>
            <p>{puesto.descripcionGeneral}</p>
            <p>Salario: € {puesto.salarioOfrecido}</p>
            <Link to={`/puestos/detalle/${puesto.id}`} className={s.btn}>Ver detalle</Link>
        </div>
    );
}

export default Buscar;
