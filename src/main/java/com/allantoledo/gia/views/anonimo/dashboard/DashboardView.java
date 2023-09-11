package com.allantoledo.gia.views.anonimo.dashboard;

import com.allantoledo.gia.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class DashboardView extends Composite<VerticalLayout> {

    public DashboardView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
    }
}
