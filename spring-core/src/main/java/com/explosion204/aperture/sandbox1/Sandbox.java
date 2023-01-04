package com.explosion204.aperture.sandbox1;

import com.explosion204.aperture.sandbox1.object.Cube;
import com.explosion204.aperture.sandbox1.object.Sphere;
import com.explosion204.aperture.sandbox1.service.CubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Sandbox {
  public static void main(String[] args) {
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SandboxConfig.class);
    final Cube cube = context.getBean(Cube.class);

    log.info("Cube id: " + cube.getId());
    log.info("Cube is created with factory bean: {}", cube.isCreatedFromFactoryBean());

    final CubeService cubeService = context.getBean(CubeService.class);
    cubeService.process(cube);

    // scoped proxies
    final Sphere sphere = context.getBean(Sphere.class);
    log.info("Sphere count result: {}", sphere.getContents().count());
    log.info("Sphere count result: {}", sphere.getContents().count());
    log.info("Sphere count result: {}", sphere.getContents().count());
    log.info("Sphere count result: {}", sphere.getContents().count());
  }
}
