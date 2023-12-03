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