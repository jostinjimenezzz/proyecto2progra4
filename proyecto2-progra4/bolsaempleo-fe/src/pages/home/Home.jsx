import s from './Home.module.css';
import { useEffect, useContext } from 'react';
import { AppContext } from '@/AppProvider.jsx';
import { Link } from 'react-router';
import { api } from '@/services/api';

function Home() {
    const { homeState, setHomeState } = useContext(AppContext);

    useEffect(() => {
        if (homeState.puestos.length === 0)
            handleList();
    }, []);

    function handleList() {
        (async () => {
            try {
                const puestos = await api.get('/puestos/recientes');
                setHomeState({ ...homeState, puestos });
            } catch (e) {
                alert(e.message);
            }
        })();
    }

    return (
        <div className={s.container}>
            <div className={s.title}>Bolsa de Empleo</div>
            <div className={s.subtitle}>Últimos 5 puestos públicos</div>
            <List puestos={homeState.puestos} />
        </div>
    );
}

function List({ puestos }) {
    return (
        <div className={s.puestosGrid}>
            {puestos.map(puesto => (
                <Card key={puesto.id} puesto={puesto} />
            ))}
        </div>
    );
}

// Construye la ruta jerárquica de una característica usando la lista de requisitos
// Ejemplo: "Lenguajes de programación / Java"
function construirRuta(caracteristica, todosLosRequisitos) {
    const mapa = {};
    todosLosRequisitos.forEach(r => {
        if (r.caracteristica) {
            mapa[r.caracteristica.id] = r.caracteristica;
        }
    });

    const partes = [];
    let actual = caracteristica;
    while (actual) {
        partes.unshift(actual.nombre);
        actual = actual.idPadre ? mapa[actual.idPadre] : null;
    }
    return partes.join(' / ');
}

function Card({ puesto }) {
    const requisitos = puesto.requisitos || [];

    return (
        <div className={s.puestoCard}>
            <h3 className={s.cardEmpresa}>{puesto.empresa?.nombre}</h3>
            <p className={s.cardDesc}>{puesto.descripcionGeneral}</p>
            <p className={s.cardSalario}>₡ {puesto.salarioOfrecido}</p>
            <Link to={`/puestos/detalle/${puesto.id}`} className={s.btn}>Ver detalle</Link>

            <div className={s.puestoTooltip}>
                <strong>Requisitos</strong>
                {requisitos.length > 0 ? (
                    <ul className={s.tooltipList}>
                        {requisitos.map((r, i) => (
                            <li key={i}>
                                • / {construirRuta(r.caracteristica, requisitos)} ({r.nivelDeseado})
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p className={s.tooltipVacio}>Sin características especificadas</p>
                )}
            </div>
        </div>
    );
}

export default Home;
