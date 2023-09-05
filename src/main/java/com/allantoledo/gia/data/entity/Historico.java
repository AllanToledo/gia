package com.allantoledo.gia.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(indexes = {
        @Index(columnList = "fk_item_id")
})
public class Historico extends AbstractEntity{
    private String estadoAnterior;
    private String estadoNovo;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="fk_item_id")
    private ItemApreendido itemApreendido;
    @ManyToOne
    private Usuario usuario;
}
