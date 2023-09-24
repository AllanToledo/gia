package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.Historico;
import com.allantoledo.gia.data.repository.HistoricoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HistoricoService {
    private final HistoricoRepository historicoRepository;

    public HistoricoService(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    public Optional<Historico> get(Long id){
        return historicoRepository.findById(id);
    }

    public void update(Historico entity){
        historicoRepository.save(entity);
    }

    public void delete(Long id){
        historicoRepository.deleteById(id);
    }

    public Page<Historico> list(Pageable pageable){
        return historicoRepository.findAll(pageable);
    }

    public Page<Historico> list(Pageable pageable, Specification<Historico> filter){
        return historicoRepository.findAll(filter, pageable);
    }

    public int count(){
        return (int) historicoRepository.count();
    }
}
