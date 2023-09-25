package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.ClasseProcesso;
import com.allantoledo.gia.data.repository.ClasseProcessoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClasseProcessoService {

    private final ClasseProcessoRepository classeProcessoRepository;

    public ClasseProcessoService(ClasseProcessoRepository classeProcessoRepository) {
        this.classeProcessoRepository = classeProcessoRepository;
    }

    public Optional<ClasseProcesso> get(Long id) {
        return classeProcessoRepository.findById(id);
    }

    public void update(ClasseProcesso entity) {
        classeProcessoRepository.save(entity);
    }

    public void delete(Long id) {
        classeProcessoRepository.deleteById(id);
    }

    public Page<ClasseProcesso> list(Pageable pageable) {
        return classeProcessoRepository.findAll(pageable);
    }

    public Page<ClasseProcesso> list(Pageable pageable, Specification<ClasseProcesso> filter) {
        return classeProcessoRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) classeProcessoRepository.count();
    }

}
