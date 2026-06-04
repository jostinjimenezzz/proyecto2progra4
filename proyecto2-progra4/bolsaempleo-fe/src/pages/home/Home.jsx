import s from './Home.module.css';
import { useEffect, useContext } from 'react';
import { AppContext } from '@/AppProvider';
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
                setHomeState({ ...homeState, puestos: puestos });
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

function Card({ puesto }) {
    return (
        <div className={s.puestoCard}>
            <h3 className={s.cardEmpresa}>{puesto.empresa?.nombre}</h3>
            <p className={s.cardDesc}>{puesto.descripcionGeneral}</p>
            <p className={s.cardSalario}>€ {puesto.salarioOfrecido}</p>
            <Link to={`/puestos/detalle/${puesto.id}`} className={s.btn}>Ver detalle</Link>

            <div className={s.puestoTooltip}>
                <strong>Características requeridas:</strong><br />
                {puesto.requisitos && puesto.requisitos.length > 0 ? (
                    <ul className={s.tooltipList}>
                        {puesto.requisitos.map((r, i) => (
                            <li key={i}>{r.caracteristica?.nombre} (nivel {r.nivelDeseado})</li>
                        ))}
                    </ul>
                ) : (
                    <span>Sin características especificadas</span>
                )}
            </div>
        </div>
    );
}

export default Home;
