package com.allantoledo.gia.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
public class OrgaoDestino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String contato;
    private String cpfCnpj;
    @OneToOne
    private Endereco endereco;
    @ManyToMany
    private Set<CategoriaItem> categoriasAceitas;
}
