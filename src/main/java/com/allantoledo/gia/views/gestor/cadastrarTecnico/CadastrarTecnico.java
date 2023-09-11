package com.allantoledo.gia.views.gestor.cadastrarTecnico;

import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.data.repository.UsuarioRepository;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Random;
import java.util.Set;

@PageTitle("Tecnicos")
@Route(value = "about", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class CadastrarTecnico extends VerticalLayout {

    UsuarioRepository usuarioRepository;

    PasswordEncoder passwordEncoder;

    Validator validator;
    public CadastrarTecnico(UsuarioRepository usuarioRepository,
                            PasswordEncoder passwordEncoder,
                            Validator validator) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;


        FormLayout formLayout = new FormLayout();
        TextField nomeCompletoField = new TextField("NOME COMPLETO");
        TextField cpfField = new TextField("CPF (Somente números)");
        TextField senhaField = new TextField("SENHA");
        senhaField.setReadOnly(true);
        senhaField.setValue(gerarSenhaAleatoria());

        Button cancelar = new Button("CANCELAR");
        cancelar.addClickListener((buttonClickEvent) -> {
            cpfField.clear();
            nomeCompletoField.clear();
            senhaField.setValue(gerarSenhaAleatoria());
        });

        Button cadastrar = new Button("CADASTRAR");
        cadastrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cadastrar.addClickListener((buttonClickEvent) -> {
            Usuario novoTecnico = new Usuario();
            novoTecnico.setAtivado(true);
            novoTecnico.setNome(nomeCompletoField.getValue());
            novoTecnico.setCpf(cpfField.getValue().replaceAll("[^0-9]", ""));
            novoTecnico.setSenhaCriptografada(passwordEncoder.encode(senhaField.getValue()));
            novoTecnico.setRole(Usuario.Role.TECNICO);
            Set<ConstraintViolation<Usuario>> violations = validator.validate(novoTecnico);
            for(var violation: violations){
                Notification.show(violation.getMessage());
            }
            if(!violations.isEmpty()) return;
            usuarioRepository.save(novoTecnico);
            Notification.show("Técnico cadastrado com sucesso");
            cancelar.click();
        });

        formLayout.add(nomeCompletoField, cpfField, senhaField);
        formLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("10cm", 2));
        formLayout.setColspan(nomeCompletoField, 2);

        this.setMaxWidth("16cm");
        add(new H3("Cadastrar Novo Técnico"));
        add(formLayout);
        var buttonLayout = new HorizontalLayout(cancelar, cadastrar);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        add(buttonLayout);
    }

    String gerarSenhaAleatoria() {
        Random random = new Random();
        StringBuilder senhaAleatoria = new StringBuilder();
        for(int i = 0; i < 6; i++)
            senhaAleatoria.append((char) ('a' + random.nextInt(26)));
        return senhaAleatoria.toString();
    }
}
