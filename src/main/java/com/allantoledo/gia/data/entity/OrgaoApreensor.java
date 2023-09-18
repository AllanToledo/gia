package com.allantoledo.gia.data.entity;


import com.github.javaparser.quality.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
public class OrgaoApreensor implements Comparable<OrgaoApreensor> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80)
    private String nome;

    @Override
    public int compareTo(@NotNull OrgaoApreensor o) {
        return nome.compareTo(o.nome);
    }
}
