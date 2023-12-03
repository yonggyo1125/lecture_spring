package org.choongang.configs;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DbConfig {
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
       DataSource ds = new DataSource();

       ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
       ds.setUrl("jdbc:mysql://localhost:3306/teamb2");
       ds.setUsername("teamb2");
       ds.setPassword("_aA123456");

       ds.setMaxActive(10);
       ds.setInitialSize(2);
       ds.setTestWhileIdle(true);
       ds.setMinEvictableIdleTimeMillis(60 * 1000);
       ds.setTimeBetweenEvictionRunsMillis(5 * 1000);

       return ds;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean.getObject();
    }
}
