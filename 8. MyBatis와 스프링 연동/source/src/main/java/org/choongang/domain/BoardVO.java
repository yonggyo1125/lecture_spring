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
