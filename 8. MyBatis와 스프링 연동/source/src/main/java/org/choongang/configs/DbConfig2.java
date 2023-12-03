package org.choongang.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DbConfig2 {
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/teamb2");
        config.setUsername("teamb2");
        config.setPassword("_aA123456");

        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
