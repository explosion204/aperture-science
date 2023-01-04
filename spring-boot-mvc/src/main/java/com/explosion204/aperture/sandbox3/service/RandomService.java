package com.explosion204.aperture.sandbox3.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class RandomService {
  public int getRandomInteger(int start, int end) {
    return ThreadLocalRandom.current().nextInt(start, end + 1);
  }
}
