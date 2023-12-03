# CRUD 구현

## 구현 준비

> BOARD 테이블 추가
> 
> MySQL 기준

```sql
CREATE TABLE BOARD (
    BNO BIGINT AUTO_INCREMENT,
    TITLE VARCHAR(200) NOT NULL,
    CONTENT VARCHAR(200) NOT NULL,
    WRITER TEXT NOT NULL,
    REGDATE DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATEDATE DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(BNO)
);
```

> Oracle 기준

```sql
CREATE TABLE BOARD (
    BNO NUMBER(10) PRIMARY KEY,
    TITLE VARCHAR2(200) NOT NULL,
    CONTENT CLOB NOT NULL,
    WRITER VARCHAR2(50) NOT NULL,
    REGDATE DATE DEFAULT SYSDATE,
    UPDATEDATE DATE DEFAULT SYSDATE
);

CREATE SEQUENCE SEQ_BOARD;
```

### BoardVO 클래스

```java
package org.choongang.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardVO {

    private Long bno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regdate;
    private LocalDateTime updateDate;
}
```

> BoardVO 클래스는 Lombok을 이용해서 getter/setter, toString() 등을 만들어내는 방식을 사용합니다. 이를 위해서 @Data 어노테이션을 적용합니다.

### Mapper 인터페이스와 Mapper XML

> MyBatis는 SQL을 처리하는데 어노테이션이나 XML을 이용할 수 있습니다. 간단한 SQL이라면 어노테이션을 이용해서 처리하는 것이 무난하지만 SQL이 점점 복잡해지고 검색과 같이 상황에 따라 다른 SQL문이 처리되는 경우에는 어노테이션은 그다지 유용하지 못하다는 단점이 있습니다. XML의 경우 단순 텍스트를 수정하는 과정만으로 처리가 끝나지만 어노테이션의 경우 코드를 수정하고 다시 빌드 하는 등의 유지 보수성이 떨어지는 이유로 기피하는 경우도 종종 있습니다. 


#### org.choongang.mapper.BoardMapper 인터페이스

> Mapper 인터페이스를 작성할 때는 리스트(select)와 등록(insert) 작업을 우선해서 작성합니다. org.choongang.mapper 패키지를 작성하고, BoardMapper 인터페이스를 추가 합니다.

```java
package org.choongang.mapper;

import org.apache.ibatis.annotations.Select;
import org.choongang.domain.BoardVO;

import java.util.List;

public interface BoardMapper {

    @Select("SELECT * FROM BOARD WHERE BNO > 0")
    List<BoardVO> getList();
}
```

#### src/test/java/org/choongang/BoardMapperTests.java 

> 작성된 BoardMapper 인터페이스를 테스트할 수 있게 테스트 환경인 'src/test/java'에 'org.choongang' 패키지를 작성하고 BoardMapperTests 클래스를 추가합니다.

```java
package org.choongang;

import org.choongang.configs.DbConfig;
import org.choongang.mapper.BoardMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbConfig.class)
public class BoardMapperTests {

    @Autowired
    private BoardMapper mapper;

    @Test
    public void testGetList(){
        mapper.getList().forEach(System.out::println);
    }
}
```

#### src/main/resources/org/choongang/mapper/BoardMapper.xml

> BoardMapperTests를 이용해서 테스트가 완료되었다면 <code>src/main/resources</code>내에 패키지와 동일한 <code>org/choongang/mapper</code> 단계의 폴더를 생성하고 XML 파일을 작성합니다(폴더를 한 번에 생성하지 말고 하나씩 생성해야 하는 점 주의).
> 
> 파일의 폴더 구조나 이름은 무방하지만 패키지와 클래스 이름과 동일하게 해주면 나중에 혼란스러운 상황을 피할 수 있습니다.


```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.choongang.mapper.BoardMapper">

    <select id="getList" resultType="org.choongang.domain.BoardVO">
        <![CDATA[
            SELECT * FROM BOARD WHERE BNO > 0
        ]]>
    </select>
    
</mapper>
```

> XML을 작성할 때는 반드시 \<mapper\>와 namespace 속성값을 Mapper 인터페이스와 동일한 이름을 주는 것에 주의하고, \<select\> 태그의 id 속성값은 메서드의 이름과 일치하게 작성합니다. resultType 속성의 값은 select 쿼리의 결과를 특정 클래스의 객체로 만들기 위해서 설정합니다. XML에 사용한 CDATA 부분은 XML에서 부등호를 사용하기 위해서 사용합니다.
> 
> XML에 SQL문이 처리되었으미 BoardMapper 인터페이스에 SQL은 제거합니다.


#### src/test/java/org/choongang/BoardMapperTests.java 

```java
public interface BoardMapper {

    //@Select("SELECT * FROM BOARD WHERE BNO > 0")
    List<BoardVO> getList();
}
```

> 인터페이스 수정 후에는 반드시 기존 테스트 코드를 통해서 기존과 동일하게 동작하는지 확인해야 합니다.


## 영속 영역의 CRUD 구현

> 웹 프로젝트는 구조에서 마지막 영역이 영속 영역이지만, 실제로 구현을 가장 먼저 할 수 있는 영역도 영속 영역입니다. 영속 영역은 기본적으로 CRUD 작업을 하기 때문에 테이블과 VO(DTO) 등 약간의 준비만으로도 비즈니스 로직과 무관하게 CRUD 작업을 작성할 수 있습니다. 
> 
> MyBatis는 내부적으로 JDBC의 PreparedStatement를 활용하고 필요한 파라미터를 처리하는 '?'에 대한 치환은 '#{속성}'을 이용해서 처리합니다.

### CREATE(INSERT) 처리

> BOARD 테이블은 PK 컬럼으로 bno를 이용하고, 시퀀스를 이용해서 자동으로 데이터가 추가될 때 번호가 만들어지는 방식을 사용합니다. 이처럼 자동으로 PK 값이 정해지는 경우에는 다음과 같은 2가지 방식으로 처리할 수 있습니다.

- INSERT만 처리되고 생성된 PK 값을 알 필요가 없는 경우
- INSERT문이 실행되고 생성된 PK 값을 알아야 하는 경우 

> BoardMapper 인터페이스에는 위의 상황들을 고려해서 다음과 같이 메서드를 추가 선택합니다.

```java
package org.choongang.mapper;

import org.choongang.domain.BoardVO;

import java.util.List;

public interface BoardMapper {

    //@Select("SELECT * FROM BOARD WHERE BNO > 0")
    List<BoardVO> getList();

    void insert(BoardVO board);
    void insertSelectKey(BoardVO board);
}
```

> BoardMapper.xml에 다음과 같은 내용을 추가합니다.

> MySQL기준 

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.choongang.mapper.BoardMapper">

    <select id="getList" resultType="org.choongang.domain.BoardVO">
        <![CDATA[
            SELECT * FROM BOARD WHERE BNO > 0
        ]]>
    </select>

    <insert id="insert">
        INSERT INTO BOARD (TITLE, CONTENT, WRITER)
        VALUES (#{title}, #{content}, #{writer})
    </insert>

    <insert id="insertSelectKey" useGeneratedKeys="true" keyProperty="bno">
        INSERT INTO BOARD (TITLE, CONTENT, WRITER)
        VALUES (${title}, #{content}, #{writer})
    </insert>

</mapper>
```

> Oracle 기준 

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.choongang.mapper.BoardMapper">

    <select id="getList" resultType="org.choongang.domain.BoardVO">
        <![CDATA[
            SELECT * FROM BOARD WHERE BNO > 0
        ]]>
    </select>

    <insert id="insert">
        INSERT INTO BOARD (BNO, TITLE, CONTENT, WRITER)
        VALUES (SEQ_BOARD.nextval, #{title}, #{content}, #{writer})
    </insert>

    <insert id="insertSelectKey">
        <selectKey keyProperty="bno" order="BEFORE" resultType="long">
            SELECT SEQ_BOARD.nextval FROM DUAL
        </selectKey>

        INSERT INTO BOARD (BNO, TITLE, CONTENT, WRITER)
        VALUES (#{bno}, ${title}, #{content}, #{writer})
    </insert>
    
</mapper>
```

> BoardMapper의 insert()는 단순히 시퀀스의 다음 값을 구해서 insert 할 때 사용합니다. insert문은 몇 건의 데이터가 변경되었는지만을 알려주기 때문에 데이터의 PK 값을 알 수는 없지만, 1번의 SQL 처리만으로 작업이 완료되는 장점이 있습니다.
> 
> insertSelectKey()는 <code>@SelectKey</code>라는 MyBatis의 어노테이션을 이용합니다. <code>@SelectKey</code>는 주로 PK 값을 미리(before) SQL을 통해서 처리해 두고 특정한 이름으로 결과를 보관하는 방식입니다. <code>@Insert</code>할 때 SQL문을 보면 #{bno}와 같이 값이 이미 처리된 결과를 이용하는 것을 볼 수 있습니다.


