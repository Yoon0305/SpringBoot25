package org.mbc.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder // @Builder + AllArgsConstructor + NoArgsConstructor 셋트
@AllArgsConstructor // 모든 필드 값들 생성자 생성
@NoArgsConstructor // 기본 생성자
public class BoardDTO {

    private Long bno;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
