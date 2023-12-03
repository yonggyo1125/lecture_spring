package org.choongang.mapper;

import org.choongang.domain.BoardVO;

import java.util.List;

public interface BoardMapper {

    //@Select("SELECT * FROM BOARD WHERE BNO > 0")
    List<BoardVO> getList();
}
