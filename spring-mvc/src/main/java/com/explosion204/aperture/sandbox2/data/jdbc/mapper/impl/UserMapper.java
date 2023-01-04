package com.explosion204.aperture.sandbox2.data.jdbc.mapper.impl;

import com.explosion204.aperture.sandbox2.config.condition.JdbcTemplateDisabledCondition;
import com.explosion204.aperture.sandbox2.data.entity.User;
import com.explosion204.aperture.sandbox2.data.jdbc.mapper.ResultSetMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
@Conditional(JdbcTemplateDisabledCondition.class)
public class UserMapper implements ResultSetMapper<User> {
  @SneakyThrows
  @Override
  public User map(ResultSet resultSet) {
    final User user = new User();
    user.setId(resultSet.getLong("id"));
    user.setUsername(resultSet.getString("username"));
    user.setEmail(resultSet.getString("email"));
    return user;
  }
}
