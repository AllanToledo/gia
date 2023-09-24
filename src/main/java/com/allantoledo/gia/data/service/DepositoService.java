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
    public static class DepositoSpecification {
        public static final String NOME = "nome";
        private DepositoSpecification() {}
        public static Specification<Deposito> filterByName(String nome) {
            return Specification.where(nomeLike(nome));
        }
        private static Specification<Deposito> nomeLike(String nome) {
            return ((root, query, cb) -> nome == null || nome.isEmpty() ?
                    cb.conjunction() :
                    cb.like(root.get(NOME), "%" + nome + "%"));
        }
    }

    private final DepositoRepository depositoRepository;

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public Optional<Deposito> get(Long id) {
        return depositoRepository.findById(id);
    }

    public void update(Deposito entity) {
        depositoRepository.save(entity);
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
