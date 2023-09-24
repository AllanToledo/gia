package com.allantoledo.gia.views.gestor.crudCategorias;

import com.allantoledo.gia.data.entity.CategoriaItem;
import com.allantoledo.gia.data.service.CategoriaItemService;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.Pageable;

@PageTitle("Gerenciar Categorias")
@Route(value = "categorias", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class GerenciarCategorias extends VerticalLayout {

    final CategoriaItemService categoriaItemService;

    GerenciarCategorias(CategoriaItemService categoriaItemService){
        this.categoriaItemService = categoriaItemService;

        Grid<CategoriaItem> categoriaItemGrid = new Grid<>(CategoriaItem.class);
        categoriaItemGrid.setItems(categoriaItemService.list(Pageable.unpaged()).toList());
        categoriaItemGrid.setColumns("id", "nomeCategoria");
        categoriaItemGrid.addComponentColumn(categoriaItem -> {
            Icon icon = new Icon("lumo", "cross");
            Button button = new Button(icon);
            button.addClickListener(buttonClickEvent -> {
                try {
                    categoriaItemService.delete(categoriaItem.getId());
                    categoriaItemGrid.setItems(categoriaItemService.list(Pageable.unpaged()).toList());
                } catch (Exception e) {
                    Notification.show("Não foi possível excluir a categoria");
                }
            });
            return button;
        });

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");
        Button adicionarButton = new Button("Adicionar");
        adicionarButton.addClickListener(buttonClickEvent -> {
            try {
                var categoriaItem = new CategoriaItem();
                categoriaItem.setNomeCategoria(nomeField.getValue().toUpperCase());
                categoriaItemService.update(categoriaItem);
                categoriaItemGrid.setItems(categoriaItemService.list(Pageable.unpaged()).toList());
                nomeField.clear();
            } catch (Exception e) {
                Notification.show("Não foi possível salvar a categoria");
            }
        });
        formLayout.add(nomeField, adicionarButton);

        setMaxWidth("16cm");
        add(new H3("Gerenciar Categorias"));
        add(formLayout);
        add(categoriaItemGrid);
    }
}
