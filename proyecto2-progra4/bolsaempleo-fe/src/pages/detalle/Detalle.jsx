import s from './Detalle.module.css';
import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router';
import { api } from '@/services/api';

function Detalle() {
    const { id } = useParams();
    const [puesto, setPuesto] = useState(null);
    const [requisitos, setRequisitos] = useState([]);
    useEffect(() => {
        handleCargarDetalle();
    }, [id]);

    function handleCargarDetalle() {
        (async () => {
            try {
                const data = await api.get('/puestos/detalle/' + id);
                setPuesto(data.puesto);
                setRequisitos(data.requisitos);
            } catch (e) {
                alert(e.message);
            }
        })();
    }

    if (!puesto) return <div className={s.container}><p>Cargando...</p></div>;

    return (
        <div className={s.container}>
            <div className={s.title}>{puesto.empresa?.nombre}</div>
            <div className={s.subtitle}>{puesto.descripcionGeneral}</div>

            <p>Salario: € {puesto.salarioOfrecido}</p>
            <p>Tipo: {puesto.tipoPublicacion}</p>

            <h3>Características requeridas</h3>
            {requisitos.length === 0 ? (
                <p>No se especificaron características.</p>
            ) : (
                <table className={s.tabla}>
                    <thead>
                        <tr>
                            <th>Característica</th>
                            <th>Nivel mínimo</th>
                        </tr>
                    </thead>
                    <tbody>
                        {requisitos.map((r, i) => (
                            <tr key={i}>
                                <td>{r.caracteristica?.nombre}</td>
                                <td>{r.nivelDeseado}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}

            <br />
            <Link to="/puestos/buscar" className={s.btnVolver}>Volver</Link>
        </div>
    );
}

export default Detalle;
