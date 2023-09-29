package com.allantoledo.gia.views.gestor.crudOrganizacoes;

import com.allantoledo.gia.data.entity.OrgaoDestino;
import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.data.service.OrgaoDestinoService;
import com.allantoledo.gia.data.service.UsuarioService;
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

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Organizações")
@Route(value = "organizacoes", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class ListarOrgaosDestino extends VerticalLayout {
    final OrgaoDestinoService orgaoDestinoService;

    private String formatCpf(String cpfOrCnpj) {
        if (cpfOrCnpj.length() > 11) return cpfOrCnpj;
        return cpfOrCnpj.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "***.$2.***-**");
    }

    private final ComponentRenderer<Component, OrgaoDestino> resultCardRenderer = new ComponentRenderer<>(
            orgaoDestino -> {
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
                infoLayout.getElement().appendChild(ElementFactory.createStrong(orgaoDestino.getNome()));
                HorizontalLayout dados = new HorizontalLayout();
                dados.add(new Div(new Text(formatCpf(orgaoDestino.getCpfCnpj()))));
                dados.add(new Div(new Text(orgaoDestino.getContato())));
                Button editar = new Button("EDITAR");
                editar.addClickListener(buttonClickEvent -> editar.getUI().ifPresent(ui -> ui.navigate(CadastrarOrgaoDestino.class, orgaoDestino.getId())));
                infoLayout.add(dados);
                cardLayout.add(infoLayout, editar);
                return cardLayout;
            });


    public ListarOrgaosDestino(OrgaoDestinoService orgaoDestinoService) {
        this.orgaoDestinoService = orgaoDestinoService;
        VirtualList<OrgaoDestino> results = new VirtualList<>();
        results.setHeightFull();
        results.setRenderer(resultCardRenderer);
        results.setItems(orgaoDestinoService.list(Pageable.unpaged()).toList());
        AtomicReference<Pageable> resultsPage = new AtomicReference<>(PageRequest.of(0, 10));
        FormLayout formLayout = new FormLayout();

        TextField searchField = new TextField();
        searchField.setPlaceholder("Pesquisar por nome");
        Button searchButton = new Button("PESQUISAR");
        searchButton.addClickShortcut(Key.ENTER);

        PaginationComponent paginationLayout = new PaginationComponent(resultsPage.get(), (page) -> {
            resultsPage.set(page);
            results.setItems(
                    orgaoDestinoService.list(resultsPage.get(),
                                    OrgaoDestinoService.OrgaoDestinoSpecification.filterByName(searchField.getValue()))
                            .stream());
            return null;
        });

        searchButton.addClickListener(buttonClickEvent -> {
            resultsPage.set(PageRequest.of(0, 10));
            paginationLayout.resetPage(resultsPage.get());
            results.setItems(
                    orgaoDestinoService.list(resultsPage.get(),
                                    OrgaoDestinoService.OrgaoDestinoSpecification.filterByName(searchField.getValue()))
                            .stream()
            );
        });



        formLayout.add(searchField, searchButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("10cm", 3));
        formLayout.setColspan(searchField, 2);

        Button criarNovaOrganizacao = new Button(new Icon(VaadinIcon.PLUS));
        criarNovaOrganizacao.setTooltipText("Criar nova organização");
        criarNovaOrganizacao.addClickListener(buttonClickEvent -> criarNovaOrganizacao.getUI().ifPresent(ui -> ui.navigate(CadastrarOrgaoDestino.class)));


        HorizontalLayout header = new HorizontalLayout();
        header.add(new H3("Pesquisar Organizações de Destino"), criarNovaOrganizacao);
        header.setAlignItems(Alignment.CENTER);
        setMaxWidth("16cm");
        add(header);
        add(formLayout);
        add(paginationLayout);
        add(results);
    }


}
