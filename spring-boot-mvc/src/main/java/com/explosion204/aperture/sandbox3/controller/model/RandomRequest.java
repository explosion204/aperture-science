package com.explosion204.aperture.sandbox3.controller.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class RandomRequest {
  @Min(value = 1, message = "{start.min.value.error}")
  @Max(value = 100, message = "{start.max.value.error}")
  private int start;

  @Min(value = 1, message = "{end.min.value.error}")
  @Max(value = 100, message = "{end.max.value.error}")
  private int end;
}
