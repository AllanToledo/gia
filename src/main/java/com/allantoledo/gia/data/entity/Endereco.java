package com.allantoledo.gia.data.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Endereco extends AbstractEntity {
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
