package com.allantoledo.gia.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80)
    @Column(columnDefinition = "varchar(80) default ''")
    private String logradouro;
    @Size(max=80)
    @Column(columnDefinition = "varchar(80) default ''")
    private String bairro;
    @Size(max=80)
    @Column(columnDefinition = "varchar(80) default ''")
    private String cidade;
    @Size(max=2)
    @Column(columnDefinition = "varchar(2) default ''")
    private String estado;
    @Size(max=8)
    @Column(columnDefinition = "varchar(8) default ''")
    private String cep;
    @Size(max=10)
    @Column(columnDefinition = "varchar(10) default ''")
    private String numero;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Endereco endereco = (Endereco) o;
        return getId() != null && Objects.equals(getId(), endereco.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
