package com.explosion204.aperture.sandbox3.controller.formatter;

import com.explosion204.aperture.sandbox3.controller.model.Gender;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

// works for @RequestParam and @PathVariable
public class GenderFormatter implements Formatter<Gender> {
  @Override
  public Gender parse(String text, Locale locale) throws ParseException {
    return switch (text) {
      case "m", "male", "MALE" -> Gender.MALE;
      case "f", "female", "FEMALE" -> Gender.FEMALE;
      default -> Gender.OTHER;
    };
  }

  @Override
  public String print(Gender object, Locale locale) {
    return object.name().toLowerCase();
  }
}
