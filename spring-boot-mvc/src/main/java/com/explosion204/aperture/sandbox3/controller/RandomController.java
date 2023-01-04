package com.explosion204.aperture.sandbox3.controller;

import com.explosion204.aperture.sandbox3.controller.model.RandomRequest;
import com.explosion204.aperture.sandbox3.service.RandomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/random")
@RequiredArgsConstructor
@Slf4j
public class RandomController {
  private static final AtomicInteger requestNumber = new AtomicInteger(0);

  private final RandomService randomService;

  // @ModelAttribute reference for usage:
  // https://stackoverflow.com/questions/8688135/modelattribute-annotation-when-to-use-it
  @GetMapping
  public ModelAndView getRandomNumberViaQueryParams(@Valid @ModelAttribute("request") RandomRequest request,
                                                    BindingResult bindingResult, Model model) {

    handleErrors(bindingResult, model);
    model.addAttribute("result", randomService.getRandomInteger(request.getStart(), request.getEnd()));
    return new ModelAndView("random");
  }

  @ModelAttribute("requestDate")
  private String getRequestDate() {
    return LocalDateTime.now().toString();
  }

  private void handleErrors(BindingResult bindingResult, Model model) {
    final List<String> errorMessages = bindingResult.getAllErrors()
            .stream()
            .map(ObjectError::getDefaultMessage)
            .toList();

    model.addAttribute("errorMessages", errorMessages);
  }
}
