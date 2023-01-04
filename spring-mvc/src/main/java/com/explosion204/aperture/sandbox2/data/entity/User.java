package com.explosion204.aperture.sandbox2.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
  private long id;
  private String username;
  private String email;
}
