package com.allantoledo.gia.data.repository;

import com.allantoledo.gia.data.entity.OrgaoDestino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface OrgaoDestinoRepository extends JpaRepository<OrgaoDestino, Long>, JpaSpecificationExecutor<OrgaoDestino> {
}
