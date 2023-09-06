package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.data.converter.JpaConverterJson;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import elemental.json.JsonType;
import jakarta.persistence.*;
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
    private String numeroProcesso;
    private Date dataApreensao;
    private BigDecimal valorAvaliado;
    private String nomeItem;

    @Convert(converter = JpaConverterJson.class)
    private Map<String, String> descricao;
    private String cpfProprietario;
    private String estadoDoObjeto;

    @ManyToOne()
    private Deposito deposito;
    @ManyToOne()
    private OrgaoDestino orgaoDestino;
    @ManyToOne()
    private OrgaoApreensor orgaoApreensor;

    @OneToMany(mappedBy = "itemApreendido")
    @Size(min=0, max=10)
    private Set<Historico> historicos;

    @ManyToMany
    private Set<CategoriaItem> categorias;
    @ManyToMany
    private Set<ClasseProcesso> classes;

}
