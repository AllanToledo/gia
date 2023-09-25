package com.allantoledo.gia.views.gestor.crudOrganizacoesApreensores;

import com.allantoledo.gia.data.entity.OrgaoApreensor;
import com.allantoledo.gia.data.service.OrgaoApreensorService;
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

@PageTitle("Gerenciar Organizações Apreensores")
@Route(value = "apreensores", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class GerenciarApreensores extends VerticalLayout {

    final OrgaoApreensorService orgaoApreensorService;

    GerenciarApreensores(OrgaoApreensorService orgaoApreensorService){
        this.orgaoApreensorService = orgaoApreensorService;

        Grid<OrgaoApreensor> orgaoApreensorGrid = new Grid<>(OrgaoApreensor.class);
        orgaoApreensorGrid.setItems(orgaoApreensorService.list(Pageable.unpaged()).toList());
        orgaoApreensorGrid.setColumns("id", "nome");
        orgaoApreensorGrid.addComponentColumn(categoriaItem -> {
            Icon icon = new Icon("lumo", "cross");
            Button button = new Button(icon);
            button.addClickListener(buttonClickEvent -> {
                try {
                    orgaoApreensorService.delete(categoriaItem.getId());
                    orgaoApreensorGrid.setItems(orgaoApreensorService.list(Pageable.unpaged()).toList());
                } catch (Exception e) {
                    Notification.show("Não foi possível excluir o orgão apreensor");
                }
            });
            return button;
        });

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");
        Button adicionarButton = new Button("Adicionar");
        adicionarButton.addClickListener(buttonClickEvent -> {
            try {
                var orgaoApreensor = new OrgaoApreensor();
                orgaoApreensor.setNome(nomeField.getValue().toUpperCase());
                orgaoApreensorService.update(orgaoApreensor);
                orgaoApreensorGrid.setItems(orgaoApreensorService.list(Pageable.unpaged()).toList());
                nomeField.clear();
            } catch (Exception e) {
                Notification.show("Não foi possível salvar o orgão apreensor");
            }
        });
        formLayout.add(nomeField, adicionarButton);

        setMaxWidth("16cm");
        add(new H3("Gerenciar Organizações Apreensores"));
        add(formLayout);
        add(orgaoApreensorGrid);
    }
}
