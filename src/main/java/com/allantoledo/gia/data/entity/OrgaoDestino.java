package com.allantoledo.gia.data.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class OrgaoDestino extends AbstractEntity {
    private String nome;
    private String contato;
    private String cpfCnpj;
    private Endereco endereco;

}
