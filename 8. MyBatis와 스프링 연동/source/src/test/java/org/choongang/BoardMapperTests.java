package org.choongang;

import org.choongang.configs.DbConfig;
import org.choongang.domain.BoardVO;
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

    @Test
    public void testInsert() {
        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글");
        board.setContent("새로 작성하는 내용");
        board.setWriter("newbie");

        mapper.insert(board);

        System.out.println(board);
    }

    @Test
    public void testInsertSelectKey() {
        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글 select key");
        board.setContent("새로 작성하는 내용 select key");
        board.setWriter("newbie");

        mapper.insertSelectKey(board);

        System.out.println(board);
    }

    @Test
    public void testRead() {
        BoardVO board = mapper.read(3L);

        System.out.println(board);
    }

    @Test
    public void testDelete() {
        System.out.printf("DELETE COUNT: %d%n", mapper.delete(3L));
    }

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
}
