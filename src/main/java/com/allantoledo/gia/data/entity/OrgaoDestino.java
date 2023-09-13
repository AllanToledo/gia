package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.validations.ValidCpfOrCnpj;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
public class OrgaoDestino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80)
    private String nome;
    @Size(max=80)
    private String contato;
    @ValidCpfOrCnpj
    private String cpfCnpj;
    @OneToOne
    private Endereco endereco;
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<CategoriaItem> categoriasAceitas;
}
