package com.explosion204.aperture.sandbox1.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class CubeFactoryEnabledCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return Objects.equals(
            context.getEnvironment().getProperty("cube.factory.enabled"),
            "true"
    );
  }
}
