package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.data.validations.ValidCpfOrCnpj;
import com.github.javaparser.quality.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
public class Deposito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=80, message = "Nome do deposito não deve exceder 80 caracteres")
    private String nome;
    @Size(max=11, message = "Contato não deve exceder 11 caracteres")
    @Pattern(regexp = "[0-9]{10,11}", message = "Contato deve conter entre 10 e 11 digitos")
    private String contato;
    @ValidCpfOrCnpj
    private String cpfCnpj;
    private boolean depositarioFiel;
    @OneToOne
    private Endereco endereco;
    @ManyToMany(fetch=FetchType.EAGER)
    @Size(min=1, message = "Selecione pelo menos uma categoria")
    private Set<CategoriaItem> categoriasAceitas;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Deposito deposito = (Deposito) o;
        return getId() != null && Objects.equals(getId(), deposito.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
