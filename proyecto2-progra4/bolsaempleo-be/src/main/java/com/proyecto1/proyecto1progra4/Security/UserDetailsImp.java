package com.proyecto1.proyecto1progra4.Security;

import com.proyecto1.proyecto1progra4.Data.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImp implements UserDetails {
    private final Usuario usuario;
    private boolean enabled = false;

    public UserDetailsImp(Usuario usuario, boolean enabled) {
        this.usuario = usuario;
        this.enabled = enabled;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRol()));
        return authorities;
    }

    @Override
    public String getPassword() { return usuario.getClave(); }

    @Override
    public String getUsername() { return usuario.getUsername(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}