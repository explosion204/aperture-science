package com.explosion204.aperture.sandbox1.postprocessor;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomIdPostProcessor implements BeanPostProcessor {
  @SneakyThrows
  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    for (Field field : bean.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(RandomId.class)) {
        final RandomId idAnnotation = field.getAnnotation(RandomId.class);
        final int id = ThreadLocalRandom.current().nextInt(idAnnotation.min(), idAnnotation.max());
        field.setAccessible(true);
        field.set(bean, id);
      }
    }

    return bean;
  }
}
