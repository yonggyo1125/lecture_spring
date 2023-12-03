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
