package com.explosion204.aperture.sandbox3.controller.argumentresolver;


import com.explosion204.aperture.sandbox3.controller.model.SecretIdentifier;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SecretIdentifierHeaderArgumentResolver implements HandlerMethodArgumentResolver {
  private static final String SECRET_IDENTIFIER_HEADER_NAME = "Secret-Identifier";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(SecretIdentifier.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

    return webRequest.getHeader(SECRET_IDENTIFIER_HEADER_NAME);
  }
}
