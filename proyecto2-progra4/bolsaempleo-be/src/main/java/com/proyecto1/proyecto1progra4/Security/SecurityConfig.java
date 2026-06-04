package com.proyecto1.proyecto1progra4.Security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/home", "/login", "/login**", "/doLogin",
                                "/registro/**", "/puestos/**",
                                "/api/**",
                                "/error", "/notAuthorized",
                                "/css/**", "/Styles/**", "/images/**", "/cvs/**"
                        ).permitAll()
                        .requestMatchers("/Administrador/**").hasAuthority("ADMIN")
                        .requestMatchers("/Empresa/**").hasAuthority("EMPRESA")
                        .requestMatchers("/Oferente/**").hasAuthority("OFERENTE")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/doLogin")
                        .failureUrl("/login?fallo=1")
                        .successHandler(this::roleAwareSuccessHandler)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/notAuthorized")
                )

                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    private void roleAwareSuccessHandler(jakarta.servlet.http.HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws java.io.IOException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ADMIN".equals(a.getAuthority()));
        boolean isEmpresa = authentication.getAuthorities().stream()
                .anyMatch(a -> "EMPRESA".equals(a.getAuthority()));
        boolean isOferente = authentication.getAuthorities().stream()
                .anyMatch(a -> "OFERENTE".equals(a.getAuthority()));

        if (isAdmin) {
            response.sendRedirect("/Administrador/dashboard");
            return;
        }
        if (isEmpresa) {
            response.sendRedirect("/Empresa/dashboard");
            return;
        }
        if (isOferente) {
            response.sendRedirect("/Oferente/dashboard");
            return;
        }
        response.sendRedirect("/home");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
