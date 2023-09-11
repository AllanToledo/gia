package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.validations.ValidCpfOrCnpj;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
public class Deposito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String contato;
    @ValidCpfOrCnpj
    private String cpfCnpj;
    private boolean depositarioFiel;
    @OneToOne
    private Endereco endereco;
    @ManyToMany
    private Set<CategoriaItem> categoriasAceitas;
}
