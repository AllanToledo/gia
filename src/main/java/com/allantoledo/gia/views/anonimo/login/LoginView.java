package com.allantoledo.gia.views.anonimo.login;

import com.allantoledo.gia.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Gestão de Itens Apreendidos");
        i18n.getHeader().setDescription("Entrar com CPF e SENHA");
        i18n.getForm().setTitle("Entrar");
        i18n.getForm().setUsername("CPF");
        i18n.getForm().setPassword("SENHA");
        i18n.getForm().setSubmit("Entrar");
        i18n.getErrorMessage().setTitle("CPF ou SENHA inválidos");
        i18n.getErrorMessage()
                .setMessage("Verifique se o CPF e a SENHA estão corretos e tente novamente.");
        i18n.getErrorMessage().setPassword("Senha Não Pode Ser Vazia");
        i18n.getErrorMessage().setUsername("CPF Não Pode Ser Vazio");

        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
