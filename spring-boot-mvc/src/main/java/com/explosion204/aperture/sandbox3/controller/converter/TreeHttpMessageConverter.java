package com.explosion204.aperture.sandbox3.controller.converter;

import com.explosion204.aperture.sandbox3.controller.model.Tree;
import lombok.SneakyThrows;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import java.util.Scanner;

public class TreeHttpMessageConverter extends AbstractHttpMessageConverter<Tree> {
  @Override
  protected boolean supports(Class<?> clazz) {
    return Tree.class.isAssignableFrom(clazz);
  }

  @Override
  protected Tree readInternal(Class<? extends Tree> clazz, HttpInputMessage inputMessage) {
    String body = getBody(inputMessage);
    String[] parts = body.split(" ");

    if (parts.length != 2) {
      return null;
    }

    final Tree tree = new Tree();
    tree.setWise(Boolean.parseBoolean(parts[0]));
    tree.setMystical(Boolean.parseBoolean(parts[1]));

    return tree;
  }

  @Override
  protected void writeInternal(Tree tree, HttpOutputMessage outputMessage) {
    throw new UnsupportedOperationException();
  }

  @SneakyThrows
  private String getBody(HttpInputMessage message) {
    final Scanner scanner = new Scanner(message.getBody(), "UTF-8");
    return scanner.useDelimiter("\\A").next();
  }
}
