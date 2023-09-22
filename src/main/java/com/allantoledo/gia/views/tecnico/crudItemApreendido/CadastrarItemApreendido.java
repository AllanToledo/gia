package com.allantoledo.gia.views.tecnico.crudItemApreendido;

import com.allantoledo.gia.data.entity.*;
import com.allantoledo.gia.data.service.*;
import com.allantoledo.gia.security.AuthenticatedUser;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@PageTitle("Items Apreendidos")
@Route(value = "itemsapreendidos/update", layout = MainLayout.class)
@RolesAllowed("TECNICO")
@Uses(Icon.class)
public class CadastrarItemApreendido extends VerticalLayout implements HasUrlParameter<Long> {

    final ItemApreendidoService itemApreendidoService;
    final ClasseProcessoService classeProcessoService;
    final CategoriaItemService categoriaItemService;
    final OrgaoApreensorService orgaoApreensorService;
    final OrgaoDestinoService orgaoDestinoService;
    final DepositoService depositoService;
    final UsuarioService usuarioService;
    final HistoricoService historicoService;

    final AuthenticatedUser authenticatedUser;
    final Validator validator;

    ItemApreendido itemApreendidoCadastrado;
    boolean inicializado = false;

    public CadastrarItemApreendido(ItemApreendidoService itemApreendidoService,
                                   ClasseProcessoService classeProcessoService,
                                   CategoriaItemService categoriaItemService,
                                   OrgaoApreensorService orgaoApreensorService,
                                   OrgaoDestinoService orgaoDestinoService,
                                   DepositoService depositoService,
                                   UsuarioService usuarioService,
                                   HistoricoService historicoService,
                                   Validator validator,
                                   AuthenticatedUser authenticatedUser) {
        this.itemApreendidoService = itemApreendidoService;
        this.classeProcessoService = classeProcessoService;
        this.categoriaItemService = categoriaItemService;
        this.orgaoApreensorService = orgaoApreensorService;
        this.orgaoDestinoService = orgaoDestinoService;
        this.depositoService = depositoService;
        this.usuarioService = usuarioService;
        this.historicoService = historicoService;
        this.validator = validator;
        this.authenticatedUser = authenticatedUser;
    }

    private final ComponentRenderer<Component, Historico> historicoComponentRenderer = new ComponentRenderer<>(
            historico -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setPadding(true);
                cardLayout.addClassNames(
                        LumoUtility.Background.CONTRAST_5,
                        LumoUtility.BorderRadius.MEDIUM,
                        LumoUtility.Margin.Vertical.SMALL
                );
                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.add(new Div(new H5("ESTADO ANTERIOR")));
                if (historico.getEstadoAnterior() != null)
                    infoLayout.add(new Div(new Text(historico.getEstadoAnterior())));

                infoLayout.add(new Div(new H5("ESTADO NOVO")));
                infoLayout.add(new Div(new Text(historico.getEstadoNovo())));
                infoLayout.add(new Div(new H5("USUARIO")));
                infoLayout.add(new Div(new Text(historico.getUsuario().getNome())));
                infoLayout.add(new Div(new H5("HORARIO DA ALTERACAO")));
                infoLayout.add(new Div(new Text(historico.getHorarioAlteracao().format(DateTimeFormatter.ISO_DATE_TIME))));

                cardLayout.add(infoLayout);
                return cardLayout;
            });

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            itemApreendidoCadastrado = itemApreendidoService.get(id).orElse(null);
        }
        if (!inicializado) carregarTela();
    }

    void carregarTela() {
        inicializado = true;
        TextField numeroProcessoField = new TextField("NUMERO DO PROCESSO");
        numeroProcessoField.setHelperText("Número do processo onde foi efetuado a apreensão do objeto.");
        DatePicker dataApreensaoPicker = new DatePicker("DATA APREENSÃO");
        BigDecimalField valorAvaliadoField = new BigDecimalField("VALOR AVALIADO");
        valorAvaliadoField.setHelperText("Valor avaliado pode ser em branco, mas não pode ser negativo.");
        valorAvaliadoField.setPrefixComponent(new Div(new Text("R$")));

        TextField cpfProprietario = new TextField("CPF DO PROPRIETARIO (APENAS OS DIGITOS)");
        cpfProprietario.setHelperText("Se não for identificado, o campo deve ficar em branco.");

        ComboBox<OrgaoApreensor> orgaoApreensorSelect = new ComboBox<>();
        orgaoApreensorSelect.setLabel("ORGAO APREENSOR");
        orgaoApreensorSelect.setItems(orgaoApreensorService.list(Pageable.unpaged()).stream().sorted().toList());
        orgaoApreensorSelect.setItemLabelGenerator(OrgaoApreensor::getNome);
        orgaoApreensorSelect.setHelperText("Selecione a organização responsavel pela apreensão.");

        ComboBox<OrgaoDestino> orgaoDestinoSelect = new ComboBox<>();
        orgaoDestinoSelect.setLabel("ORGANIZACAO DE DESTINO");
        orgaoDestinoSelect.setHelperText("Deve ficar em branco caso o objeto esteja em depósito ou foi reconstituido.");
        orgaoDestinoSelect.setItemLabelGenerator(OrgaoDestino::getNome);

        ComboBox<Deposito> depositoSelect = new ComboBox<>();
        depositoSelect.setLabel("DEPOSITO");
        depositoSelect.setHelperText("Preencha caso o item esteja em algum depósito.");
        depositoSelect.setItemLabelGenerator(Deposito::getNome);

        MultiSelectComboBox<CategoriaItem> categoriaItemSelect = new MultiSelectComboBox<>();
        categoriaItemSelect.setLabel("CATEGORIA DO ITEM");
        categoriaItemSelect.setItems(categoriaItemService.list(Pageable.unpaged()).stream().sorted().toList());
        categoriaItemSelect.setItemLabelGenerator(CategoriaItem::getNomeCategoria);

        MultiSelectComboBox<ClasseProcesso> classeProcessoSelect = new MultiSelectComboBox<>();
        classeProcessoSelect.setLabel("CLASSE DO PROCESSO");
        classeProcessoSelect.setItems(classeProcessoService.list(Pageable.unpaged()).stream().sorted().toList());
        classeProcessoSelect.setItemLabelGenerator(ClasseProcesso::getNomeClasse);

        Select<ItemApreendido.EstadoDoObjeto> estadoDoObjetoSelect = new Select<>();
        estadoDoObjetoSelect.setLabel("ESTADO DO OBJETO");
        estadoDoObjetoSelect.setItems(ItemApreendido.EstadoDoObjeto.values());
        estadoDoObjetoSelect.addValueChangeListener(selectEstadoDoObjetoComponentValueChangeEvent -> {
            if(estadoDoObjetoSelect.getValue() == ItemApreendido.EstadoDoObjeto.EM_DEPOSITO){
                depositoSelect.setItems(depositoService.list(Pageable.unpaged()).stream().sorted().toList());
                orgaoDestinoSelect.clear();
            } else if(estadoDoObjetoSelect.getValue() != ItemApreendido.EstadoDoObjeto.RECONSTITUIDO){
                orgaoDestinoSelect.setItems(orgaoDestinoService.list(Pageable.unpaged()).stream().sorted().toList());
                depositoSelect.clear();
            } else {
                depositoSelect.clear();
                orgaoDestinoSelect.clear();
            }
        });
        estadoDoObjetoSelect.setItemLabelGenerator(item -> item.name().replaceAll("_", " "));


        if (itemApreendidoCadastrado != null) {
            numeroProcessoField.setValue(itemApreendidoCadastrado.getNumeroProcesso());
            dataApreensaoPicker.setValue(itemApreendidoCadastrado.getDataApreensao());
            valorAvaliadoField.setValue(itemApreendidoCadastrado.getValorAvaliado());
            cpfProprietario.setValue(itemApreendidoCadastrado.getCpfProprietario());
            estadoDoObjetoSelect.setValue(itemApreendidoCadastrado.getEstadoDoObjeto());
            orgaoApreensorSelect.setValue(itemApreendidoCadastrado.getOrgaoApreensor());
            orgaoDestinoSelect.setValue(itemApreendidoCadastrado.getOrgaoDestino());
            depositoSelect.setValue(itemApreendidoCadastrado.getDeposito());
            categoriaItemSelect.setValue(itemApreendidoCadastrado.getCategorias());
            classeProcessoSelect.setValue(itemApreendidoCadastrado.getClasses());
        }

        FormLayout formLayout = new FormLayout();
        formLayout.add(numeroProcessoField, dataApreensaoPicker,
                valorAvaliadoField, cpfProprietario,
                categoriaItemSelect, classeProcessoSelect,
                estadoDoObjetoSelect, orgaoApreensorSelect,
                orgaoDestinoSelect, depositoSelect);

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("10cm", 2));

        VerticalLayout descricaoLayout = new VerticalLayout();
        descricaoLayout.setPadding(false);
        List<HorizontalLayout> descricaoForms = new LinkedList<>();
        Button adicionarCampo = new Button("ADICIONAR CAMPO");
        adicionarCampo.setWidthFull();
        adicionarCampo.addClickListener(buttonClickEvent -> {
            criarCampo(descricaoLayout, descricaoForms, null);
        });

        if (itemApreendidoCadastrado != null && itemApreendidoCadastrado.getDescricao() != null) {
            for (var entry : itemApreendidoCadastrado.getDescricao().entrySet()) {
                criarCampo(descricaoLayout, descricaoForms, entry);
            }
        }

        Button cancelar = new Button("CANCELAR");
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelar.addClickListener(buttonClickEvent -> {
            this.getUI().ifPresent(ui -> ui.getPage().getHistory().back());
        });

        Button cadastrar = new Button("CADASTRAR");
        cadastrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cadastrar.addClickListener(buttonClickEvent -> {

            Historico historico = new Historico();
            if (itemApreendidoCadastrado != null)
                historico.setEstadoAnterior(itemApreendidoCadastrado.toString());

            ItemApreendido itemApreendidoNovo = Objects.requireNonNullElseGet(itemApreendidoCadastrado, ItemApreendido::new);
            itemApreendidoNovo.setNumeroProcesso(numeroProcessoField.getValue());
            itemApreendidoNovo.setDataApreensao(dataApreensaoPicker.getValue());
            itemApreendidoNovo.setValorAvaliado(valorAvaliadoField.getValue());
            itemApreendidoNovo.setCpfProprietario(cpfProprietario.getValue());
            itemApreendidoNovo.setEstadoDoObjeto(estadoDoObjetoSelect.getValue());
            itemApreendidoNovo.setOrgaoApreensor(orgaoApreensorSelect.getValue());
            itemApreendidoNovo.setOrgaoDestino(orgaoDestinoSelect.getValue());
            itemApreendidoNovo.setDeposito(depositoSelect.getValue());
            itemApreendidoNovo.setCategorias(categoriaItemSelect.getSelectedItems());
            itemApreendidoNovo.setClasses(classeProcessoSelect.getSelectedItems());

            itemApreendidoNovo.setDescricao(new HashMap<>());
            for (var horizontalLayout : descricaoForms) {
                var forms = (FormLayout) horizontalLayout.getComponentAt(1);
                String chave = ((TextField) forms.getChildren().toList().get(0)).getValue();
                String valor = ((TextField) forms.getChildren().toList().get(1)).getValue();
                itemApreendidoNovo.getDescricao().put(chave, valor);
            }

            Set<ConstraintViolation<ItemApreendido>> violations = validator.validate(itemApreendidoNovo);
            for (var violation : violations) {
                Notification.show(violation.getMessage());
            }

            if (!violations.isEmpty()) return;

            Usuario usuarioLogado = authenticatedUser.get().orElseThrow();

            historico.setHorarioAlteracao(LocalDateTime.now());
            historico.setEstadoNovo(itemApreendidoNovo.toString());
            historico.setUsuario(usuarioLogado);

            historicoService.update(historico);
            if (itemApreendidoNovo.getHistoricos() == null)
                itemApreendidoNovo.setHistoricos(new HashSet<>());
            itemApreendidoNovo.getHistoricos().add(historico);
            itemApreendidoService.update(itemApreendidoNovo);

            if (itemApreendidoCadastrado == null) {
                Notification.show("Item Apreendido cadastrado com sucesso!");
            } else {
                Notification.show("Item Apreendido atualizado com sucesso!");
            }

            cancelar.click();
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(cancelar, cadastrar);
        buttonsLayout.setWidthFull();
        buttonsLayout.setJustifyContentMode(JustifyContentMode.END);

        VerticalLayout ajudaContent = new VerticalLayout();
        ajudaContent.add(new Text("As propriedades servem para adicionar um campo ao formulário" +
                " que pode ser especifico a um tipo de item, como por exemplo: um item que é da marca Ferrari, " +
                "onde a chave do campo é \"Marca\" e o valor do campo é \"Ferrari\".\nPode adicionar quantas propriedades forem necessárias."));
        VerticalLayout exemploLayout = new VerticalLayout();
        criarCampoExemplo(exemploLayout, Pair.of("Marca", "Ferrari"));
        ajudaContent.add(exemploLayout);
        Details ajuda = new Details("Ajuda", ajudaContent);

        setMaxWidth("16cm");
        add(new H3("Cadastrar Item Apreendido"));
        add(formLayout);
        add(new H4("Propriedades"));
        add(ajuda);
        add(descricaoLayout);
        add(adicionarCampo);
        add(buttonsLayout);

        if (itemApreendidoCadastrado == null) return;

        add(new H3("Histórico"));

        VirtualList<Historico> historicoVirtualList = new VirtualList<>();
        historicoVirtualList.setRenderer(historicoComponentRenderer);
        historicoVirtualList.setItems(itemApreendidoCadastrado.getHistoricos());
        add(historicoVirtualList);

    }

    private static void criarCampo(VerticalLayout descricaoLayout, List<HorizontalLayout> descricaoForms, Map.Entry<String, String> entry) {
        FormLayout descricaoItem = new FormLayout();
        Button remover = new Button(new Icon(VaadinIcon.CLOSE));
        TextField chave = new TextField();
        chave.setPlaceholder("CHAVE");
        TextField valor = new TextField();
        valor.setPlaceholder("VALOR");
        if (entry != null) {
            chave.setValue(entry.getKey());
            valor.setValue(entry.getValue());
        }
        descricaoItem.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        descricaoItem.add(chave, valor);

        HorizontalLayout newLayout = new HorizontalLayout(remover, descricaoItem);
        newLayout.setWidthFull();
        newLayout.setPadding(false);
        newLayout.setFlexGrow(1, descricaoItem);
        descricaoForms.add(newLayout);
        descricaoLayout.removeAll();
        for (var descricaoForm : descricaoForms)
            descricaoLayout.add(descricaoForm);
        remover.addClickListener(buttonClickEvent1 -> {
            descricaoForms.remove(newLayout);
            descricaoLayout.removeAll();
            for (var descricaoForm : descricaoForms)
                descricaoLayout.add(descricaoForm);
        });
    }

    private static void criarCampoExemplo(VerticalLayout descricaoLayout, Map.Entry<String, String> entry) {
        FormLayout descricaoItem = new FormLayout();
        TextField chave = new TextField();
        chave.setPlaceholder("CHAVE");
        TextField valor = new TextField();
        valor.setPlaceholder("VALOR");
        if (entry != null) {
            chave.setValue(entry.getKey());
            valor.setValue(entry.getValue());
        }
        descricaoItem.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        descricaoItem.add(chave, valor);
        descricaoLayout.add(descricaoItem);
    }
}
