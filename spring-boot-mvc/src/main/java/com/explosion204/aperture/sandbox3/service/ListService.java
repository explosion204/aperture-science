package com.explosion204.aperture.sandbox3.service;

import com.explosion204.aperture.sandbox3.controller.model.ListItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class ListService {
  public List<ListItem> generateItems(List<String> stringItems) {
    return IntStream.range(0, stringItems.size())
            .mapToObj(i -> new ListItem(i, stringItems.get(i)))
            .toList();
  }
}
