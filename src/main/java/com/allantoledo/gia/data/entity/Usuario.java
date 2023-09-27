package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.data.validations.ValidCpf;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;


@Entity
@Table(name = "usuario", indexes = {
        @Index(columnList = "cpf", unique = true)
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ValidCpf
    private String cpf;

    @Size(min=3, max=80, message = "Nome do usuário deve ter entre 3 e 80 caracteres")
    private String nome;

    @Column(columnDefinition = "boolean default true")
    private Boolean ativado;

    @JsonIgnore
    private String senhaCriptografada;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        TECNICO, GESTOR
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuario usuario = (Usuario) o;
        return getId() != null && Objects.equals(getId(), usuario.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
