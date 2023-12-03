package org.choongang;

import org.choongang.configs.DbConfig;
import org.choongang.mapper.TimeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbConfig.class)
public class TimeMapperTests {

    @Autowired
    private TimeMapper timeMapper;

    @Test
    public void testGetTime() {
        System.out.println(timeMapper.getClass().getName());
        System.out.println(timeMapper.getTime());
    }

    @Test
    public void testGetTime2() {
        System.out.println(timeMapper.getClass().getName());
        System.out.println(timeMapper.getTime());
    }
}
