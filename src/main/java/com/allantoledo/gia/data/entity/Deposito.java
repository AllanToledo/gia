package com.allantoledo.gia.data.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Deposito extends AbstractEntity{
    private String nome;
    private String contato;
    private String cpfCnpj;
    private boolean depositarioFiel;
    private Endereco endereco;
}
