package com.explosion204.aperture.sandbox2.data.jdbc.dao;

import com.explosion204.aperture.sandbox2.config.condition.JdbcTemplateDisabledCondition;
import com.explosion204.aperture.sandbox2.data.entity.User;
import com.explosion204.aperture.sandbox2.data.jdbc.mapper.ResultSetMapper;
import com.explosion204.aperture.sandbox2.data.jdbc.query.QueryContext;
import com.explosion204.aperture.sandbox2.data.jdbc.query.QueryContextFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Conditional(JdbcTemplateDisabledCondition.class)
public class UserDaoPureJdbcImpl implements UserDao {
  private static final String SELECT_BY_ID =
          "SELECT id, username, email  " +
          "FROM app_user " +
          "WHERE id = ?;";

  private static final String INSERT = "INSERT INTO app_user(username, email) VALUES (?, ?)";

  private final QueryContextFactory contextFactory;
  private final ResultSetMapper<User> userMapper;

  public Optional<User> findById(long id) {
    final QueryContext queryContext = contextFactory.createContext(false);
    return queryContext.executeSelectForSingleResult(userMapper, SELECT_BY_ID, id);
  }

  public void create(User user) {
    final QueryContext queryContext = contextFactory.createContext(true);
    queryContext.executeInsert(INSERT, user.getUsername(), user.getEmail());
    // trigger uniqueness violation and transaction rollback
    queryContext.executeInsert(INSERT, user.getUsername(), user.getEmail());
    queryContext.commit();
  }
}
