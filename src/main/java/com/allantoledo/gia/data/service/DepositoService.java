package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.Deposito;
import com.allantoledo.gia.data.repository.DepositoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepositoService {
    private final DepositoRepository depositoRepository;

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public Optional<Deposito> get(Long id) {
        return depositoRepository.findById(id);
    }

    public Deposito update(Deposito entity) {
        return depositoRepository.save(entity);
    }

    public void delete(Long id) {
        depositoRepository.deleteById(id);
    }

    public Page<Deposito> list(Pageable pageable) {
        return depositoRepository.findAll(pageable);
    }

    public Page<Deposito> list(Pageable pageable, Specification<Deposito> filter) {
        return depositoRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) depositoRepository.count();
    }

}
