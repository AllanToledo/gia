package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.data.converter.JpaConverterJson;
import com.allantoledo.gia.validations.NullOrCpfValid;
import com.allantoledo.gia.validations.ValidCpf;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@Table(indexes = {
        @Index(columnList = "numeroProcesso")
})
public class ItemApreendido{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min=1, max=10, message = "Número de processo precisa ser entre 1 e 10 caracteres")
    @Pattern(regexp = "[0-9]{1,10}", message = "Número do processo deve contar apenas digitos")
    private String numeroProcesso;
    @NotNull(message = "Precisa ser selecionado a data de apreensão")
    @PastOrPresent
    private LocalDate dataApreensao;
    @DecimalMin(value = "0", message = "Não é permitido valores negativos.")
    private BigDecimal valorAvaliado = BigDecimal.ZERO;

    @Convert(converter = JpaConverterJson.class)
    @Column(columnDefinition = "text")
    private Map<String, String> descricao;
    @NullOrCpfValid
    private String cpfProprietario;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Selecione um estado do objeto")
    private EstadoDoObjeto estadoDoObjeto;

    @ManyToOne()
    private Deposito deposito;
    @ManyToOne()
    private OrgaoDestino orgaoDestino;
    @ManyToOne()
    @NotNull
    private OrgaoApreensor orgaoApreensor;

    @ToString.Exclude
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="item_apreendido_id", referencedColumnName = "id")
    private Set<Historico> historicos;

    @ManyToMany(fetch=FetchType.EAGER)
    private Set<CategoriaItem> categorias;
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<ClasseProcesso> classes;

    public enum EstadoDoObjeto {
        EM_DEPOSITO,
        ENVIADO_PARA_DESTRUICAO,
        ENVIADO_PARA_DOACAO,
        ENVIADO_PARA_LEILAO,
        RECONSTITUIDO
    }

}
