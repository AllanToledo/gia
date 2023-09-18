package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.Usuario;

import java.util.Optional;

import com.allantoledo.gia.data.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    public static class UsuarioSpecification {
        public static final String NOME = "nome";
        public static final String ROLE = "role";
        private UsuarioSpecification() {}
        public static Specification<Usuario> filterTecnicoByName(String nome) {
            return Specification.where(nomeLike(nome));
        }

        private static Specification<Usuario> roleIs(Usuario.Role role) {
            return ((root, query, cb) -> role == null ?
                    cb.conjunction() :
                    cb.equal(root.get(ROLE), role));
        }

        private static Specification<Usuario> nomeLike(String nome) {
            return ((root, query, cb) -> nome == null || nome.isEmpty() ?
                    cb.conjunction() :
                    cb.like(root.get(NOME), "%" + nome + "%"));
        }
    }

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Optional<Usuario> get(Long id) {
        return repository.findById(id);
    }

    public Usuario update(Usuario entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Usuario> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Usuario> list(Pageable pageable, Specification<Usuario> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
