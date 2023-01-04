package com.explosion204.aperture.sandbox2.data.jdbc.query;

import com.explosion204.aperture.sandbox2.config.condition.JdbcTemplateDisabledCondition;
import com.explosion204.aperture.sandbox2.data.jdbc.connection.JdbcConnectionPool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
@RequiredArgsConstructor
@Conditional(JdbcTemplateDisabledCondition.class)
public class QueryContextFactory {
  private final JdbcConnectionPool connectionPool;

  public QueryContext createContext(boolean isTransaction) {
    final Connection contextConnection = connectionPool.acquireConnection();
    return new QueryContext(contextConnection, isTransaction);
  }
}
