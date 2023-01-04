package com.explosion204.aperture.sandbox2.config;

import com.explosion204.aperture.sandbox2.config.condition.JdbcTemplateEnabledCondition;
import com.explosion204.aperture.sandbox2.data.entity.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = "com.explosion204.aperture.sandbox2")
@PropertySources({
        @PropertySource("classpath:/db.properties"),
        @PropertySource("classpath:/app.properties")
})
public class ApplicationConfiguration implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("css/**", "images/**", "js/**")
            .addResourceLocations("classpath:/css/", "classpath:/images/", "classpath:/js/");
  }

  @Bean
  public ServletContextTemplateResolver templateResolver(ServletContext context) {
    final ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
    templateResolver.setPrefix("/WEB-INF/views/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);

    return templateResolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine(ServletContextTemplateResolver templateResolver) {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);

    return templateEngine;
  }

  @Bean
  public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
    final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine);
    viewResolver.setOrder(1);

    return viewResolver;
  }

  @Bean
  @Conditional(JdbcTemplateEnabledCondition.class)
  public DataSource dataSource(
          @Value("${db.driver}") String driver,
          @Value("${db.url}") String url,
          @Value("${db.user}") String username,
          @Value("${db.password}") String password,
          @Value("${db.pool.min-size}") int poolMinSize,
          @Value("${db.pool.max-size}") int poolMaxSize
  ) {
    final BasicDataSource dataSource = new BasicDataSource();

    dataSource.setDriverClassName(driver);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMinIdle(poolMinSize);
    dataSource.setMaxTotal(poolMaxSize);

    return dataSource;
  }

  @Bean
  @Conditional(JdbcTemplateEnabledCondition.class)
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  @Conditional(JdbcTemplateEnabledCondition.class)
  public BeanPropertyRowMapper<User> userRowMapper() {
    return BeanPropertyRowMapper.newInstance(User.class);
  }

  @Bean
  @Conditional(JdbcTemplateEnabledCondition.class)
  public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  @Conditional(JdbcTemplateEnabledCondition.class)
  public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    return new TransactionTemplate(transactionManager);
  }
}
