package com.explosion204.aperture.sandbox3.controller.advice;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = EnableTimestampResponseBody.class)
@ConditionalOnExpression("#{false}")
public class TimestampResponseBodyAdvice implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @SneakyThrows
  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
    if (body == null) {
      return null;
    }

    Map<Object, Object> modifiedBody;

    if (Collection.class.isAssignableFrom(body.getClass())) {
      modifiedBody = handleCollection(body);
    } else if (Map.class.isAssignableFrom(body.getClass())) {
      modifiedBody = (Map<Object, Object>) body;

    } else {
      modifiedBody = handleEntity(body);
    }

    modifiedBody.put("timestamp", LocalDateTime.now().toString());
    return modifiedBody;
  }

  private Map<Object, Object> handleCollection(Object body) {
    final Map<Object, Object> modifiedBody = new HashMap<>();
    modifiedBody.put("data", body);
    return modifiedBody;
  }

  @SneakyThrows
  private Map<Object, Object> handleEntity(Object body) {
    final Map<Object, Object> modifiedBody = new HashMap<>();

    for (Field field : body.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      modifiedBody.put(field.getName(), field.get(body));
    }

    return modifiedBody;
  }
}
