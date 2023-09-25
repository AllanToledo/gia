package com.allantoledo.gia.views.anonimo.dashboard;

import com.allantoledo.gia.data.Dashboard;
import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.math.BigDecimal;
import java.text.NumberFormat;

@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class DashboardView extends VerticalLayout {

    final Dashboard dashboard;

    public DashboardView(Dashboard dashboard) {
        setWidthFull();
        setHeightFull();
        this.dashboard = dashboard;
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new HorizontalLayout(
                itensApreendidosNoAno(), sumValorDosItensApreendidosNoAno()
        ));
        add(new HorizontalLayout(
                categoriaComMaisApreensoes(), itensDoados()
        ));
    }

    private Component createComponente(String titulo, String conteudo) {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.SMALL,
                LumoUtility.Padding.MEDIUM
        );

        H4 tituloH4 = new H4(titulo);
        tituloH4.getStyle().setTextAlign(Style.TextAlign.CENTER);
        layout.add(tituloH4);
        H3 conteudoComponent = new H3(conteudo);
        conteudoComponent.getStyle().setTextAlign(Style.TextAlign.CENTER);
        layout.add(conteudoComponent);

        return layout;
    }

    private Component itensApreendidosNoAno() {
        return createComponente("Quantidade de Itens Apreendidos em 2023",
                dashboard.countItensApreendidosNoAno("2023") + " itens");
    }

    private Component itensDoados() {
        return createComponente("Quantidade de Itens enviados para doação em 2023",
                dashboard.quantidadeDeItemsDoados("2023") + " itens");
    }

    private Component sumValorDosItensApreendidosNoAno() {
        return createComponente("Soma do valor avaliado em apreensões em 2023",
                currencyFormat(dashboard.sumValorDosItensApreendidosNoAno("2023")));
    }

    private Component categoriaComMaisApreensoes() {
        return createComponente("Categoria com mais apreensões em 2023",
                dashboard.categoriaComMaisApreensoes("2023"));
    }

    public static String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

}
