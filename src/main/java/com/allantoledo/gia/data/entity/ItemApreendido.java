package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.data.converter.JpaConverterJson;
import com.allantoledo.gia.validations.ValidCpf;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import elemental.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@Table(indexes = {
        @Index(columnList = "numeroProcesso"),
        @Index(columnList = "nomeItem")
})
public class ItemApreendido{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=20)
    private String numeroProcesso;
    private Date dataApreensao;
    @DecimalMin(value = "0")
    private BigDecimal valorAvaliado;
    @Size(max=80)
    private String nomeItem;

    @Convert(converter = JpaConverterJson.class)
    private Map<String, String> descricao;
    @ValidCpf
    private String cpfProprietario;
    @Enumerated(EnumType.STRING)
    private EstadoDoObjeto estadoDoObjeto;

    @ManyToOne()
    private Deposito deposito;
    @ManyToOne()
    private OrgaoDestino orgaoDestino;
    @ManyToOne()
    private OrgaoApreensor orgaoApreensor;

    @OneToMany(mappedBy = "itemApreendido")
    @Size(max=10)
    private Set<Historico> historicos;

    @ManyToMany
    private Set<CategoriaItem> categorias;
    @ManyToMany
    private Set<ClasseProcesso> classes;

    public enum EstadoDoObjeto {
        EM_DEPOSITO,
        ENVIADO_PARA_DESTRUICAO,
        ENVIADO_PARA_DOACAO,
        ENVIADO_PARA_LEILAO,
        RECONSTITUIDO
    }

}
