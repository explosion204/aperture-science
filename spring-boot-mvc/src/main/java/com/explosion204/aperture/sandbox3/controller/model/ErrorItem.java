package com.explosion204.aperture.sandbox3.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorItem {
  private String fieldName;
  private String message;
}
