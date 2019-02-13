package app.demo.common.web;

import app.demo.common.exception.ResourceNotFoundException;
import app.demo.common.exception.UserAuthorizationException;
import app.demo.common.util.JSONBinder;
import app.demo.common.util.Messages;
import com.google.common.collect.Maps;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;


@ControllerAdvice
public class WebControllerAdvice {
    @Inject
    Messages messages;

    @Inject
    UserPreference userPreference;

    @Inject
    UserInfo userInfo;

    @ExceptionHandler
    public String unauthenticated(UserAuthorizationException e) {
        return "redirect:/login";
    }

    @ExceptionHandler
    public ModelAndView error(ResourceNotFoundException exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ModelAndView("/404.jsp", Maps.newHashMap());
    }

    @ExceptionHandler
    public ModelAndView error(NoHandlerFoundException exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ModelAndView("/404.jsp", Maps.newHashMap());
    }

    @ExceptionHandler
    public ModelAndView error(Exception exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ModelAndView("/500.jsp", Maps.newHashMap());
    }

    @ModelAttribute("userInfo")
    public UserInfo user() {
        return userInfo;
    }

    @ModelAttribute("lang")
    public String lang() {
        Locale locale = LocaleContextHolder.getLocale();
        return locale.toLanguageTag();
    }

    @ModelAttribute("messageScript")
    public String messageScript() {
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        return JSONBinder.toJSON(new MessagesScriptBuilder(messages).build());
    }

    @ModelAttribute("userScript")
    public String userScript() {
        return JSONBinder.toJSON(new UserInfoScriptBuilder(userInfo).build());
    }
}
