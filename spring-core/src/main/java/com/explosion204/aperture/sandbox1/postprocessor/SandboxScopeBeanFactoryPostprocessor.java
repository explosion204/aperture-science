package com.explosion204.aperture.sandbox1.postprocessor;

import com.explosion204.aperture.sandbox1.scope.SandboxScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SandboxScopeBeanFactoryPostprocessor implements BeanFactoryPostProcessor {
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
    factory.registerScope("sandbox", new SandboxScope());
    log.info("Registered sandbox scope");
  }
}
