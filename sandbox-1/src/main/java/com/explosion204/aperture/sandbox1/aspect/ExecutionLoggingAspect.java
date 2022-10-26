package com.explosion204.aperture.sandbox1.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionLoggingAspect {
  @Pointcut("within(com.explosion204.aperture.sandbox1.service.*)")
  public void logServiceMethodExecution() { }

  @SneakyThrows
  @Around("logServiceMethodExecution()")
  public Object handleLogging(ProceedingJoinPoint joinPoint) {
    final String methodName = joinPoint.getSignature().getName();
    log.info("Start invocation: {}", methodName);
    final Object result = joinPoint.proceed();
    log.info("Finish invocation: {}", methodName);

    return result;
  }
}
