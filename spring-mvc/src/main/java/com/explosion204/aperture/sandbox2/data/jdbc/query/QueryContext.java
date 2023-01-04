package com.explosion204.aperture.sandbox2.data.jdbc.query;

import com.explosion204.aperture.sandbox2.data.jdbc.mapper.ResultSetMapper;
import com.explosion204.aperture.sandbox2.exception.QueryContextException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Slf4j
public class QueryContext {
  private final Connection contextConnection;
  private final boolean isTransaction;
  private boolean isTerminated;

  @SneakyThrows
  public QueryContext(Connection contextConnection, boolean isTransaction)  {
    this.isTransaction = isTransaction;
    this.contextConnection = contextConnection;

    if (isTransaction) {
      contextConnection.setAutoCommit(false);
    }
  }

  @SneakyThrows
  public <T> void executeSelect(ResultSetMapper<T> mapper, String sqlQuery, List<T> result, Object ... params) {
    if (isTerminated) {
      throw new QueryContextException("Context is already terminated");
    }

    try (final PreparedStatement statement = contextConnection.prepareStatement(sqlQuery)) {
      initPreparedStatement(statement, params);

      try (final ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          final T mappedObject = mapper.map(resultSet);
          result.add(mappedObject);
        }
      }
    } catch (SQLException e) {
      terminateContext();
      log.warn("Context is terminated due to exception: ", e);
    } finally {
      if (!isTransaction && !isTerminated) {
        terminateContext();
      }
    }
  }

  public <T> Optional<T> executeSelectForSingleResult(ResultSetMapper<T> mapper, String sqlQuery, Object ... params) {
    if (isTerminated) {
      throw new QueryContextException("Context is already terminated");
    }

    T mappedObject = null;

    try (final PreparedStatement statement = contextConnection.prepareStatement(sqlQuery)) {
      initPreparedStatement(statement, params);

      try (final ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          mappedObject = mapper.map(resultSet);
        }
      }
    } catch (SQLException e) {
      terminateContext();
      log.warn("Context is terminated due to exception: ", e);
    } finally {
      if (!isTransaction && !isTerminated) {
        terminateContext();
      }
    }

    return mappedObject != null ? Optional.of(mappedObject) : Optional.empty();
  }

  public long executeInsert(String sqlQuery, Object ... params) {
    if (isTerminated) {
      throw new QueryContextException("Context is already terminated");
    }

    try (final PreparedStatement statement = contextConnection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
      initPreparedStatement(statement, params);
      statement.execute();

      try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
        return generatedKeys.next() ? generatedKeys.getLong(1) : 0;
      }
    } catch (SQLException e) {
      if (isTransaction) {
        try {
          contextConnection.rollback();
        } catch (SQLException ex) {
          log.error("Caught an exception trying to rollback: ", e);
        }
      }

      terminateContext();
      throw new QueryContextException("Context is terminated due to exception: ", e);
    } finally {
      if (!isTransaction && !isTerminated) {
        terminateContext();
      }
    }
  }

  public int executeUpdateOrDelete(String sqlQuery, Object ... params) {
    if (isTerminated) {
      throw new QueryContextException("Context is already terminated");
    }

    try (final PreparedStatement statement = contextConnection.prepareStatement(sqlQuery)) {
      initPreparedStatement(statement, params);
      return statement.executeUpdate();
    } catch (SQLException e) {
      if (isTransaction) {
        try {
          contextConnection.rollback();
        } catch (SQLException ex) {
          throw new QueryContextException("Caught an exception trying to rollback: ", e);
        }
      }

      terminateContext();
      throw new QueryContextException("Context is terminated due to exception: ", e);
    } finally {
      if (!isTransaction && !isTerminated) {
        terminateContext();
      }
    }
  }

  public void commit() {
    if (isTerminated) {
      throw new QueryContextException("Context is already terminated");
    }

    if (isTransaction) {
      try {
        contextConnection.commit();
      } catch (SQLException e) {
        throw new QueryContextException("Caught an exception while committing: ", e);
      }
    }

    terminateContext();
  }

  private void terminateContext() {
    if (isTerminated) {
      log.warn("Context is already terminated");
      return;
    }

    isTerminated = true;

    try {
      if (isTransaction) {
        contextConnection.setAutoCommit(true);
      }

      contextConnection.close();
    } catch (SQLException e) {
      log.error("An error occurred trying to close connection: ", e);
    }
  }

  private void initPreparedStatement(PreparedStatement statement, Object ... params) throws SQLException {
    int i = 1;

    for (Object parameter : params) {
      if (parameter != null) {
        statement.setObject(i++, parameter);
      } else {
        statement.setNull(i++, Types.NULL);
      }
    }
  }
}
