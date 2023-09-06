package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   Usuario findByCpf(String cpf);
}
