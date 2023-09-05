package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   @Query(value = "SELECT u FROM Usuario u WHERE u.cpf = :cpf")
   Usuario findByCpf(String cpf);
}
