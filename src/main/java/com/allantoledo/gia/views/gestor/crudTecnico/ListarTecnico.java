package com.allantoledo.gia.views.gestor.crudTecnico;

import com.allantoledo.gia.data.entity.Usuario;
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

@PageTitle("Tecnicos")
@Route(value = "tecnicos", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class ListarTecnico extends VerticalLayout {
    UsuarioService usuarioService;

    VirtualList<Usuario> results = new VirtualList<>();

    TextField searchField = new TextField();

    private String formatCpf(String cpf) {
        return cpf.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "***.$2.***-**");
    }

    private final ComponentRenderer<Component, Usuario> resultCardRenderer = new ComponentRenderer<>(
            usuario -> {
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
                infoLayout.getElement().appendChild(ElementFactory.createStrong(usuario.getNome()));
                HorizontalLayout dados = new HorizontalLayout();
                dados.add(new Div(new Text(formatCpf(usuario.getCpf()))));
                dados.add(new Div(new Text(usuario.getRole().toString())));
                dados.add(new Div(new Text(usuario.getAtivado() ? "ATIVO" : "DESATIVADO")));
                Button desativar = new Button(usuario.getAtivado() ? "DESATIVAR" : "ATIVAR");
                desativar.addClickListener(buttonClickEvent -> {
                    usuario.setAtivado(!usuario.getAtivado());
                    usuarioService.update(usuario);
                    results.setItems(usuarioService.list(Pageable.ofSize(10),
                            UsuarioService.UsuarioSpecification.filterTecnicoByName(searchField.getValue())).stream().sorted(Comparator.comparing(Usuario::getNome)));
                });
                Button editar = new Button("EDITAR");
                editar.addClickListener(buttonClickEvent -> editar.getUI().ifPresent(ui -> ui.navigate(CadastrarTecnico.class, usuario.getId())));
                infoLayout.add(dados);
                cardLayout.add(infoLayout, desativar, editar);
                return cardLayout;
            });


    public ListarTecnico(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        results.setRenderer(resultCardRenderer);
        results.setHeightFull();
        setHeightFull();
        AtomicReference<Pageable> resultsPage = new AtomicReference<>(PageRequest.of(0, 10));

        results.setItems(usuarioService.list(resultsPage.get()).stream().sorted(Comparator.comparing(Usuario::getNome)));
        FormLayout formLayout = new FormLayout();
        searchField.setPlaceholder("Pesquisar por nome");

        PaginationComponent paginationLayout = new PaginationComponent(resultsPage.get(), (page) -> {
            resultsPage.set(page);
            results.setItems(usuarioService.list(resultsPage.get(),
                            UsuarioService.UsuarioSpecification.filterTecnicoByName(searchField.getValue()))
                    .stream().sorted(Comparator.comparing(Usuario::getNome)));
            return null;
        });

        Button searchButton = new Button("PESQUISAR");
        searchButton.addClickShortcut(Key.ENTER);
        searchButton.addClickListener(buttonClickEvent -> {
            resultsPage.set(PageRequest.of(0, 10));
            paginationLayout.resetPage(resultsPage.get());
            results.setItems(
                    usuarioService.list(resultsPage.get(),
                                    UsuarioService.UsuarioSpecification.filterTecnicoByName(searchField.getValue()))
                            .stream().sorted(Comparator.comparing(Usuario::getNome))
            );
        });

        formLayout.add(searchField, searchButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("10cm", 3));
        formLayout.setColspan(searchField, 2);

        Button criarNovoUsuario = new Button(new Icon(VaadinIcon.PLUS));
        criarNovoUsuario.setTooltipText("Criar novo usuario");
        criarNovoUsuario.addClickListener(buttonClickEvent -> criarNovoUsuario.getUI().ifPresent(ui -> ui.navigate(CadastrarTecnico.class)));


        HorizontalLayout header = new HorizontalLayout();
        header.add(new H3("Pesquisar Tecnicos"), criarNovoUsuario);
        header.setAlignItems(Alignment.CENTER);
        setMaxWidth("16cm");
        add(header);
        add(formLayout);
        add(paginationLayout);
        add(results);
    }
}
