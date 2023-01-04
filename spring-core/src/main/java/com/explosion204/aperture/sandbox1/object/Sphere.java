package com.explosion204.aperture.sandbox1.object;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class Sphere {
  private final Contents contents;
}
