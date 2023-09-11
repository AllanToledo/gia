package com.allantoledo.gia.data.repository;

import com.allantoledo.gia.data.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface HistoricoRepository extends JpaRepository<Historico, Long>, JpaSpecificationExecutor<Historico> {
}
