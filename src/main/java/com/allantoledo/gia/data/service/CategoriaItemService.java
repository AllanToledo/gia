package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.CategoriaItem;
import com.allantoledo.gia.data.repository.CategoriaItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaItemService {
    private final CategoriaItemRepository categoriaItemRepository;

    public CategoriaItemService(CategoriaItemRepository categoriaItemRepository) {
        this.categoriaItemRepository = categoriaItemRepository;
    }

    public Optional<CategoriaItem> get(Long id) {
        return categoriaItemRepository.findById(id);
    }

    public CategoriaItem update(CategoriaItem entity) {
        return categoriaItemRepository.save(entity);
    }

    public void delete(Long id) {
        categoriaItemRepository.deleteById(id);
    }

    public Page<CategoriaItem> list(Pageable pageable) {
        return categoriaItemRepository.findAll(pageable);
    }

    public Page<CategoriaItem> list(Pageable pageable, Specification<CategoriaItem> filter) {
        return categoriaItemRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) categoriaItemRepository.count();
    }

}
