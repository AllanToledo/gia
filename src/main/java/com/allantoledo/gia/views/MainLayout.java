package com.allantoledo.gia.views;

import com.allantoledo.gia.data.entity.Usuario;
import com.allantoledo.gia.security.AuthenticatedUser;
import com.allantoledo.gia.views.anonimo.dashboard.DashboardView;
import com.allantoledo.gia.views.gestor.crudCategorias.GerenciarCategorias;
import com.allantoledo.gia.views.gestor.crudClasses.GerenciarClasses;
import com.allantoledo.gia.views.gestor.crudOrganizacoes.ListarOrgaosDestino;
import com.allantoledo.gia.views.gestor.crudOrganizacoesApreensores.GerenciarApreensores;
import com.allantoledo.gia.views.gestor.crudTecnico.CadastrarTecnico;
import com.allantoledo.gia.views.gestor.crudDeposito.ListarDeposito;
import com.allantoledo.gia.views.gestor.crudTecnico.ListarTecnico;
import com.allantoledo.gia.views.perfil.Perfil;
import com.allantoledo.gia.views.tecnico.crudItemApreendido.CadastrarItemApreendido;
import com.allantoledo.gia.views.tecnico.crudItemApreendido.ListarItemsApreendidos;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class MainLayout extends AppLayout {
    private final SideNav menu;
    private H3 viewTitle;

    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        // Use the drawer for the menu
        setPrimarySection(Section.DRAWER);

        // Make the nav bar a header
        addToNavbar(true, createHeaderContent());

        // Put the menu in the drawer
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();

        // Configure styling for the header
        layout.setId("header");
        layout.setWidthFull();
        layout.setSpacing(true);
        layout.setMargin(false);
        layout.addClassNames(LumoUtility.Margin.End.LARGE);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Have the drawer toggle button on the left
        layout.add(new DrawerToggle());

        // Placeholder for the title of the current view.
        // The title will be set after navigation.
        viewTitle = new H3();
        layout.add(viewTitle);
        layout.setFlexGrow(1, viewTitle);
        // A user icon

        Usuario usuario = authenticatedUser.get().orElse(null);
        if (usuario == null) {
            Anchor loginLink = new Anchor("login", "Entrar");
            layout.add(loginLink);
        } else {
            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");
            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(usuario.getNome());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Desconectar", e -> authenticatedUser.logout());
            userName.getSubMenu().addItem("Perfil", e -> UI.getCurrent().navigate(Perfil.class));
            layout.add(userMenu);
        }
        return layout;
    }

    private Component createDrawerContent(SideNav menu) {
        VerticalLayout layout = new VerticalLayout();

        // Configure styling for the drawer
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.getThemeList().set("spacing-s", true);
        layout.getThemeList().set("dark", true);
        layout.getStyle().setBackground("#192434");
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Have a drawer header with an application logo
        H4 logoLayout = new H4("Gestão de Itens Apreendidos");
        logoLayout.getStyle().setTextAlign(Style.TextAlign.CENTER);
        logoLayout.setWidthFull();
        layout.add(logoLayout);

        layout.add(menu);
        return layout;
    }

    private SideNav createMenu() {
        SideNav nav = new SideNav();
        if (accessChecker.hasAccess(DashboardView.class)) {
            nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.GRIP_VERTICAL_SOLID.create()));

        }

        if (accessChecker.hasAccess(CadastrarTecnico.class)) {
            nav.addItem(new SideNavItem("Tecnicos", ListarTecnico.class, LineAwesomeIcon.USER.create()));
            nav.addItem(new SideNavItem("Depositos", ListarDeposito.class, LineAwesomeIcon.WAREHOUSE_SOLID.create()));
            nav.addItem(new SideNavItem("Organizações", ListarOrgaosDestino.class, LineAwesomeIcon.BUILDING.create()));
            nav.addItem(new SideNavItem("Categorias", GerenciarCategorias.class, LineAwesomeIcon.LIST_SOLID.create()));
            nav.addItem(new SideNavItem("Classes", GerenciarClasses.class, LineAwesomeIcon.LIST_SOLID.create()));
            nav.addItem(new SideNavItem("Organizações Apreensores", GerenciarApreensores.class, LineAwesomeIcon.GAVEL_SOLID.create()));
        }

        if(accessChecker.hasAccess(CadastrarItemApreendido.class)){
            nav.addItem(new SideNavItem("Apreensões", ListarItemsApreendidos.class, LineAwesomeIcon.FOLDER_OPEN.create()));
        }

        return nav;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        // Set the view title in the header
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
