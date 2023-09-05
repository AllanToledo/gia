package com.allantoledo.gia.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "application_user", indexes = {
        @Index(columnList = "cpf", unique = true)
})
@Data
public class Usuario {

    @Id
    private String cpf;

    public String nome;
    @Column(columnDefinition = "boolean default true")
    private Boolean ativado = true;

    @JsonIgnore
    private String senhaCriptografada;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        TECNICO, GESTOR;
    }
}
