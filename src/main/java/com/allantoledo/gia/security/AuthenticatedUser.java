package com.allantoledo.gia.security;

import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.data.service.UsuarioRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthenticatedUser {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> usuarioRepository.findByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }

}
