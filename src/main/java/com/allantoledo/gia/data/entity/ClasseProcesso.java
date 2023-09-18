package com.allantoledo.gia.data.entity;


import com.github.javaparser.quality.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
public class ClasseProcesso implements Comparable<ClasseProcesso> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80)
    private String nomeClasse;

    @Override
    public int compareTo(@NotNull ClasseProcesso o) {
        return nomeClasse.compareTo(o.nomeClasse);
    }
}
