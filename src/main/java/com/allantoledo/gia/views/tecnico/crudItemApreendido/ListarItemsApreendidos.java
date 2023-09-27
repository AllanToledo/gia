package com.allantoledo.gia.views.tecnico.crudItemApreendido;

import com.allantoledo.gia.data.entity.CategoriaItem;
import com.allantoledo.gia.data.entity.ItemApreendido;
import com.allantoledo.gia.data.service.ItemApreendidoService;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

@PageTitle("Depositos")
@Route(value = "apreensoes", layout = MainLayout.class)
@RolesAllowed("TECNICO")
@Uses(Icon.class)
public class ListarItemsApreendidos extends VerticalLayout {
    final ItemApreendidoService itemApreendidoService;

    private String formatCpf(String cpf) {
        if (cpf == null) return "";
        if (cpf.length() > 11) return cpf;
        return cpf.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "***.$2.***-**");
    }

    public static String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

    private final ComponentRenderer<Component, ItemApreendido> resultCardRenderer = new ComponentRenderer<>(
            itemApreendido -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setPadding(true);
                cardLayout.addClassNames(
                        LumoUtility.Background.CONTRAST_5,
                        LumoUtility.BorderRadius.MEDIUM,
                        LumoUtility.Margin.Vertical.SMALL
                );
                cardLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
                cardLayout.setAlignItems(Alignment.CENTER);

                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.getElement().appendChild(ElementFactory.createStrong(itemApreendido.getNumeroProcesso()));
                infoLayout.add(new Div(new Text("Data da Apreensão " + itemApreendido.getDataApreensao().format(DateTimeFormatter.ISO_DATE))));
                if (itemApreendido.getCpfProprietario() != null && itemApreendido.getCpfProprietario().length() != 0)
                    infoLayout.add(new Div(new Text(formatCpf(itemApreendido.getCpfProprietario()))));

                infoLayout.add(new Div(new Text(itemApreendido.getCategorias()
                        .stream()
                        .map(CategoriaItem::getNomeCategoria)
                        .reduce("", (result, categoriaItem) -> result + categoriaItem + " "))));
                if (itemApreendido.getValorAvaliado() != null)
                    infoLayout.add(new Div(new Text("Item Avaliado em " + currencyFormat(itemApreendido.getValorAvaliado()))));

                Button editar = new Button("EDITAR");
                editar.addClickListener(buttonClickEvent -> editar.getUI().ifPresent(ui -> ui.navigate(CadastrarItemApreendido.class, itemApreendido.getId())));
                cardLayout.add(infoLayout, editar);
                return cardLayout;
            });


    public ListarItemsApreendidos(ItemApreendidoService itemApreendidoService) {
        this.itemApreendidoService = itemApreendidoService;
        VirtualList<ItemApreendido> results = new VirtualList<>();
        results.setRenderer(resultCardRenderer);
        Pageable resultsPage = PageRequest.of(0, 10);
        results.setItems(
                itemApreendidoService.list(resultsPage)
                        .stream()
        );
        FormLayout formLayout = new FormLayout();
        TextField searchField = new TextField();
        searchField.setPlaceholder("Pesquisar por processo");
        Button searchButton = new Button("PESQUISAR");
        searchButton.addClickShortcut(Key.ENTER);
        searchButton.addClickListener(buttonClickEvent -> results.setItems(
                itemApreendidoService.list(resultsPage,
                                ItemApreendidoService.ItemApreendidoSpecification.filterByProcess(searchField.getValue()))
                        .stream()
        ));

        formLayout.add(searchField, searchButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("10cm", 3));
        formLayout.setColspan(searchField, 2);

        Button criarNovaApreensao = new Button(new Icon(VaadinIcon.PLUS));
        criarNovaApreensao.setTooltipText("Criar nova apreensão");
        criarNovaApreensao.addClickListener(buttonClickEvent -> criarNovaApreensao.getUI().ifPresent(ui -> ui.navigate(CadastrarItemApreendido.class)));

        HorizontalLayout header = new HorizontalLayout();
        header.add(new H3("Pesquisar Apreensões"), criarNovaApreensao);
        header.setAlignItems(Alignment.CENTER);

        setMaxWidth("16cm");
        add(header);
        add(formLayout);
        add(results);
    }
}
