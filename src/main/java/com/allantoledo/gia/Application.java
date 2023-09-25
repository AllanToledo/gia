package com.allantoledo.gia;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

/**
 * The entry point of the Spring Boot application.
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "gestodeitensapreendidos")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("PT-BR"));
        SpringApplication.run(Application.class, args);
    }

}
