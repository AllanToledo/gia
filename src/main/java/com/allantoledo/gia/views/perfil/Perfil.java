package com.allantoledo.gia.views.perfil;

import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.data.service.UsuarioService;
import com.allantoledo.gia.security.AuthenticatedUser;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@PageTitle("Tecnicos")
@Route(value = "perfil", layout = MainLayout.class)
@RolesAllowed({"GESTOR", "TECNICO"})
@Uses(Icon.class)
public class Perfil extends VerticalLayout {

    UsuarioService usuarioService;

    PasswordEncoder passwordEncoder;

    Validator validator;

    AuthenticatedUser authenticatedUser;

    public Perfil(UsuarioService usuarioService,
                  PasswordEncoder passwordEncoder,
                  Validator validator,
                  AuthenticatedUser authenticatedUser) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.authenticatedUser = authenticatedUser;
        carregarTela();
    }

    public void carregarTela() {
        Usuario usuarioCadastrado = authenticatedUser.get().orElseThrow();
        FormLayout formLayout = new FormLayout();
        TextField nomeCompletoField = new TextField("NOME COMPLETO");
        TextField cpfField = new TextField("CPF (Somente números)");
        cpfField.setReadOnly(true);
        PasswordField senhaField = new PasswordField("SENHA NOVA");
        PasswordField repetirSenhaField = new PasswordField("REPETIR SENHA");

        nomeCompletoField.setValue(usuarioCadastrado.getNome());
        cpfField.setValue(usuarioCadastrado.getCpf());


        Button atualizar = new Button("ATUALIZAR PERFIL");
        atualizar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        atualizar.addClickListener((buttonClickEvent) -> {
            usuarioCadastrado.setNome(nomeCompletoField.getValue());
            usuarioCadastrado.setCpf(cpfField.getValue().replaceAll("[^0-9]", ""));
            senhaField.setValue(senhaField.getValue().trim());
            if (senhaField.getValue().length() > 1) {
                if (senhaField.getValue().length() < 10) {
                    Notification.show("Senha deve conter ao menos 10 caracteres");
                    return;
                }
                if (!senhaField.getValue().equals(repetirSenhaField.getValue())) {
                    Notification.show("As senhas precisam ser iguais!");
                    return;
                }
                usuarioCadastrado.setSenhaCriptografada(passwordEncoder.encode(senhaField.getValue()));

            }

            Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioCadastrado);
            for (var violation : violations) {
                Notification.show(violation.getMessage());
            }
            if (!violations.isEmpty()) return;
            usuarioService.update(usuarioCadastrado);
            senhaField.clear();
            repetirSenhaField.clear();
            Notification.show("Usuário atualizado com sucesso");

        });

        formLayout.add(nomeCompletoField, cpfField, senhaField, repetirSenhaField);
        formLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("10cm", 2),
                new ResponsiveStep("15cm", 3));
        formLayout.setColspan(nomeCompletoField, 3);

        this.setMaxWidth("16cm");
        add(new H3("Perfil"));
        add(formLayout);
        var buttonLayout = new HorizontalLayout(atualizar);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        add(buttonLayout);
    }

}
