package com.explosion204.aperture.sandbox2.data.jdbc.dao;

import com.explosion204.aperture.sandbox2.config.condition.JdbcTemplateEnabledCondition;
import com.explosion204.aperture.sandbox2.data.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@Component
@Conditional(JdbcTemplateEnabledCondition.class)
@RequiredArgsConstructor
public class UserDaoJdbcTemplateImpl implements UserDao {
  private static final String SELECT_BY_ID =
          "SELECT id, username, email  " +
          "FROM app_user " +
          "WHERE id = ?;";

  private static final String INSERT = "INSERT INTO app_user(username, email) VALUES (?, ?)";

  private final JdbcTemplate jdbcTemplate;
  private final BeanPropertyRowMapper<User> rowMapper;
  private final TransactionTemplate transactionTemplate;

  @Override
  public Optional<User> findById(long id) {
    return jdbcTemplate.query(SELECT_BY_ID, rowMapper, id)
            .stream()
            .findFirst();
  }

  @Override
  public void create(User user) {
    transactionTemplate.executeWithoutResult(status -> {
      jdbcTemplate.update(INSERT, user.getUsername(), user.getEmail());
      // trigger uniqueness violation and transaction rollback
      jdbcTemplate.update(INSERT, user.getUsername(), user.getEmail());
    });
  }
}
