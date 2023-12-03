package org.choongang.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {

    @Select("SELECT CURRENT_TIMESTAMP")
    String getTime();
}
