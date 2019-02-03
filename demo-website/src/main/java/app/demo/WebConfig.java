package app.demo;

import app.demo.common.exception.RuntimeIOException;
import app.demo.common.util.JSONBinder;
import app.demo.common.util.Messages;
import app.demo.common.web.interceptor.ExceptionInterceptor;
import app.demo.common.web.interceptor.LoginRequiredInterceptor;
import app.demo.common.web.interceptor.PermissionRequiredInterceptor;
import com.google.common.base.Strings;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author chi
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class)
public class WebConfig implements WebMvcConfigurer {
    @Bean
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setMessageInterpolator(new ParameterMessageInterpolator());
        validator.afterPropertiesSet();
        return validator;
    }

    @Bean
    public MessageSource messageSource() {
        try {
            Resource[] messageResources = new PathMatchingResourcePatternResolver().getResources("classpath*:messages/*.properties");
            Messages messages = new Messages();
            String[] baseNames = new String[messageResources.length];
            for (int i = 0; i < messageResources.length; i++) {
                Resource messageResource = messageResources[i];
                String filename = messageResource.getFilename();
                if (!Strings.isNullOrEmpty(filename)) {
                    baseNames[i] = "messages/" + filename.substring(0, filename.indexOf('_'));
                }
            }
            messages.setBasenames(baseNames);
            return messages;
        } catch (IOException e) {
            throw new RuntimeIOException("failed to load properties", e);
        }
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000 * 1000 * 10);
        return multipartResolver;
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/templates");
//        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Bean
    public LoginRequiredInterceptor loginRequiredInterceptor() {
        return new LoginRequiredInterceptor();
    }

    @Bean
    public PermissionRequiredInterceptor permissionRequiredInterceptor() {
        return new PermissionRequiredInterceptor();
    }

    @Bean
    public ExceptionInterceptor exceptionInterceptor() {
        return new ExceptionInterceptor();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.US);
        cookieLocaleResolver.setCookieName("lang");
        return cookieLocaleResolver;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        ArrayList<MediaType> textTypes = new ArrayList<>();
        textTypes.add(MediaType.TEXT_PLAIN);
        textTypes.add(MediaType.TEXT_HTML);
        textTypes.add(MediaType.TEXT_XML);
        textTypes.add(MediaType.APPLICATION_XML);
        textTypes.add(MediaType.APPLICATION_JSON);
        stringConverter.setSupportedMediaTypes(textTypes);
        converters.add(stringConverter);
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(JSONBinder.objectMapper());
        converters.add(jsonConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(exceptionInterceptor());
        registry.addInterceptor(loginRequiredInterceptor());
        registry.addInterceptor(permissionRequiredInterceptor());
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/admin/user/user");
    }
}
