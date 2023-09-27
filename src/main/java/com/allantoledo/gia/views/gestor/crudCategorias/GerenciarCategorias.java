package com.allantoledo.gia.views.gestor.crudCategorias;

import com.allantoledo.gia.data.entity.CategoriaItem;
import com.allantoledo.gia.data.service.CategoriaItemService;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Deletar " + categoriaItem.getNomeCategoria() + "?");
                dialog.setText("Essa ação é irreversível e deve ser usada apenas se necessário.");
                dialog.setCancelable(true);
                dialog.setCancelText("Cancelar");
                Button confimarDelete = new Button("DELETAR");
                confimarDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
                confimarDelete.addClickListener(event -> {
                    try {
                        categoriaItemService.delete(categoriaItem.getId());
                        categoriaItemGrid.setItems(categoriaItemService.list(Pageable.unpaged()).toList());
                    } catch (Exception e) {
                        Notification.show("Não foi possível excluir a categoria");
                    }
                });
                dialog.setConfirmButton(confimarDelete);
                dialog.open();

            });
            return button;
        });

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");
        Button adicionarButton = new Button("Adicionar");
        adicionarButton.addClickListener(buttonClickEvent -> {
            try {
                var categoriaItem = new CategoriaItem();
                var nome = nomeField.getValue().toUpperCase().trim();
                if(nome.isEmpty()) {
                    Notification.show("Nome da categoria não pode ser vazio");
                    return;
                }
                categoriaItem.setNomeCategoria(nome);
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
