package com.explosion204.aperture.sandbox3.controller;

import com.explosion204.aperture.sandbox3.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/list")
@RequiredArgsConstructor
public class ListController {
  private final ListService listService;

  @GetMapping
  public ModelAndView generateList(
          @RequestParam(name = "stringItems", defaultValue = "") List<String> stringItems,
          Model model
  ) {
    model.addAttribute("items", listService.generateItems(stringItems));
    return new ModelAndView("list");
  }
}
