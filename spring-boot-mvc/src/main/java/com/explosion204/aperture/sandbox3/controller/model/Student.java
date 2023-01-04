package com.explosion204.aperture.sandbox3.controller.model;

import com.explosion204.aperture.sandbox3.controller.validation.CreateGroup;
import com.explosion204.aperture.sandbox3.controller.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class Student {
  @Null(groups = { CreateGroup.class, UpdateGroup.class })
  private Long id;

  @NotNull(groups = { CreateGroup.class })
  @JsonView(JsonViews.Student.External.class)
  private String firstName;

  @NotNull(groups = { CreateGroup.class })
  @JsonView(JsonViews.Student.External.class)
  private String lastName;

  @NotNull(groups = { CreateGroup.class })
  @JsonView(JsonViews.Student.External.class)
  private Gender gender;

  @Null(groups = { CreateGroup.class })
  @Min(value = 1, groups = { UpdateGroup.class })
  @JsonView(JsonViews.Student.Internal.class)
  private Integer averageGrade;
}
