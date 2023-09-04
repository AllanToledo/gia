package com.allantoledo.gia.data.entity;


import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class OrgaoApreensor extends AbstractEntity {
    private String nome;

}
