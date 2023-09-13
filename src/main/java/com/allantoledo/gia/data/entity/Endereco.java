package com.allantoledo.gia.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80)
    private String logradouro;
    @Size(max=80)
    private String bairro;
    @Size(max=80)
    private String cidade;
    @Size(max=2)
    private String estado;
    @Size(max=8)
    private String cep;
    @Size(max=10)
    private String numero;
}
