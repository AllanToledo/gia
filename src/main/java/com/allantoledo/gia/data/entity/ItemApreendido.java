package com.allantoledo.gia.data.entity;

import com.allantoledo.gia.data.converter.JpaConverterJson;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import elemental.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class ItemApreendido extends AbstractEntity {
    private String numeroProcesso;
    private Date dataApreensao;
    private BigDecimal valorAvaliado;

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
    private List<Historico> historicos;

}
