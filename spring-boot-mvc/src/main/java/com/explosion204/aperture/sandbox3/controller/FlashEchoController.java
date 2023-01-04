package com.explosion204.aperture.sandbox3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/echo/flash")
public class FlashEchoController {
  @GetMapping
  public ModelAndView getEchoForm(Model model) {
    model.addAttribute("submitUrl", "/echo/flash");
    return new ModelAndView("echo_form");
  }

  @PostMapping
  public RedirectView sendEcho(String echo, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("echo", echo);
    return new RedirectView("/echo/flash/result");
  }

  @GetMapping("/result")
  public ModelAndView getEchoResult(@ModelAttribute("echo") String echo, Model model) {
    return new ModelAndView("echo");
  }
}
