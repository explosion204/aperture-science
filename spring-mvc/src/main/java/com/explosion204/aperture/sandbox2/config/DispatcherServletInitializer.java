package com.explosion204.aperture.sandbox2.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class DispatcherServletInitializer implements WebApplicationInitializer {
  private static final String ROOT_URL_MAPPING = "/";

  @Override
  public void onStartup(ServletContext servletContext) {
    final AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.register(ApplicationConfiguration.class);
    final DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

    final ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
    registration.setLoadOnStartup(1); // set the highest priority for servlet initialization
    registration.addMapping(ROOT_URL_MAPPING);
  }
}
