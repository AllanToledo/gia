package com.allantoledo.gia.views.gestor.crudTecnico;

import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.data.service.UsuarioService;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@PageTitle("Tecnicos")
@Route(value = "tecnicos/update", layout = MainLayout.class)
@RolesAllowed("GESTOR")
@Uses(Icon.class)
public class CadastrarTecnico extends VerticalLayout implements HasUrlParameter<Long> {

    UsuarioService usuarioService;

    PasswordEncoder passwordEncoder;

    Validator validator;

    Usuario usuarioCadastrado;

    public CadastrarTecnico(UsuarioService usuarioService,
                            PasswordEncoder passwordEncoder,
                            Validator validator) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter != null) {
            usuarioCadastrado = usuarioService.get(parameter).orElse(null);
        }
        carregarTela();
    }

    public void carregarTela() {
        FormLayout formLayout = new FormLayout();
        TextField nomeCompletoField = new TextField("NOME COMPLETO");
        TextField cpfField = new TextField("CPF (Somente números)");
        TextField senhaField = new TextField("SENHA");
        Button ativadoButton = new Button("ATIVADO");
        Select<Usuario.Role> roleSelect = new Select<>();
        roleSelect.setLabel("NIVEL DE ACESSO");
        roleSelect.setValue(Usuario.Role.TECNICO);
        roleSelect.setItems(Usuario.Role.values());

        AtomicBoolean usuarioAtivado = new AtomicBoolean(true);
        ativadoButton.addClickListener(buttonClickEvent -> {
            usuarioAtivado.set(!usuarioAtivado.get());
            ativadoButton.setText(usuarioAtivado.get()? "ATIVADO": "DESATIVADO");
        });
        senhaField.setReadOnly(true);
        senhaField.setValue(gerarSenhaAleatoria());

        if (usuarioCadastrado != null) {
            nomeCompletoField.setValue(usuarioCadastrado.getNome());
            cpfField.setValue(usuarioCadastrado.getCpf());
            senhaField.setValue("");
            roleSelect.setValue(usuarioCadastrado.getRole());
            usuarioAtivado.set(!usuarioCadastrado.getAtivado());
            ativadoButton.click();
        }

        Button cancelar = new Button("CANCELAR");
        cancelar.addClickListener((buttonClickEvent) -> {
            cpfField.clear();
            nomeCompletoField.clear();
            senhaField.setValue(gerarSenhaAleatoria());
            roleSelect.clear();
            UI.getCurrent().getPage().getHistory().back();
        });

        Button cadastrar = new Button(usuarioCadastrado != null ? "ATUALIZAR" : "CADASTRAR");
        cadastrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cadastrar.addClickListener((buttonClickEvent) -> {
            Usuario novoUsuario;

            novoUsuario = Objects.requireNonNullElseGet(usuarioCadastrado, Usuario::new);
            novoUsuario.setAtivado(usuarioAtivado.get());
            novoUsuario.setNome(nomeCompletoField.getValue());
            novoUsuario.setCpf(cpfField.getValue().replaceAll("[^0-9]", ""));
            novoUsuario.setRole(roleSelect.getValue());
            if(usuarioCadastrado == null)
                novoUsuario.setSenhaCriptografada(passwordEncoder.encode(senhaField.getValue()));

            Set<ConstraintViolation<Usuario>> violations = validator.validate(novoUsuario);
            for (var violation : violations) {
                Notification.show(violation.getMessage());
            }
            if (!violations.isEmpty()) return;
            usuarioService.update(novoUsuario);
            if(usuarioCadastrado != null){
                Notification.show("Usuário atualizado com sucesso");
            } else {
                Notification.show("Usuário cadastrado com sucesso");
            }
            cancelar.click();
        });

        formLayout.add(nomeCompletoField, cpfField, senhaField, ativadoButton, roleSelect);
        formLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("10cm", 2),
                new ResponsiveStep("15cm", 3));
        formLayout.setColspan(nomeCompletoField, 3);

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
        for (int i = 0; i < 6; i++)
            senhaAleatoria.append((char) ('a' + random.nextInt(26)));
        return senhaAleatoria.toString();
    }


}
