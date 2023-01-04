package com.explosion204.aperture.sandbox2.config.condition;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class JdbcTemplateDisabledCondition extends JdbcTemplateEnabledCondition  {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return !super.matches(context, metadata);
  }
}
