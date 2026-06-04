package com.proyecto1.proyecto1progra4.Security;

import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Oferente;
import com.proyecto1.proyecto1progra4.Data.Usuario;
import com.proyecto1.proyecto1progra4.Repositories.EmpresaRepository;
import com.proyecto1.proyecto1progra4.Repositories.OferenteRepository;
import com.proyecto1.proyecto1progra4.Repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired private UsuariosRepository usuarioRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private OferenteRepository oferenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usr = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

        boolean enabled = Boolean.TRUE.equals(usr.getHabilitado());

        if (enabled) {
            String rol = usr.getRol();

            if ("EMPRESA".equals(rol)) {
                Empresa emp = empresaRepository.findByUsuario(usr)
                        .orElseThrow(() -> new UsernameNotFoundException("Perfil Empresa no encontrado para " + username));
                enabled = "APROBADO".equals(emp.getEstadoAprobacion());
            } else if ("OFERENTE".equals(rol)) {
                Oferente of = oferenteRepository.findByUsuario(usr)
                        .orElseThrow(() -> new UsernameNotFoundException("Perfil Oferente no encontrado para " + username));
                enabled = "APROBADO".equals(of.getEstadoAprobacion());
            }
        }

        return new UserDetailsImp(usr, enabled);
    }
}