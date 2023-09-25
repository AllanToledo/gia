package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.OrgaoApreensor;
import com.allantoledo.gia.data.repository.OrgaoApreensorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgaoApreensorService {
    private final OrgaoApreensorRepository orgaoApreensorRepository;

    public OrgaoApreensorService(OrgaoApreensorRepository orgaoApreensorRepository) {
        this.orgaoApreensorRepository = orgaoApreensorRepository;
    }

    public Optional<OrgaoApreensor> get(Long id) {
        return orgaoApreensorRepository.findById(id);
    }

    public void update(OrgaoApreensor entity) {
        orgaoApreensorRepository.save(entity);
    }

    public void delete(Long id) {
        orgaoApreensorRepository.deleteById(id);
    }

    public Page<OrgaoApreensor> list(Pageable pageable){
        return orgaoApreensorRepository.findAll(pageable);
    }

    public Page<OrgaoApreensor> list(Pageable pageable, Specification<OrgaoApreensor> filter){
        return orgaoApreensorRepository.findAll(filter, pageable);
    }
    public int count() {
        return (int) orgaoApreensorRepository.count();
    }
}
