package com.explosion204.aperture.sandbox1.factory;

import com.explosion204.aperture.sandbox1.condition.CubeFactoryEnabledCondition;
import com.explosion204.aperture.sandbox1.object.Cube;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Conditional(CubeFactoryEnabledCondition.class)
@Scope("sandbox")
public class CubeFactoryBean implements FactoryBean<Cube> {
  @Override
  public Cube getObject() {
    final Cube cube = new Cube();
    cube.setCreatedFromFactoryBean(true);

    return cube;
  }

  @Override
  public Class<?> getObjectType() {
    return Cube.class;
  }
}
