package com.allantoledo.gia.data.repository;

import com.allantoledo.gia.data.entity.CategoriaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface CategoriaItemRepository extends JpaRepository<CategoriaItem, Long>, JpaSpecificationExecutor<CategoriaItem> {
}
