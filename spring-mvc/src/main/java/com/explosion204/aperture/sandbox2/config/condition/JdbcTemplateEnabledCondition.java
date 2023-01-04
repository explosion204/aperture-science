package com.explosion204.aperture.sandbox2.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class JdbcTemplateEnabledCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return Objects.equals(
            context.getEnvironment().getProperty("app.jdbc.use-template"),
            "true"
    );
  }
}
