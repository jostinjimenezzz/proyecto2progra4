import { createContext, useState } from 'react';

const AppContext = createContext();

function AppProvider(props) {
    const [homeState, setHomeState] = useState({
        puestos: [],
    });

    const [buscarState, setBuscarState] = useState({
        caracteristicas: [],
        resultados: [],
        buscado: false,
        seleccionadas: [],
    });

    const [registroState, setRegistroState] = useState({
        empresa: { correo: '', clave: '', nombre: '', localizacion: '', telefono: '', descripcion: '' },
        oferente: { correo: '', clave: '', identificacion: '', nombre: '', primerApellido: '', nacionalidad: '', telefono: '', lugarResidencia: '' },
        exito: null,
        error: null,
    });

    return (
        <AppContext.Provider value={{
            homeState, setHomeState,
            buscarState, setBuscarState,
            registroState, setRegistroState,
        }}>
            {props.children}
        </AppContext.Provider>
    );
}

export { AppContext, AppProvider };
