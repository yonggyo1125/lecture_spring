package org.choongang.mapper;

import org.choongang.domain.BoardVO;

import java.util.List;

public interface BoardMapper {

    //@Select("SELECT * FROM BOARD WHERE BNO > 0")
    List<BoardVO> getList();

    void insert(BoardVO board);

    void insertSelectKey(BoardVO board);

    BoardVO read(Long bno);

    int delete(Long bno);

    int update(BoardVO board);
}
