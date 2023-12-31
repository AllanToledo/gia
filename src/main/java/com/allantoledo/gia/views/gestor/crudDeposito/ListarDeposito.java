package com.allantoledo.gia.views.gestor.crudDeposito;

import com.allantoledo.gia.data.entity.Deposito;
import com.allantoledo.gia.data.service.DepositoService;
import com.allantoledo.gia.views.MainLayout;
import com.allantoledo.gia.views.componentes.PaginationComponent;
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

import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Depósitos")
@Route(value = "depositos", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class ListarDeposito extends VerticalLayout {
    final DepositoService depositoService;

    private String formatCpfCnpj(String cpfOrCnpj) {
        if (cpfOrCnpj.length() > 11) {
            return cpfOrCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        } else {
            return cpfOrCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
    }

    private String formatContato(String contato) {
        return contato.replaceAll("(\\d{2})(\\d{5})(\\d{0,4}).*", "($1) $2-$3");
    }

    private final ComponentRenderer<Component, Deposito> resultCardRenderer = new ComponentRenderer<>(
            deposito -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                //cardLayout.setMargin(true);
                cardLayout.setPadding(true);
                cardLayout.addClassNames(
                        LumoUtility.Background.CONTRAST_5,
                        LumoUtility.BorderRadius.MEDIUM,
                        LumoUtility.Margin.Vertical.SMALL
                );
                //cardLayout.setWidthFull();
                cardLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
                cardLayout.setAlignItems(Alignment.CENTER);

                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.getElement().appendChild(ElementFactory.createStrong(deposito.getNome()));
                HorizontalLayout dados = new HorizontalLayout();
                dados.add(new Div(new Text(formatCpfCnpj(deposito.getCpfCnpj()))));
                dados.add(new Div(new Text(formatContato(deposito.getContato()))));
                Button editar = new Button("EDITAR");
                editar.addClickListener(buttonClickEvent -> editar.getUI().ifPresent(ui -> ui.navigate(CadastrarDeposito.class, deposito.getId())));
                infoLayout.add(dados);
                cardLayout.add(infoLayout, editar);
                return cardLayout;
            });


    public ListarDeposito(DepositoService depositoService) {
        this.depositoService = depositoService;
        VirtualList<Deposito> results = new VirtualList<>();
        results.setHeightFull();
        results.setRenderer(resultCardRenderer);

        AtomicReference<Pageable> resultsPage = new AtomicReference<>(PageRequest.of(0, 10));

        results.setItems(depositoService.list(Pageable.unpaged()).toList());
        FormLayout formLayout = new FormLayout();
        TextField searchField = new TextField();
        searchField.setPlaceholder("Pesquisar por nome");
        Button searchButton = new Button("PESQUISAR");
        searchButton.addClickShortcut(Key.ENTER);

        PaginationComponent paginationLayout = new PaginationComponent(resultsPage.get(), (nextPage) -> {
            resultsPage.set(nextPage);
            results.setItems(depositoService.list(resultsPage.get(),
                            DepositoService.DepositoSpecification.filterByName(searchField.getValue()))
                    .stream());
            return null;
        });

        searchButton.addClickListener(buttonClickEvent -> {
            resultsPage.set(PageRequest.of(0, 10));
            paginationLayout.resetPage(resultsPage.get());
            results.setItems(depositoService.list(resultsPage.get(),
                            DepositoService.DepositoSpecification.filterByName(searchField.getValue()))
                    .stream());
        });

        formLayout.add(searchField, searchButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("10cm", 3));
        formLayout.setColspan(searchField, 2);

        Button criarNovoDeposito = new Button(new Icon(VaadinIcon.PLUS));
        criarNovoDeposito.setTooltipText("Criar novo depósito");
        criarNovoDeposito.addClickListener(buttonClickEvent -> criarNovoDeposito.getUI().ifPresent(ui -> ui.navigate(CadastrarDeposito.class)));


        HorizontalLayout header = new HorizontalLayout();
        header.add(new H3("Pesquisar Depósitos"), criarNovoDeposito);
        header.setAlignItems(Alignment.CENTER);
        setHeightFull();
        setMaxWidth("16cm");
        add(header);
        add(formLayout);
        add(paginationLayout);
        add(results);
    }
}
