package com.allantoledo.gia.data.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ClasseProcesso extends AbstractEntity {
    private String nomeClasse;
}
