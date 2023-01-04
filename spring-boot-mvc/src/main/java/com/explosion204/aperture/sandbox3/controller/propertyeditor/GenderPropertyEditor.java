package com.explosion204.aperture.sandbox3.controller.propertyeditor;

import com.explosion204.aperture.sandbox3.controller.model.Gender;

import java.beans.PropertyEditorSupport;

public class GenderPropertyEditor extends PropertyEditorSupport {
  @Override
  public String getAsText() {
    final Gender gender = (Gender) getValue();
    return gender.name().toLowerCase();
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    final Gender gender = switch (text) {
      case "m", "male", "MALE" -> Gender.MALE;
      case "f", "female", "FEMALE" -> Gender.FEMALE;
      default -> Gender.OTHER;
    };

    setValue(gender);
  }
}
