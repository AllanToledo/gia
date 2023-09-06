package com.allantoledo.gia.views.gestor.formulario;

import com.allantoledo.gia.data.service.UsuarioRepository;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@PageTitle("Cadastrar Novo Tecnico")
@Route(value = "about", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class FormularioView extends Composite<VerticalLayout> {

    UsuarioRepository usuarioRepository;

    public FormularioView(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

        HorizontalLayout row = new HorizontalLayout();

        TextField nomeField = new TextField();
        TextField cpfField = new TextField();
        TextField senhaField = new TextField();
        senhaField.setReadOnly(true);
        StringBuilder randomPasswordInitial = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 10; i++)
            randomPasswordInitial.append('a' + random.nextInt(0, 26));
        senhaField.setValue(String.valueOf(randomPasswordInitial));

    }
}
