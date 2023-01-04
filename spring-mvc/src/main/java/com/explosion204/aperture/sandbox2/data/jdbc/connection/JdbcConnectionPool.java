package com.explosion204.aperture.sandbox2.data.jdbc.connection;

import com.explosion204.aperture.sandbox2.config.condition.JdbcTemplateDisabledCondition;
import com.explosion204.aperture.sandbox2.exception.JdbcConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
@RequiredArgsConstructor
@Conditional(JdbcTemplateDisabledCondition.class)
public class JdbcConnectionPool {
  private final ConnectionFactory connectionFactory;
  private final Lock connectionPoolLock = new ReentrantLock(true);
  private final Condition hasAvailableConnections = connectionPoolLock.newCondition();

  @Value("${db.pool.min-size}")
  private int poolMinSize;

  @Value("${db.pool.max-size}")
  private int poolMaxSize;

  @Value("${db.pool.validation.connection-usage-timeout}")
  private long connectionUsageTimeout;

  private Queue<Connection> availableConnections;
  private Queue<Pair<Connection, Instant>> busyConnections;

  @PostConstruct
  void init() {
    if (poolMinSize > poolMaxSize) {
      log.error("Min size of pool is greater than max size");
      throw new JdbcConnectionException();
    }

    availableConnections = new ArrayDeque<>(poolMaxSize);
    busyConnections = new ArrayDeque<>(poolMaxSize);

    for (int i = 0; i < poolMinSize; i++) {
      final Connection connection = createConnection();
      availableConnections.add(connection);
    }

    if (availableConnections.size() < poolMinSize) {
      log.error("Unable to create pool due to lack of connections. Required {} but got {}", poolMaxSize,
              availableConnections);
      throw new JdbcConnectionException();
    }
  }

  public Connection acquireConnection() {
    Connection connection = null;

    try {
      connectionPoolLock.lock();

      if (availableConnections.size() + busyConnections.size() < poolMaxSize) {
        connection = createConnection();
      } else {
        while (availableConnections.isEmpty()) {
          hasAvailableConnections.await();
        }

        connection = availableConnections.remove();
      }

      final Instant usageStart = Instant.now();
      busyConnections.add(Pair.of(connection, usageStart));
    } catch (InterruptedException e) {
      log.error("Caught an exception", e);
      Thread.currentThread().interrupt();
    } finally {
      connectionPoolLock.unlock();
    }

    return connection;
  }

  public boolean releaseConnection(Connection connection) {
    if (connection != null && connection.getClass() == ProxyConnection.class) {
      try {
        connectionPoolLock.lock();
        busyConnections.removeIf(pair -> pair.getLeft().equals(connection));
        availableConnections.add(connection);
      } finally {
        hasAvailableConnections.signal();
        connectionPoolLock.unlock();
      }

      return true;

    } else {
      log.warn("Trying to release a connection not supposed to be released!");
      return false;
    }
  }


  public void destroy() {
    connectionPoolLock.lock();
    final int activeConnections = availableConnections.size() + busyConnections.size();

    for (int i = 0; i < activeConnections; i++) {
      try {
        while (availableConnections.isEmpty()) {
          hasAvailableConnections.await();
        }

        final ProxyConnection connection = (ProxyConnection) availableConnections.remove();
        connection.closeWrappedConnection();
      } catch (InterruptedException e) {
        log.error("Caught an exception", e);
        Thread.currentThread().interrupt();
      } catch (SQLException e) {
        log.error("Unable to close connection in a proper way", e);
      }
    }

    connectionPoolLock.unlock();
    DriverManager.getDrivers()
            .asIterator()
            .forEachRemaining(driver -> {
              try {
                DriverManager.deregisterDriver(driver);
              } catch (SQLException e) {
                log.error("Failed to deregister driver: ", e);
              }
            });
  }

  @Scheduled(
          initialDelayString = "${db.pool.validation.initial-delay}",
          fixedDelayString = "${db.pool.validation.period}"
  )
  public void sanitize() {
    log.info("Starting pool sanitizing");

    try {
      connectionPoolLock.lock();
      closeTimedOutConnections(busyConnections, connectionUsageTimeout);
      validatePoolSize(availableConnections, busyConnections, poolMinSize);
      freePool(availableConnections, busyConnections, poolMinSize);
    } finally {
      connectionPoolLock.unlock();
      log.info("Pool sanitizing complete");
    }
  }

  private void closeTimedOutConnections(Queue<Pair<Connection, Instant>> busyConnections,
                                        long connectionUsageTimeout) {

    for (Pair<Connection, Instant> pair : busyConnections) {
      final ProxyConnection connection = (ProxyConnection) pair.getLeft();
      final Instant usageStart = pair.getRight();
      final Instant currentTimestamp = Instant.now();
      final long usageDuration = Duration.between(usageStart, currentTimestamp).toMillis();

      if (usageDuration > connectionUsageTimeout) {
        try {
          connection.closeWrappedConnection();
          busyConnections.removeIf(p -> p.getLeft().equals(connection));
        } catch (SQLException e) {
          log.error("Unable to close connection in a proper way", e);
        }
      }
    }
  }

  private void validatePoolSize(Queue<Connection> availableConnections,
                                Queue<Pair<Connection, Instant>> busyConnections, int poolMinSize) {

    while (availableConnections.size() + busyConnections.size() < poolMinSize) {
      try {
        final Connection newConnection = connectionFactory.createConnection();
        availableConnections.offer(newConnection);
      } catch (JdbcConnectionException e) {
        log.error("Caught an error trying to establish connection", e);
      }
    }
  }

  private void freePool(Queue<Connection> availableConnections, Queue<Pair<Connection, Instant>> busyConnections,
                        int poolMinSize) {

    if (availableConnections.size() + busyConnections.size() > poolMinSize) {
      final ProxyConnection connection = (ProxyConnection) availableConnections.poll();

      if (connection != null) {
        try {
          connection.closeWrappedConnection();
        } catch (SQLException e) {
          log.error("Unable to close connection in a proper way", e);
        }
      }
    }
  }

  private Connection createConnection() {
    return new ProxyConnection(this, connectionFactory.createConnection());
  }
}
