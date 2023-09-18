package com.allantoledo.gia.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@Table(indexes = {
//        @Index(columnList = "item_apreendido_id")
})
public class Historico{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String estadoAnterior;
    @Column(columnDefinition = "text")
    private String estadoNovo;
    private LocalDateTime horarioAlteracao;
    @ManyToOne
    private Usuario usuario;
}
