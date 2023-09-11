package com.allantoledo.gia.security;

import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.data.repository.UsuarioRepository;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCpf(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Nenhum usuário cadastrado com CPF: " + username);
        } else {
            if(!usuario.getAtivado()) throw new UsernameNotFoundException("Usuário desativado");
            return new User(usuario.getCpf(), usuario.getSenhaCriptografada(), getAuthorities(usuario));
        }
    }

    private static List<GrantedAuthority> getAuthorities(Usuario usuario) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole()));
    }

}
