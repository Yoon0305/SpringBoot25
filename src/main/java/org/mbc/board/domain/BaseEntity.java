package org.mbc.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 공통적인 최상위 클래스
@EntityListeners(value = AuditingEntityListener.class) // 감시용 클래스 명시
@Getter // 날짜 처리용 (sysdate) -> database 날짜를 가져오기만 하겠다.(보안 문제)
abstract class BaseEntity { // 추상적인 abstract -> 자체 실행문 x
    // 모든 테이블에 공통적으로 사용되는 필드를 만든다.

    @CreatedDate // 생성일 용
    @Column(name = "regdate", updatable = false) //  Column명 지정 + 수정금지 옵션 추가
    private LocalDateTime regDate; //등록일

    @Column(name = "moddate") // Column명 지정
    @LastModifiedDate // 수정일 용
    private LocalDateTime modDate; //수정일

}
