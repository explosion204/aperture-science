package com.explosion204.aperture.sandbox3.controller.converter;

import com.explosion204.aperture.sandbox3.controller.model.Gender;
import org.springframework.core.convert.converter.Converter;

// works for @RequestParam and @PathVariable
public class StringToGenderConverter implements Converter<String, Gender> {

  @Override
  public Gender convert(String source) {
    return switch (source) {
      case "m", "male", "MALE" -> Gender.MALE;
      case "f", "female", "FEMALE" -> Gender.FEMALE;
      default -> Gender.OTHER;
    };
  }
}
