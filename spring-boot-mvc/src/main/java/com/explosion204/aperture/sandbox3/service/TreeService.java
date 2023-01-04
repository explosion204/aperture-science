package com.explosion204.aperture.sandbox3.service;

import com.explosion204.aperture.sandbox3.controller.model.Tree;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TreeService {
  public String generateDescription(Tree tree) {
    final StringBuilder sb = new StringBuilder("This tree is: ");
    final Iterator<String> iterator = getCharacteristics(tree).iterator();

    while (iterator.hasNext()) {
      sb.append(iterator.next());

      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }

    return sb.toString();
  }

  private List<String> getCharacteristics(Tree tree) {
    final List<String> characteristics = new ArrayList<>();

    if (tree.isWise()) {
      characteristics.add("wise");
    }

    if (tree.isMystical()) {
      characteristics.add("mystical");
    }

    return characteristics;
  }
}
