# JDBC와 커넥션 풀 설정

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

# MyBatis와 스프링 연동

## MyBatis

- MyBatis(https://mybatis.org/mybatis-3/)는 흔히 <code>SQL 매핑(mapping)</code> 프레임워크로 분류
- 개발자들은 JDBC 코드의 복잡하고 지루한 작업을 피하는 용도로 많이 사용합니다. 
- 전통적인 JDBC 프로그래밍의 구조와 비교해 보면 MyBatis의 장점을 파악할 수 있습니다.

| 전통적인 JDBC 프로그램 | MyBatis             |
|----------------|---------------------|
|- 직접 Connection을 맺고 마지막에 close()|자동으로 Connection close() 가능|
|PreparedStatement의 setXXX()등에 대한 모든 작업을 개발자가 처리|#{prop}과 같이 속성을 지정하면 내부적으로 자동 처리|
|SELECT의 경우 직접 ResultSet 처리|리턴 타입을 지정하는 경우 자동으로 객체 생성 및 ResultSet 처리|

- MyBatis는 기존의 SQL을 그대로 활용할 수 있다는 장접이 있고, 진입 장벽이 낮은 편이어서 JDBC의 대안으로 많이 사용합니다.
- 스프링 프레임워크의 특징 중 하나는 다른 프레임워크를 배척하는 대신에 다른 프레임워크들과의 연동을 쉽게 하는 추가적인 라이브러리들이 많다는 것입니다. 
- MyBatis 역시 <code>mybatis-spring</code>이라는 라이브러리를 통해서 쉽게 연동작업을 처리할 수 있습니다.

### MyBatis 관련 라이브러리 추가

- <code>spring-jdbc/spring-tx</code> : 스프링에서 데이터베이스 처리와 트랜잭션 처리(해당 라이브러리들은 MyBatis와 무관하게 보이지만 추가하지 않은 경우에 에러가 발생하므로 주의합니다.)
- <code>mybatis/mybatis-spring</code> : MyBatis와 스프링 연동용 라이브러리

> build.gradle

```groovy
dependencies {
    def spring_version= '6.1.1'
    
    ...

    // MyBatis
    implementation 'org.mybatis:mybatis:3.5.14'
    implementation 'org.mybatis:mybatis-spring:3.0.3'

    // Spring jdbc
    implementation "org.springframework:spring-jdbc:${spring_version}"

    ...
}
```

### SQLSessionFactory

- MyBatis에서 가장 핵심적인 객체는 <code>SqlSession</code>이라는 존재와 <code>SqlSessionFactory</code> 입니다.
- <code>SqlSessionFactory</code>의 이름에서 보듯이 내부적으로 <code>SqlSession</code>이라는 것을 만들어내는 존재
- 개발에서는 <code>SqlSession</code>을 통해서 <code>Connection</code>을 생성하거나 원하는 SQL을 전달하고, 결과를 리턴 받는 구조로 작성하게 됩니다. 
- 스프링에 <code>SqlSessionFactory</code>를 등록하는 작업은 <code>SqlSessionFactoryBean</code>을 이용합니다.
- 패키지명을 보면 MyBatis의 패키지가 아니라 스프링과 연동 작업을 처리하는 <code>mybatis-spring</code>라이브러리의 클래스임을 알 수 있습니다.

> org.choongang.cofigs.DbConfig.java

```java
package org.choongang.configs;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DbConfig {
    
    ...

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean.getObject();
    }
}

```

> <code>SqlSessionFactoryBean</code>을 이용해서 <code>SqlSession</code>을 사용해 보는 테스트는 기존의 DataSourceTests 클래스에 추가해서 확인합니다.

```java
package org.choongang;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    ...

    @Test
    public void testMyBatis() {
        try (SqlSession session = sqlSessionFactory.openSession();
            Connection con = session.getConnection()) {

            System.out.println(session);
            System.out.println(con);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
```

> <code>testMyBatis()</code>는 설정된 <code>SqlSessionFactory</code> 인터페이스 타입의 <code>SqlSessionFactoryBean</code>을 이용해서 생성하고 이를 이용해서 <code>Connection</code>까지를 테스트 합니다. 

## 스프링과의 연동 처리 

> <code>SqlSessionFactory</code>를 이용해서 코드를 작성해도 직접 <code>Connection</code>을 얻어서 JDBC 코딩이 가능하지만, 좀 더 편하게 작업하기 위해서는 SQL을 어떻게 처리할 것인지를 별도의 설정을 분리해 주고, 자동으로 처리되는 방식을 이용하는 것이 좋습니다. 이를 위해서는 MyBatis의 <code>Mapper</code>라는 존재를 작성해 줘야 합니다.
> 
> <code>Mapper</code>는 쉽게 말해서 SQL과 그에 대한 처리를 지정하는 역할을 합니다. <code>MyBatis-Spring</code>을 이용하는 경우에는 <code>Mapper</code>를 <code>XML</code>과 <code>인터페이스</code> + <code>어노테이션</code>의 형태로 작성할 수 있습니다.

### Mapper 인터페이스

> Mapper를 작성하는 방법은 XML을 이용할 수도 있지만 최소한의 코드를 작성하는 <code>Mapper 인터페이스</code>를 사용할 수도 있습니다.
> 
> org.choongang.mapper라는 패키지를 만들고, TimeMapper라는 인터페이스를 추가합니다.
> 
> TimeMapper 인터페이스에는 MyBatis의 어노테이션을 이용해서 SQL을 메서드에 추가합니다.


> MySQL 기준 

```java
package org.choongang.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {

    @Select("SELECT CURRENT_TIMESTAMP")
    String getTime();
}
```

> Oracle 기준

```java
package org.choongang.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {

    @Select("SELECT SYSDATE FROM DUAL")
    String getTime();
}

```

#### Mapper 설정

> Mapper를 작성해 주었다면 MyBatis가 동작할 때 Mapper를 인식할 수 있도록 클래스 선언부에 <code>mybatis-spring</code>에서 사용하는 <code>@MapperScan</code>를 이용해서 처리합니다. 

```java
package org.choongang.configs;

...

@Configuration
@MapperScan("org.choongang.mapper")
public class DbConfig {
    ...
}
```

#### Mapper 테스트 

> <code>MyBatis-Spring</code>은 <code>Mapper 인터페이스</code>를 사용해서 실제 SQL 처리가 되는 클래스를 자동으로 생성합니다. 따라서 개발자들은 인터페이스와 SQL만을 작성하는 방식으로도 모든 JDBC 처리를 끝낼 수 있습니다.
> 
> 작성한 TimeMapper를 테스트하는 코드는 src/test/java 하위 org.choongang.TimeMapperTests라는 클래스를 생성해서 처리합니다.

```java
package org.choongang;

import org.choongang.configs.DbConfig;
import org.choongang.mapper.TimeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbConfig.class)
public class TimeMapperTests {

    @Autowired
    private TimeMapper timeMapper;

    @Test
    public void testGetTime() {
        System.out.println(timeMapper.getClass().getName());
        System.out.println(timeMapper.getTime());
    }
}
```

> <code>TimeMapperTests</code> 클래스는 <code>TimeMapper</code>가 정상적으로 사용이 가능한지를 알아보기 위한 테스트 코드입니다. 위의 코드가 정상적으로 동작한다면 스프링 내부에는 TimeMapper 타입으로 만들어진 스프링 객체(빈)가 존재한다는 뜻이 됩니다.
> 
> 위 코드에서 <code>timeMapper.getClass().getName()은 실제 동작하는 클래스의 이름을 확인해 주는데 실행 결과를 보면 개발 시 인터페이스만 만들어 주었는데 내부적으로 적당한 클래스가 만들어진 것을 확인할 수 있습니다.</code>
> 우선 스프링이 인터페이스를 이용해서 객체를 생성한다는 사실에 주목하세요.


### XML 매퍼와 함께 사용

> MyBatis를 이용해서 SQL을 처리할 때 어노테이션을 이용하는 방식이 압도적으로 편리하기는 하지만, SQL이 복잡하거나 길어지는 경우에는 어노테이션 보다는 XML을 이용하는 방식을 더 선호하게 됩니다. 다행히도 <code>MyBatis-Spring</code>의 경우 <code>Mapper 인터페이스</code>와 <code>XML</code>을 동시에 이용할 수 있습니다.
> 
> XML을 작성해서 사용할 때에는 XML 파일의 위치와 XML 파일에 지정하는 namespace 속성이 중요한데, XML 파일 위치의 경우 Mapper 인터페이스가 있는 곳에 같이 작성하거나 <code>src/main/resources</code> 구조에 XML을 저장할 폴더를 생성할 수 있습니다. XML 파일을 만들 때 이름에 대한 규칙은 없지만, 가능하다면 Mapper 인터페이스와 같은 이름을 이용하는 것이 가독성을 높여줍니다.
> 
> <code>src/main/resources</code> 폴더 내 org 폴더와 하위 choongang 폴더, mapper 폴더를 생성하고 <code>TimeMapper.xml</code>를 생성합니다.