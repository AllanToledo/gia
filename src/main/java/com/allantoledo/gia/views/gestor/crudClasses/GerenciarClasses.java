package com.allantoledo.gia.views.gestor.crudClasses;

import com.allantoledo.gia.data.entity.CategoriaItem;
import com.allantoledo.gia.data.entity.ClasseProcesso;
import com.allantoledo.gia.data.service.CategoriaItemService;
import com.allantoledo.gia.data.service.ClasseProcessoService;
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
@Route(value = "classes", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class GerenciarClasses extends VerticalLayout {

    ClasseProcessoService classeProcessoService;

    GerenciarClasses(ClasseProcessoService classeProcessoService){
        this.classeProcessoService = classeProcessoService;

        Grid<ClasseProcesso> classeProcessoGrid = new Grid<>(ClasseProcesso.class);
        classeProcessoGrid.setItems(classeProcessoService.list(Pageable.unpaged()).toList());
        classeProcessoGrid.setColumns("id", "nomeClasse");
        classeProcessoGrid.addComponentColumn(categoriaItem -> {
            Icon icon = new Icon("lumo", "cross");
            Button button = new Button(icon);
            button.addClickListener(buttonClickEvent -> {
                try {
                    classeProcessoService.delete(categoriaItem.getId());
                    classeProcessoGrid.setItems(classeProcessoService.list(Pageable.unpaged()).toList());
                } catch (Exception e) {
                    Notification.show("Não foi possível excluir a classe");
                }
            });
            return button;
        });

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");
        Button adicionarButton = new Button("Adicionar");
        adicionarButton.addClickListener(buttonClickEvent -> {
            try {
                var classeProcesso = new ClasseProcesso();
                classeProcesso.setNomeClasse(nomeField.getValue().toUpperCase());
                classeProcessoService.update(classeProcesso);
                classeProcessoGrid.setItems(classeProcessoService.list(Pageable.unpaged()).toList());
                nomeField.clear();
            } catch (Exception e) {
                Notification.show("Não foi possível salvar a classe");
            }
        });
        formLayout.add(nomeField, adicionarButton);

        setMaxWidth("16cm");
        add(new H3("Gerenciar Classes de Processo"));
        add(formLayout);
        add(classeProcessoGrid);
    }
}
