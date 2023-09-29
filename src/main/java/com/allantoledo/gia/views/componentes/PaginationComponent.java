package com.allantoledo.gia.views.componentes;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class PaginationComponent extends HorizontalLayout {
    Pageable page;
    public PaginationComponent(Pageable page, Function<Pageable, Void> onChangePage) {
        Text pageNumberText = new Text("1");
        this.page = page;
        Button previousPageButton = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        previousPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        previousPageButton.addClickListener(ignored -> {
            var previousOrFirst = this.page.previousOrFirst();
            pageNumberText.setText(String.valueOf(previousOrFirst.getPageNumber() + 1));
            try {
                onChangePage.apply(previousOrFirst);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Button nextPageButton = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        nextPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        nextPageButton.addClickListener(ignored -> {
            var nextPage = this.page.next();
            pageNumberText.setText(String.valueOf(nextPage.getPageNumber() + 1));
            try {
                onChangePage.apply(nextPage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        add(previousPageButton, pageNumberText, nextPageButton);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setWidthFull();
    }

    public void resetPage(Pageable page){
        this.page = page;
    };
}
