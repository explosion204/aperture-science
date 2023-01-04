package com.explosion204.aperture.sandbox1;

import com.explosion204.aperture.sandbox1.condition.CubeFactoryDisabledCondition;
import com.explosion204.aperture.sandbox1.object.Cube;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "com.explosion204.aperture.sandbox1")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class SandboxConfig {

  @Bean
  @Conditional(CubeFactoryDisabledCondition.class)
  @Scope("sandbox")
  public Cube cube() {
    return new Cube();
  }
}
