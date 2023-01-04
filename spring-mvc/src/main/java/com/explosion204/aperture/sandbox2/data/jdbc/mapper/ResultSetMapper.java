package com.explosion204.aperture.sandbox2.data.jdbc.mapper;

import java.sql.ResultSet;

@FunctionalInterface
public interface ResultSetMapper<T> {
  T map(ResultSet resultSet);
}
