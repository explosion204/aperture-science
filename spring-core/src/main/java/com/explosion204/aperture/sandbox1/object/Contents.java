package com.explosion204.aperture.sandbox1.object;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Contents {
  private int counter;

  public int count() {
    return ++counter;
  }
}
