# MyBatis와 스프링 연동

## JDBC 연결 

- JDBC 연결을 하려면 JDBC Driver가 필요합니다. 
- mvn 저장소에서 MySQL을 사용하는 경우라면 mysql connector, Oracle이라면 ojdbc11 검색어로 검색하여 build.gradle에 의존성을 다음과 같이 추가합니다.

> MySQL 연동 

```groovy
...
dependencies {
    ...
    runtimeOnly 'com.mysql:mysql-connector-j:8.0.33'
    ...
}
...
```

> Oracle 연동

```groovy
...
dependencies {
    ... 
    
    runtimeOnly 'com.oracle.database.jdbc:ojdbc11:23.3.0.23.09'
    
    ...
}
...
```

## JDBC 테스트 코드

- JDBC 드라이버가 정상적으로 추가되었고, 데이터베이스의 연결이 가능하다면 이를 눈으로 확인할 수 있게 테스트 코드를 작성합니다.
- 테스트 코드가 있는 폴더에 JDBCTests 클래스를 다음과 같이 추가합니다.

> MySQL

```java
package org.choongang;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCTests {

    @BeforeAll
    static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnection() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/teamb2", "teamb2", "_aA123456")) {
            System.out.println(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

> Oracle

```java 
package org.choongang;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCTests {

    @BeforeAll
    static void init() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnection() {
        try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "TEAMB2", "_aA123456")) {
            System.out.println(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

- 테스트 코드는 JDBC 드라이버만으로 구현해서 먼저 테스트해야 합니다. 
- 데이터베이스 연결이 가능하다면 정상적으로 데이터베이스가 연결된 Connection 객체가 출력됩니다.

## 커넥션 풀 설정

> 일반적으로 여러 명의 사용자를 동시에 처리해야 하는 웹 애플리케이션의 경우 데이터베이스 연결을 이용할 때는 <code>커넥션 풀(Connection Pool)</code>를 이용하므로, 아예 스프링에 커넥션 풀을 등록해서 사용하는 것이 좋습니다. 
> Java에서는 <code>DataSource</code>라는 인터페이스를 통해서 커넥션 풀을 사용합니다. <code>DataSource</code>를 통해 매번 데이터베이스와 연결하는 방식이 아닌, 미리 연결을 맺어주고 반환하는 구조를 이용하여 성능 향상을 꾀합니다.

### Tomcat JDBC를 사용한 설정 

> build.gradle에 <code>tomcat-jdbc</code> 의존성 추가 

```groovy
dependencies {
    def spring_version= '6.1.1'

    ...
    
    implementation 'org.apache.tomcat:tomcat-jdbc:10.1.16'
    
    ...
}
```

> <code>DataSource</code> 설정
> org.choongang.cofigs.DbConfig.java

> MySQL 기준

```java
package org.choongang.configs;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DbConfig {
    @Bean
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
}
```

> Oracle 기준

```java
package org.choongang.configs;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DbConfig {
    @Bean
    public DataSource dataSource() {
       DataSource ds = new DataSource();

       ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
       ds.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
       ds.setUsername("TEAMB2");
       ds.setPassword("_aA123456");

       ds.setMaxActive(10);
       ds.setInitialSize(2);
       ds.setTestWhileIdle(true);
       ds.setMinEvictableIdleTimeMillis(60 * 1000);
       ds.setTimeBetweenEvictionRunsMillis(5 * 1000);

       return ds;
    }
}
```

> 연결 테스트 - <code>DataSourceTests</code> 클래스를 다음과 같이 작성합니다.

```java
package org.choongang;


import org.choongang.configs.DbConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=DbConfig.class)
public class DataSourceTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() {
        try (Connection con = dataSource.getConnection()) {
            System.out.println(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
     }
}
```

### HikariCP를 사용한 설정 

> 커넥션 풀은 여러 종류가 있고 <code>spring-jdbc</code> 라이브러리를 이용하는 방식도 있지만, HikariCP가 최근 부터 유행하고 있습니다. HikariCP는 스프링 부트 2.0 버전 이후 부터 기본 사용될 만큼 빠르게 퍼지고 있습니다.

> build.gradle에 <code>HikariCP</code> 의존성 추가

```groovy
dependencies {
    def spring_version= '6.1.1'
    ...
    
    implementation 'com.zaxxer:HikariCP:5.1.0'
    
    ...
}
```

> <code>DataSource</code> 설정
> org.choongang.cofigs.DbConfig2.java

> MySQL 기준

```java
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
```

> Oracle 기준
 
```java
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
config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
config.setUsername("TEAMB2");
config.setPassword("_aA123456");

        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
```

> 연결 테스트 - <code>DataSourceTests2</code> 클래스를 다음과 같이 작성합니다.

```java
package org.choongang;

import org.choongang.configs.DbConfig2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= DbConfig2.class)
public class DataSourceTests2 {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() {
        try (Connection con = dataSource.getConnection()) {
            System.out.println(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

