package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.Endereco;
import com.allantoledo.gia.data.repository.EnderecoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Optional<Endereco> get(Long id) {
        return enderecoRepository.findById(id);
    }

    public Endereco update(Endereco entity) {
        return enderecoRepository.save(entity);
    }

    public void delete(Long id) {
        enderecoRepository.deleteById(id);
    }

    public Page<Endereco> list(Pageable pageable) {
        return enderecoRepository.findAll(pageable);
    }

    public Page<Endereco> list(Pageable pageable, Specification<Endereco> filter) {
        return enderecoRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) enderecoRepository.count();
    }
}
