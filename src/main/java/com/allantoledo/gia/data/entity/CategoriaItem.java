package com.allantoledo.gia.data.entity;

import com.github.javaparser.quality.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class CategoriaItem implements Comparable<CategoriaItem> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80)
    private String nomeCategoria;

    @Override
    public int compareTo(@NotNull CategoriaItem o) {
        return nomeCategoria.compareTo(o.nomeCategoria);
    }
}
