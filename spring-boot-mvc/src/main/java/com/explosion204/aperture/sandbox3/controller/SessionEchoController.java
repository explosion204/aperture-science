package com.explosion204.aperture.sandbox3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/echo/session")
@SessionAttributes("echo")
public class SessionEchoController {
  @GetMapping
  public ModelAndView getEchoForm(Model model) {
    model.addAttribute("submitUrl", "/echo/session");
    return new ModelAndView("echo_form");
  }

  @PostMapping
  public RedirectView sendEcho(String echo, Model model) {
    model.addAttribute("echo", echo);
    return new RedirectView("/echo/session/result");
  }

  @GetMapping("/result")
  public ModelAndView getEchoResult(@ModelAttribute("echo") String echo, SessionStatus sessionStatus) {
    return new ModelAndView("echo");
  }
}
