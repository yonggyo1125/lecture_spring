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
        VALUES (#{title}, #{content}, #{writer})
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
> 
> 우선 insert()에 대한 테스트 코드를 src/test/java 내에 <code>BoardMapperTests</code> 클래스에 새로운 메서드로 작성해보면 다음과 같이 작성할 수 있습니다.

```java
    @Test
    public void testInsert() {
        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글");
        board.setContent("새로 작성하는 내용");
        board.setWriter("newbie");

        mapper.insert(board);

        System.out.println(board);
    }
```

> 테스트 결과의 마지막을 살펴보면 BoardVO 클래스의 toString()의 결과가 출력되는 것을 볼 수 있는데, bno의 값이 null로 비어 있는 것을 확인할 수 있습니다.
>
> <code>@SelectKey</code>를 이용하는 경우 테스트 코드는 다음과 같습니다.

```java
    @Test
    public void testInsertSelectKey() {
        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글 select key");
        board.setContent("새로 작성하는 내용 select key");
        board.setWriter("newbie");

        mapper.insertSelectKey(board);

        System.out.println(board);
    }
```

> 실행되는 결과를 살펴보면 'SELECT SEQ_BOARD.nextval FROM DUAL'과 같은 쿼리가 먼저 실행되고 여기서 생성된 결과를 이용해서 bno 값으로 처리되는 것을 볼 수 있습니다. 
> 
> BoardMapper의 insertSelectKey()의 <code>@Insert</code> 문의 SQL을 보면 'INSERT INTO BOARD (BNO, TITLE, CONTENT, WRITER) VALUES (#{bno}, #{title}, #{content}, #{writer})'와 같이 파라미터로 전달되는 BoardVO의 bno 값을 사용하게 되어 있습니다.
> 
> 테스트 코드의 마지막 부분을 보면 BoardVO 객체의 bno 값이 이전과 달리 지정된 것을 볼 수 있습니다(시퀀스의 값이므로 현재 테스트하는 환경마다 다른 값이 나옵니다. 시퀀스 값은 중복 없는 값을 위한 것일 뿐 다른 의미가 있지 않습니다.). <code>@SelectKey</code>를 이용하는 방식은 SQL을 한 번 더 실행하는 부담이 있기는 하지만 자동으로 추가되는 PK 값을 확인해야 하는 상황에서 유용하게 사용될 수 있습니다.

 ### READ(SELECT) 처리

> INSERT가 된 데이터를 조회하는 작업은 PK를 이용해서 처리하므로 BoardMapper의 파라미터 역시 BoardVO 클래스의 bno 타입 정보를 이용해서 처리합니다.
        
#### org.choongang.mapper.BoardMapper 인터페이스 일부

```java
public interface BoardMapper {

    ...

    BoardVO read(Long bno);
}
```

#### BoardMapper.xml에 추가되는 \<select\>

```xml
    <select id="read" resultType="org.choongang.domain.BoardVO">
        SELECT * FROM BOARD WHERE BNO = #{bno}
    </select>
```

> MyBatis는 Mapper 인터페이스의 리턴 타입에 맞게 select의 결과를 처리하기 때문에 BOARD의 모든 컬럼은 BoardVO의 'bno, title, content, writer, regdate, updateDate' 속성값으로 처리됩니다.
> 
> 좀 더 엄밀하게 말하면 MyBatis는 bno라는 컬럼이 존재하면 인스턴스의 'setBno()'를 호출하게 됩니다. MyBatis의 모든 파라미터와 리턴 타입의 처리는 get파라미터명(), set컬럼명()의 규칙으로 호출됩니다. 다만 위와 같이 #{속성}이 1개만 존재하는 경우에는 별도의 get파라미터명()을 사용하지 않고 처리됩니다.
> 
> 현재 테이블에 존재하는 데이터의 bno 컬럼의 값을 이용해서 테스트 코드를 통해 확인합니다.

#### BoardMapperTests 클래스의 일부

```java
    @Test
    public void testRead() {
        BoardVO board = mapper.read(3L);

        System.out.println(board);
    }
```

> mapper.read()를 호출할 경우에는 현재 테이블에 있는 데이터의 bno 값이 존재하는지 여부를 반드시 확인해야 합니다.


### DELETE 처리

> 특정한 데이터를 삭제하는 작업 역시 PK 값을 이용해서 처리하므로 조회하는 작업과 유사하게 처리합니다. 등록, 삭제, 수정과 같은 DML 작업은 '몇 건의 데이터가 삭제(혹은 수정)되었는지'를 반환할 수 있습니다.

#### BoardMapper 인터페이스 일부

```java 

public interface BoardMapper {

    ...

    int delete(Long bno);
}
```

#### BoardMapper.xml의 일부

```xml
    <delete id="delete">
        DELETE FROM BOARD WHERE BNO = #{bno}
    </delete>
```

> delete()의 메서드 리턴 타입은 int로 지정해서 만일 정상적으로 데이터가 삭제되면 1이상의 값을 가지도록 작성합니다. 테스트 코드는 현재 테이블에 존재하는 번호의 데이터를 삭제해 보고 '1'이라는 값이 출력되는지 확인합니다. 만일 해당 번호의 게시물이 없다면 '0'이 출력됩니다.


#### BoardMapperTests 클래스의 일부

```java
    @Test
    public void testDelete() {
        System.out.printf("DELETE COUNT: %d%n", mapper.delete(3L));
    }
```

### UPDATE 처리

> 마지막으로 UPDATE를 처리합니다. 게시물의 업데이트는 제목, 내용, 작성자를 수정한다고 가정합니다. 업데이트할 때는 최종 수정시간을 데이터베이스 내 현재 시간으로 수정합니다. UPDATE는 DELETE와 마찬가지로 '몇 개의 데이터가 수정되었는가'를 처리할 수 있게 int 타입으로 메서드를 설계할 수 있습니다.

#### BoardMapper 인터페이스 일부

```java
public interface BoardMapper {

    ...

    int update(BoardVO board);
}

```

#### BoardMapper.xml의 일부

> MySQL
```xml
    <update id="update">
        UPDATE BOARD
            SET
                TITLE = #{title},
                CONTENT = #{content},
                WRITER = #{writer},
                UPDATEDATE = CURRENT_TIMESTAMP
        WHERE BNO = #{bno}
    </update>
```

> Oracle 
```xml
    <update id="update">
        UPDATE BOARD
            SET
                TITLE = #{title},
                CONTENT = #{content},
                WRITER = #{writer},
                UPDATEDATE = SYSDATE
        WHERE BNO = #{bno}
    </update>
```

> SQL에서 주의 깊게 봐야 하는 부분은 update 컬럼이 최종 수정시간을 의미하는 컴럼이기 때문에 현재 시간으로 변경해 주고 있다는 점과, REGDATE 컬럼은 최초 생성 시간이므로 건드리지 않는다믄 점입니다. #{title}과 같은 부분은 파라미터로 전달된 BoardVO 객체의 getTitle()과 같은 메서드들을 호출해서 파라미터들이 처리됩니다.
>
> 테스트 코드는 read()를 이용해서 가져온 BoardVO 객체의 일부를 수정하는 방식이나 직접 BoardVO 객체를 생성해서 처리할 수 있습니다. 

#### BoardMapperTests 클래스 일부

```java
    @Test
    public void testUpdate() {
        BoardVO board = new BoardVO();

        // 실행 전 존재하는 번호인지 확인할 것
        board.setBno(2L);
        board.setTitle("수정된 제목");
        board.setContent("수정된 내용");
        board.setWriter("user00");

        int count = mapper.update(board);
        System.out.printf("UPDATE COUNT : %d%n", count);
    }
```

# 동적 SQL(https://mybatis.org/mybatis-3/ko/dynamic-sql.html)

> 마이바티스의 가장 강력한 기능 중 하나는 동적 SQL을 처리하는 방법이다. JDBC나 다른 유사한 프레임워크를 사용해본 경험이 있다면 동적으로 SQL 을 구성하는 것이 얼마나 힘든 작업인지 이해할 것이다. 간혹 공백이나 콤마를 붙이는 것을 잊어본 적도 있을 것이다. 동적 SQL 은 그만큼 어려운 것이다.
>
> 동적 SQL 을 사용하는 것은 결코 파티가 될 수 없을 것이다. 마이바티스는 강력한 동적 SQL 언어로 이 상황을 개선한다.
>
> 동적 SQL 엘리먼트들은 JSTL이나 XML기반의 텍스트 프로세서를 사용해 본 사람에게는 친숙할 것이다. 마이바티스의 이전 버전에서는 알고 이해해야 할 엘리먼트가 많았다. 마이바티스 3 에서는 이를 크게 개선했고 실제 사용해야 할 엘리먼트가 반 이하로 줄었다. 마이바티스의 다른 엘리먼트의 사용을 최대한 제거하기 위해 OGNL 기반의 표현식을 가져왔다.

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

## if

> 동적 SQL 에서 가장 공통적으로 사용되는 것으로 where의 일부로 포함될 수 있다.

```xml
<select id="findActiveBlogWithTitleLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
</select>
```

> 이 구문은 선택적으로 문자열 검색 기능을 제공할 것이다. 만약에 title 값이 없다면 모든 active 상태의 Blog 가 리턴될 것이다. 하지만 title 값이 있다면 그 값과 비슷한 데이터를 찾게 될 것이다.
>
> title과 author를 사용하여 검색하고 싶다면? 먼저 의미가 좀더 잘 전달되도록 구문의 이름을 변경할 것이다. 그리고 다른 조건을 추가한다.

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

## choose, when, otherwise

> 우리는 종종 적용 할 모든 조건을 원하는 대신에 한가지 경우만을 원할 수 있다. 자바에서는 switch 구문과 유사하며 마이바티스에서는 choose 엘리먼트를 제공한다.
>
> 위 예제를 다시 사용해보자. 지금은 title만으로 검색하고 author가 있다면 그 값으로 검색된다. 둘다 제공하지 않는다면 featured 상태의 blog가 리턴된다.

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
  </choose>
</select>
```

## trim, where, set

> 앞서 예제는 악명높게 다양한 엘리먼트가 사용된 동적 SQL 이다. “if” 예제를 사용해보자.

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE
  <if test="state != null">
    state = #{state}
  </if>
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

> 어떤 조건에도 해당되지 않는다면 어떤 일이 벌어질까? 아마도 다음과 같은 SQL 이 만들어질 것이다.

```xml
SELECT * FROM BLOG
WHERE
```

> 아마도 이건 실패할 것이다. 두번째 조건에만 해당된다면 무슨 일이 벌어질까? 아마도 다음과 같은 SQL이 만들어질 것이다.

```xml
SELECT * FROM BLOG
WHERE
AND title like ‘someTitle’
```

> 이것도 아마 실패할 것이다. 이 문제는 조건만 가지고는 해결되지 않았다. 이렇게 작성했다면 다시는 이렇게 작성하지 않게 될 것이다.
>
> 실패하지 않기 위해서 조금 수정해야 한다. 조금 수정하면 아마도 다음과 같을 것이다.

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

> where 엘리먼트는 태그에 의해 컨텐츠가 리턴되면 단순히 “WHERE”만을 추가한다. 게다가 컨텐츠가 “AND”나 “OR”로 시작한다면 그 “AND”나 “OR”를 지워버린다. 
>
> 만약에 where 엘리먼트가 기대한 것처럼 작동하지 않는다면 trim 엘리먼트를 사용자 정의할 수도 있다. 예를들어 다음은 where 엘리먼트에 대한 trim 기능과 동일하다.

```xml
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```

> override 속성은 오버라이드하는 텍스트의 목록을 제한한다. 결과는 override 속성에 명시된 것들을 지우고 with 속성에 명시된 것을 추가한다.
>
> 다음 예제는 동적인 update 구문의 유사한 경우이다. set 엘리먼트는 update 하고자 하는 칼럼을 동적으로 포함시키기 위해 사용될 수 있다.

```xml
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

> 여기서 set 엘리먼트는 동적으로 SET 키워드를 붙히고 필요없는 콤마를 제거한다.
> 
> 아마도 trim 엘리먼트로 처리한다면 아래와 같을 것이다.

```xml
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
```

> 이 경우 접두사는 추가하고 접미사를 오버라이딩 한다.

## foreach

> 동적 SQL 에서 공통적으로 필요한 것은 collection 에 대해 반복처리를 하는 것이다. 종종 IN 조건을 사용하게 된다.

```xml
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  <where>
    <foreach item="item" index="index" collection="list"
        open="ID in (" separator="," close=")" nullable="true">
          #{item}
    </foreach>
  </where>
</select>
```
> foreach엘리먼트는 매우 강력하고 collection 을 명시하는 것을 허용한다. 엘리먼트 내부에서 사용할 수 있는 item, index두가지 변수를 선언한다. 이 엘리먼트는 또한 열고 닫는 문자열로 명시할 수 있고 반복간에 둘 수 있는 구분자도 추가할 수 있다.
> 
> 참고) 컬렉션 파라미터로 Map이나 배열객체와 더불어 List, Set등과 같은 반복가능한 객체를 전달할 수 있다. 반복가능하거나 배열을 사용할때 index값은 현재 몇번째 반복인지를 나타내고 value항목은 반복과정에서 가져오는 요소를 나타낸다. Map을 사용할때 index는 key객체가 되고 항목은 value객체가 된다.

## script

> 애노테이션을 사용한 매퍼 클래스에서 동적 SQL을 사용하는 경우 script 태그를 사용할 수 있다.

```xml
   @Update({"<script>",
      "update Author",
      "  <set>",
      "    <if test='username != null'>username=#{username},</if>",
      "    <if test='password != null'>password=#{password},</if>",
      "    <if test='email != null'>email=#{email},</if>",
      "    <if test='bio != null'>bio=#{bio}</if>",
      "  </set>",
      "where id=#{id}",
      "</script>"})
    void updateAuthorValues(Author author);
```

## bind

> 엘리먼트는 OGNL표현을 사용해서 변수를 만든 뒤 컨텍스트에 바인딩한다

```xml
<select id="selectBlogsLike" resultType="Blog">
  <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
  SELECT * FROM BLOG
  WHERE title LIKE #{pattern}
</select>
```

## 공식문서

https://mybatis.org/mybatis-3/ko/