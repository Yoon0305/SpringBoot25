package org.mbc.board.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Comment;

@Entity // 데이터베이스 관련 객체임을 선언
@Table(name="tbl_memo") // 데이터베이스 테이블 명을 선언
@ToString
@Getter
@Builder // 빌더 페턴 사용
@AllArgsConstructor
@NoArgsConstructor
public class Memo {


    @Id // pk 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //GenerationType.IDENTITY -> pk를 자동으로 생성하고자 함.(key 생성 전략)
    // 만일 연결되는 db가 오라클이면, 번호를 위한 별도 테이블을 생성(sequence 객체)
    // mySql이나 mariaDB면 auto increment를 기본으로 사용해서 새로운 레코드가 생성될 때 다른번호를 열어서
    // GenerationType.type.AUTO
    // GenerationTYPE.SEQUENCE
    // GenerationType.TABLE
    private Long mno;

    @Column(length=200, nullable = false)
    private String memoText;

}
