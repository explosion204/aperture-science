package com.explosion204.aperture.sandbox3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.PushBuilder;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {
  private final MessageSource messageSource;

  @Value("${spring.application.name}")
  private String applicationName;

  @GetMapping
  public ModelAndView index(PushBuilder pushBuilder) {
    if (pushBuilder != null) {
      // HTTP2 works only via TLS
      pushBuilder.path("images/cake.jpg");
      pushBuilder.push();
    }

    return new ModelAndView("index");
  }

  @ModelAttribute("applicationName")
  public String applicationName() {
    return applicationName;
  }

  @ModelAttribute("language")
  public String language() {
    return messageSource.getMessage("language", null, LocaleContextHolder.getLocale());
  }
}
