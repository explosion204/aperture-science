package com.explosion204.aperture.sandbox3.config;

import com.explosion204.aperture.sandbox3.controller.argumentresolver.SecretIdentifierHeaderArgumentResolver;
import com.explosion204.aperture.sandbox3.controller.converter.StringToGenderConverter;
import com.explosion204.aperture.sandbox3.controller.converter.TreeHttpMessageConverter;
import com.explosion204.aperture.sandbox3.controller.formatter.GenderFormatter;
import com.explosion204.aperture.sandbox3.controller.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Configuration
@ComponentScan(basePackages = "com.explosion204.aperture.sandbox3")
@RequiredArgsConstructor
public class ApplicationConfiguration implements WebMvcConfigurer {
  @Value("${feature.use-formatters.enabled:false}")
  private boolean useFormatters;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("css/**", "images/**", "js/**")
            .addResourceLocations("classpath:/css/", "classpath:/images/", "classpath:/js/");
  }

  @Bean
  public LocaleResolver localeResolver() {
    final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.US);
    return localeResolver;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getLocaleChangeInterceptor());
    registry.addInterceptor(new LoggingInterceptor());
  }

  private LocaleChangeInterceptor getLocaleChangeInterceptor() {
    final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("i18n/messages");
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

    return messageSource;
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    if (useFormatters) {
      registry.addFormatter(new GenderFormatter());
    } else {
      registry.addConverter(new StringToGenderConverter());
    }
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new SecretIdentifierHeaderArgumentResolver());
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    final TreeHttpMessageConverter treeHttpMessageConverter = new TreeHttpMessageConverter();
    treeHttpMessageConverter.setSupportedMediaTypes(List.of(MediaType.TEXT_PLAIN));
    converters.add(treeHttpMessageConverter);

    converters.add(new MappingJackson2XmlHttpMessageConverter());
  }

  @Override
  public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    configurer.setTaskExecutor(asyncTaskExecutor());
  }

  @Bean
  public AsyncTaskExecutor asyncTaskExecutor() {
    final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setThreadNamePrefix("async-");
    return taskExecutor;
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.favorParameter(false)
            .ignoreAcceptHeader(false)
            .defaultContentType(MediaType.APPLICATION_JSON);
  }
}
