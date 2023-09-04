package com.allantoledo.gia.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Historico extends AbstractEntity{
    private String estadoAnterior;
    private String estadoNovo;
    @JsonIgnore
    private ItemApreendido itemApreendido;
    private Usuario usuario;
}
