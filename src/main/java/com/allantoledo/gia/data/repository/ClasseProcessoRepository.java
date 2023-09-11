package com.allantoledo.gia.data.repository;

import com.allantoledo.gia.data.entity.ClasseProcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ClasseProcessoRepository extends JpaRepository<ClasseProcesso, Long>, JpaSpecificationExecutor<ClasseProcesso> {
}
