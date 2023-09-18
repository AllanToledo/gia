package com.allantoledo.gia.data.service;

import com.allantoledo.gia.data.entity.Deposito;
import com.allantoledo.gia.data.entity.ItemApreendido;
import com.allantoledo.gia.data.repository.ItemApreendidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemApreendidoService {

    public static class ItemApreendidoSpecification {
        public static final String PROCESS_NUMBER = "numeroProcesso";
        private ItemApreendidoSpecification() {}
        public static Specification<ItemApreendido> filterByProcess(String numeroProcesso) {
            return Specification.where(numberProcessEquals(numeroProcesso));
        }
        private static Specification<ItemApreendido> numberProcessEquals(String numeroProcesso) {
            return ((root, query, cb) -> numeroProcesso == null || numeroProcesso.isEmpty() ?
                    cb.conjunction() :
                    cb.equal(root.get(PROCESS_NUMBER), numeroProcesso));
        }
    }
    
    private final ItemApreendidoRepository itemApreendidoRepository;

    public ItemApreendidoService(ItemApreendidoRepository itemApreendidoRepository) {
        this.itemApreendidoRepository = itemApreendidoRepository;
    }

    public Optional<ItemApreendido> get(Long id) {
        return itemApreendidoRepository.findById(id);
    }

    public ItemApreendido update(ItemApreendido entity) {
        return itemApreendidoRepository.save(entity);
    }

    public void delete(Long id) {
        itemApreendidoRepository.deleteById(id);
    }

    public Page<ItemApreendido> list(Pageable pageable) {
        return itemApreendidoRepository.findAll(pageable);
    }

    public Page<ItemApreendido> list(Pageable pageable, Specification<ItemApreendido> filter) {
        return itemApreendidoRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) itemApreendidoRepository.count();
    }
}
