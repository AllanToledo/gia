package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.OrgaoDestino;
import com.allantoledo.gia.data.repository.OrgaoDestinoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgaoDestinoService {

    public static class OrgaoDestinoSpecification {
        public static final String NOME = "nome";
        private OrgaoDestinoSpecification() {}
        public static Specification<OrgaoDestino> filterByName(String nome) {
            return Specification.where(nomeLike(nome));
        }
        private static Specification<OrgaoDestino> nomeLike(String nome) {
            return ((root, query, cb) -> nome == null || nome.isEmpty() ?
                    cb.conjunction() :
                    cb.like(root.get(NOME), "%" + nome + "%"));
        }
    }

    private final OrgaoDestinoRepository orgaoDestinoRepository;

    public OrgaoDestinoService(OrgaoDestinoRepository orgaoDestinoRepository) {
        this.orgaoDestinoRepository = orgaoDestinoRepository;
    }

    public Optional<OrgaoDestino> get(Long id) {
        return orgaoDestinoRepository.findById(id);
    }

    public void update(OrgaoDestino entity) {
        orgaoDestinoRepository.save(entity);
    }

    public void delete(Long id) {
        orgaoDestinoRepository.deleteById(id);
    }

    public Page<OrgaoDestino> list(Pageable pageable){
        return orgaoDestinoRepository.findAll(pageable);
    }

    public Page<OrgaoDestino> list(Pageable pageable, Specification<OrgaoDestino> filter) {
        return orgaoDestinoRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) orgaoDestinoRepository.count();
    }
}
