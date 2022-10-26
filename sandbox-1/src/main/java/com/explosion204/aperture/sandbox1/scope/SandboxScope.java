package com.explosion204.aperture.sandbox1.scope;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SandboxScope implements Scope {
  private static final Map<String, Object> beans = new ConcurrentHashMap<>();
  private static final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

  @Override
  public Object get(String name, ObjectFactory<?> objectFactory) {
    log.info("Getting cube from sandbox scope");
    return beans.computeIfAbsent(name, key -> objectFactory.getObject());
  }

  @Override
  public Object remove(String name) {
    log.info("Removing bean {} from sandbox scope", name);
    final Runnable callback = destructionCallbacks.remove(name);

    if (callback != null) {
      callback.run();
    }

    return beans.remove(name);
  }

  @Override
  public void registerDestructionCallback(String name, Runnable callback) {
    destructionCallbacks.put(name, callback);
  }

  @Override
  public Object resolveContextualObject(String s) {
    return null;
  }

  @Override
  public String getConversationId() {
    return null;
  }
}
