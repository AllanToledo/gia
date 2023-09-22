package com.allantoledo.gia;

import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.data.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UsuarioRepositoryIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void whenFindByCPF_thenReturnUsuario() {
        Usuario usuario = new Usuario();
        usuario.setCpf("69296469031");
        usuarioRepository.save(usuario);

        Usuario found = usuarioRepository.findByCpf(usuario.getCpf());

        assertThat(found.getCpf()).isEqualTo(usuario.getCpf());
    }

}