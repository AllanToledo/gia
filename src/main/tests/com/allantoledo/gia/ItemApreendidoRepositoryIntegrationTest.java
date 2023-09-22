package com.allantoledo.gia;

import com.allantoledo.gia.data.entity.ItemApreendido;
import com.allantoledo.gia.data.repository.ItemApreendidoRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemApreendidoRepositoryIntegrationTest {

    @Autowired
    ItemApreendidoRepository itemApreendidoRepository;

    Validator validator;

    @Before
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void whenInsertMap_thenFindTheSameMap() {
        ItemApreendido itemApreendido = new ItemApreendido();
        itemApreendido.setEstadoDoObjeto(ItemApreendido.EstadoDoObjeto.EM_DEPOSITO);
        itemApreendido.setDataApreensao(LocalDate.now());
        itemApreendido.setDescricao(new HashMap<>());
        itemApreendido.getDescricao().put("cor", "preto");
        itemApreendido = itemApreendidoRepository.save(itemApreendido);

        ItemApreendido found = itemApreendidoRepository.findById(itemApreendido.getId()).get();
        assertThat(found).isNotNull();
        assertThat(found.getDescricao()).isNotNull();
        assertThat(found.getDescricao().get("cor")).isEqualTo("preto");

    }

    @Test
    public void notAcceptInvalid_Cpf() {
        ItemApreendido itemApreendido = new ItemApreendido();
        itemApreendido.setCpfProprietario("teste");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getCpfProprietario());

        itemApreendido.setCpfProprietario("12345678911");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getCpfProprietario());

        itemApreendido.setCpfProprietario("43924232040");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getCpfProprietario());

    }

    @Test
    public void notAcceptInvalid_valorAvaliado() {
        ItemApreendido itemApreendido = new ItemApreendido();
        itemApreendido.setValorAvaliado(null);
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getValorAvaliado());

        itemApreendido.setValorAvaliado(new BigDecimal("-1"));
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getValorAvaliado());

        itemApreendido.setValorAvaliado(new BigDecimal("0"));
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getValorAvaliado());

        itemApreendido.setValorAvaliado(new BigDecimal("1"));
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getValorAvaliado());

    }

    @Test
    public void notAcceptInvalid_numeroProcesso() {
        ItemApreendido itemApreendido = new ItemApreendido();
        itemApreendido.setNumeroProcesso(null);
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getNumeroProcesso());

        itemApreendido.setNumeroProcesso("teste");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getNumeroProcesso());

        itemApreendido.setNumeroProcesso("-123123");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getNumeroProcesso());

        itemApreendido.setNumeroProcesso("12345678911");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getNumeroProcesso());

        itemApreendido.setNumeroProcesso("         ");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getNumeroProcesso());

        itemApreendido.setNumeroProcesso("1234567");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getNumeroProcesso());
        itemApreendido.setNumeroProcesso("1");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getNumeroProcesso());
        itemApreendido.setNumeroProcesso("1234567890");
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getNumeroProcesso());
    }

    @Test
    public void notAcceptInvalid_dataApreensao() {
        ItemApreendido itemApreendido = new ItemApreendido();
        itemApreendido.setDataApreensao(null);
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getDataApreensao());

        itemApreendido.setDataApreensao(LocalDate.now().plus(1, ChronoUnit.DAYS));
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .contains(itemApreendido.getDataApreensao());

        itemApreendido.setDataApreensao(LocalDate.now().plus(-1, ChronoUnit.DAYS));
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getDataApreensao());


        itemApreendido.setDataApreensao(LocalDate.now());
        assertThat(validator.validate(itemApreendido)
                .stream()
                .map(ConstraintViolation::getInvalidValue))
                .doesNotContain(itemApreendido.getDataApreensao());
    }


}
