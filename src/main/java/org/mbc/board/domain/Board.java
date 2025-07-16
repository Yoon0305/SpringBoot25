package org.mbc.board.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity // 데이터 베이스 테이블 관련 객체
@Getter
@Builder // 빌더 패턴 (Setter 대신 활용)
@AllArgsConstructor // 모든 필드값으로 생성자 만들기
@NoArgsConstructor // 기본 생성자
@ToString // 객체 주소가 아닌 값을 출력
public class Board extends BaseEntity { //extends BaseEntity (날짜 관련 jpa 연결)

    @Id // pk로 선언 (NotNull,Unique, indexing)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 번호 생성용 (Auto sequence increment)
    private Long bno;           // 게시물 번호

    @Column(length = 500, nullable = false) // notNull
    private String title;       // 게시물 제목

    @Column(length = 2000, nullable = false)
    private String content;     // 게시물 내용

    @Column(length =50, nullable = false)
    private String writer;      // 게시물 작성자

    //  Hibernate:
    //    create table board (
    //        bno bigint not null auto_increment,
    //        content varchar(255),
    //        title varchar(255),
    //        writer varchar(255),
    //        primary key (bno)
    //    ) engine=InnoDB

    // Base Entity 상속 후 변경 코드
    //Hibernate:
    //alter table if exists board
    //modify column content varchar(2000) not null
    //Hibernate:
    //alter table if exists board
    //modify column title varchar(500) not null
    //Hibernate:
    //alter table if exists board
    //modify column writer varchar(50) not null

    public void change(String title, String content) {
        // 제목과 내용만 수정하는 method (Setter 대신 builder를 사용하기 때문에 이용)
        this.title = title;
        this.content = content;
    }


}
