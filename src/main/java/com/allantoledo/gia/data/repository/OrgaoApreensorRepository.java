package com.allantoledo.gia.data.repository;

import com.allantoledo.gia.data.entity.OrgaoApreensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface OrgaoApreensorRepository extends JpaRepository<OrgaoApreensor, Long>, JpaSpecificationExecutor<OrgaoApreensor> {
}
