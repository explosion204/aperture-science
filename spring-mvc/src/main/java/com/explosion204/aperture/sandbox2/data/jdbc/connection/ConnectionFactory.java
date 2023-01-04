package com.explosion204.aperture.sandbox2.data.jdbc.connection;

import com.explosion204.aperture.sandbox2.config.condition.JdbcTemplateDisabledCondition;
import com.explosion204.aperture.sandbox2.exception.JdbcConnectionException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
@Conditional(JdbcTemplateDisabledCondition.class)
public class ConnectionFactory {
  @Value("${db.url}")
  private String url;

  @Value("${db.user}")
  private String username;

  @Value("${db.password}")
  private String password;

  @Value("${db.driver}")
  private String driver;

  private final Properties properties = new Properties();

  @SneakyThrows
  @PostConstruct
  void init() {
    Class.forName(driver);
    properties.put("url", url);
    properties.put("user", username);
    properties.put("password", password);
    properties.put("driver", driver);
  }

  public Connection createConnection() {
    try {
      return DriverManager.getConnection(url, properties);
    } catch (SQLException e) {
      throw new JdbcConnectionException();
    }
  }
}
