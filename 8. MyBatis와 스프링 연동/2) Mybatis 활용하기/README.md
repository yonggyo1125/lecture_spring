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
    public List<BoardVO> getList();
}
```

