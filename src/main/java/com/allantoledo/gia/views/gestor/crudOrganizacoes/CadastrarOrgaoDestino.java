package com.allantoledo.gia.views.gestor.crudOrganizacoes;

import com.allantoledo.gia.data.entity.CategoriaItem;
import com.allantoledo.gia.data.entity.Endereco;
import com.allantoledo.gia.data.entity.OrgaoDestino;
import com.allantoledo.gia.data.external.ConsumeEnderecoApi;
import com.allantoledo.gia.data.external.EnderecoApi;
import com.allantoledo.gia.data.service.CategoriaItemService;
import com.allantoledo.gia.data.service.EnderecoService;
import com.allantoledo.gia.data.service.OrgaoDestinoService;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Pageable;

import java.util.Objects;
import java.util.Set;

@PageTitle("Organizações")
@Route(value = "organizacoes/update", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class CadastrarOrgaoDestino extends VerticalLayout implements HasUrlParameter<Long> {
    final OrgaoDestinoService orgaoDestinoService;
    final EnderecoService enderecoService;
    final CategoriaItemService categoriaItemService;
    final Validator validator;
    OrgaoDestino organizacaoCadastrada;
    final ConsumeEnderecoApi consumeEnderecoApi;

    CadastrarOrgaoDestino(OrgaoDestinoService orgaoDestinoService,
                          EnderecoService enderecoService,
                          Validator validator,
                          ConsumeEnderecoApi consumeEnderecoApi,
                          CategoriaItemService categoriaItemService) {
        this.orgaoDestinoService = orgaoDestinoService;
        this.enderecoService = enderecoService;
        this.validator = validator;
        this.consumeEnderecoApi = consumeEnderecoApi;
        this.categoriaItemService = categoriaItemService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter != null) {
            organizacaoCadastrada = orgaoDestinoService.get(parameter).orElse(null);
        }
        carregarTela();
    }

    public void carregarTela() {
        TextField nomeField = new TextField("NOME DA ORGANIZAÇÃO DE DESTINO");
        TextField contatoField = new TextField("CONTATO");
        TextField cpfCnpjField = new TextField("CPF/CNPJ");

        MultiSelectComboBox<CategoriaItem> categoriasDeItemAceitas = new MultiSelectComboBox<>(
                "CATEGORIAS DE ITEMS ACEITAS");
        categoriasDeItemAceitas.setItems(categoriaItemService.list(Pageable.unpaged()).stream().toList());
        categoriasDeItemAceitas.setItemLabelGenerator(CategoriaItem::getNomeCategoria);

        FormLayout depositoForm = new FormLayout();
        depositoForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("10cm", 2));
        depositoForm.add(nomeField, contatoField, cpfCnpjField, categoriasDeItemAceitas);
        depositoForm.setColspan(nomeField, 2);

        TextField cepField = new TextField("CEP (Somente os números)");
        TextField logradouroField = new TextField("LOGRADOURO");
        TextField numeroField = new TextField("NUMERO");
        TextField bairroField = new TextField("BAIRRO");
        TextField cidadeField = new TextField("CIDADE");
        cidadeField.setReadOnly(true);
        TextField estadoField = new TextField("ESTADO");
        estadoField.setReadOnly(true);

        cepField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
            cepField.setValue(cepField.getValue().replaceAll("[^0-9]", ""));
            if (cepField.getValue().length() != 8) return;
            EnderecoApi enderecoApi = consumeEnderecoApi.loadCep(cepField.getValue());
            if(enderecoApi == null) return;
            logradouroField.setValue(enderecoApi.logradouro());
            bairroField.setValue(enderecoApi.bairro());
            cidadeField.setValue(enderecoApi.localidade());
            estadoField.setValue(enderecoApi.uf());
        });

        FormLayout enderecoForm = new FormLayout();
        enderecoForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("10cm", 2));
        enderecoForm.add(cepField, logradouroField, numeroField, bairroField, cidadeField, estadoField);

        if (organizacaoCadastrada != null) {
            nomeField.setValue(organizacaoCadastrada.getNome());
            contatoField.setValue(organizacaoCadastrada.getContato());
            cpfCnpjField.setValue(organizacaoCadastrada.getCpfCnpj());
            categoriasDeItemAceitas.setValue(organizacaoCadastrada.getCategoriasAceitas());

            cepField.setValue(organizacaoCadastrada.getEndereco().getCep());
            logradouroField.setValue(organizacaoCadastrada.getEndereco().getLogradouro());
            numeroField.setValue(organizacaoCadastrada.getEndereco().getNumero());
            bairroField.setValue(organizacaoCadastrada.getEndereco().getBairro());
            cidadeField.setValue(organizacaoCadastrada.getEndereco().getCidade());
            estadoField.setValue(organizacaoCadastrada.getEndereco().getEstado());
        }



        Button cancelar = new Button("Cancelar");
        cancelar.addClickListener(buttonClickEvent -> {
            nomeField.clear();
            contatoField.clear();
            cpfCnpjField.clear();
            categoriasDeItemAceitas.clear();

            cepField.clear();
            logradouroField.clear();
            numeroField.clear();
            bairroField.clear();
            cidadeField.clear();
            estadoField.clear();

            UI.getCurrent().getPage().getHistory().back();
        });

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Deletar Organização");
        dialog.setText("Você deseja deletar essa organização?");
        dialog.setCancelable(true);
        dialog.setCancelText("Cancelar");
        Button confimarDelete = new Button("DELETAR");
        confimarDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        confimarDelete.addClickListener(event -> {
            orgaoDestinoService.delete(organizacaoCadastrada.getId());
            cancelar.click();
        });
        dialog.setConfirmButton(confimarDelete);

        Button deletar = new Button("Deletar");
        deletar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deletar.addClickListener(buttonClickEvent -> dialog.open());

        Button cadastrar = new Button("Cadastrar");
        cadastrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cadastrar.addClickListener(buttonClickEvent -> {
            OrgaoDestino depositoNovo = Objects.requireNonNullElseGet(organizacaoCadastrada, OrgaoDestino::new);
            depositoNovo.setNome(nomeField.getValue());
            depositoNovo.setContato(contatoField.getValue());
            depositoNovo.setCpfCnpj(cpfCnpjField.getValue());

            Endereco enderecoDeposito = Objects.requireNonNullElseGet(depositoNovo.getEndereco(), Endereco::new);
            enderecoDeposito.setCep(cepField.getValue());
            enderecoDeposito.setNumero(numeroField.getValue());
            enderecoDeposito.setLogradouro(logradouroField.getValue());
            enderecoDeposito.setBairro(bairroField.getValue());
            enderecoDeposito.setCidade(cidadeField.getValue());
            enderecoDeposito.setEstado(estadoField.getValue());

            Set<ConstraintViolation<Endereco>> enderecoViolations = validator.validate(enderecoDeposito);
            for (ConstraintViolation<Endereco> violation : enderecoViolations) {
                Notification.show(violation.getMessage());
            }

            depositoNovo.setCategoriasAceitas(categoriasDeItemAceitas.getSelectedItems());
            depositoNovo.setEndereco(enderecoDeposito);

            Set<ConstraintViolation<OrgaoDestino>> depositoViolations = validator.validate(depositoNovo);
            for (ConstraintViolation<OrgaoDestino> violation : depositoViolations) {
                Notification.show(violation.getMessage());
            }

            if (!enderecoViolations.isEmpty()) return;
            if (!depositoViolations.isEmpty()) return;

            depositoNovo.setEndereco(enderecoService.update(enderecoDeposito));
            orgaoDestinoService.update(depositoNovo);

            if (organizacaoCadastrada == null) {
                Notification.show("Depósito cadastrado com sucesso!");
            } else {
                Notification.show("Depósito atualizado com sucesso!");
            }

            cancelar.click();

        });

        setMaxWidth("16cm");
        add(new H3("Cadastrar Organização de Destino"));
        add(depositoForm);
        add(new H4("Endereço"));
        add(enderecoForm);

        var buttonLayout = new HorizontalLayout();
        if (organizacaoCadastrada != null) buttonLayout.add(deletar);
        buttonLayout.add(cancelar, cadastrar);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        add(buttonLayout);

    }


}
