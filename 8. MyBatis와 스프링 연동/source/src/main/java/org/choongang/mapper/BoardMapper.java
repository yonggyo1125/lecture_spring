package org.choongang.mapper;

import org.apache.ibatis.annotations.Select;
import org.choongang.domain.BoardVO;

import java.util.List;

public interface BoardMapper {

    @Select("SELECT * FROM BOARD WHERE BNO > 0")
    public List<BoardVO> getList();
}
