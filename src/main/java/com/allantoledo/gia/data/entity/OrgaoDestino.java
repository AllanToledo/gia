package com.allantoledo.gia.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgaoDestino extends AbstractEntity {
    private String nome;
    private String contato;
    private String cpfCnpj;
    @OneToOne
    private Endereco endereco;
    @ManyToMany
    private Set<CategoriaItem> categoriasAceitas;
}
