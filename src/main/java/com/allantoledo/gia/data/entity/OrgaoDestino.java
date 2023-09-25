package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.validations.ValidCpfOrCnpj;
import com.github.javaparser.quality.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrgaoDestino implements Comparable<OrgaoDestino> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80)
    private String nome;
    @Size(max=80)
    private String contato;
    @ValidCpfOrCnpj
    private String cpfCnpj;
    @OneToOne
    private Endereco endereco;
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<CategoriaItem> categoriasAceitas;
    @Override
    public int compareTo(@NotNull OrgaoDestino o) {
        return nome.compareTo(o.nome);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrgaoDestino that = (OrgaoDestino) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
