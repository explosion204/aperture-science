package com.explosion204.aperture.sandbox3.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
  MALE,
  FEMALE,
  OTHER;

  @JsonCreator
  public static Gender fromText(String text) {
    return switch (text) {
      case "m", "male", "MALE" -> Gender.MALE;
      case "f", "female", "FEMALE" -> Gender.FEMALE;
      default -> Gender.OTHER;
    };
  }


  @Override
  @JsonValue
  public String toString() {
    return name().toLowerCase();
  }
}
