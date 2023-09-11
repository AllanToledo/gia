package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.validations.ValidCpf;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "usuario", indexes = {
        @Index(columnList = "cpf", unique = true)
})
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ValidCpf
    private String cpf;

    @Size(min=3, max=80, message = "Tamanho de nome Inválido")
    private String nome;

    @Column(columnDefinition = "boolean default true")
    private Boolean ativado;

    @JsonIgnore
    private String senhaCriptografada;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        TECNICO, GESTOR;
    }

}
