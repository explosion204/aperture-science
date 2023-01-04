package com.explosion204.aperture.sandbox1.object;

import com.explosion204.aperture.sandbox1.postprocessor.RandomId;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import javax.annotation.PreDestroy;

@Data
@Scope("sandbox")
@Slf4j
public class Cube {
  @RandomId(min = 1, max = 100)
  private int id;

  private boolean createdFromFactoryBean = false;
  private boolean processed = false;

  @PreDestroy
  void destroy() {
    log.info("Object is destroyed");
  }
}
