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