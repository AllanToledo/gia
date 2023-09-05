package com.allantoledo.gia.data.entity;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgaoApreensor extends AbstractEntity {
    private String nome;

}
