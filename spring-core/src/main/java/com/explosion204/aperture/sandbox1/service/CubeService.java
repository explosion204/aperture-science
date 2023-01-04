package com.explosion204.aperture.sandbox1.service;

import com.explosion204.aperture.sandbox1.object.Cube;
import org.springframework.stereotype.Service;

@Service
public class CubeService {
  public void process(Cube cube) {
    cube.setProcessed(true);
  }
}
