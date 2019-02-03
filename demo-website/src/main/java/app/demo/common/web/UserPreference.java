package app.demo.common.web;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class UserPreference {
    public Locale locale() {
        return LocaleContextHolder.getLocale();
    }
}
