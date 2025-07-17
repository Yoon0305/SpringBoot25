package org.mbc.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.mbc.board.domain.Board;
import org.mbc.board.domain.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;


public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    //                          QueryDSL용 상속                  구현체 interface 지정

    public BoardSearchImpl() { // 생성자
        super(Board.class);
    }

    @Override // 인터페이스에서 만든 메서드 -> 실행코드 작성용
    public Page<Board> search1(Pageable pageable) {
        // QueryDSL로 다중검색용 코드 추가
        // QueryDSL의 목적은 type 기반으로 코드를 이용함 -> Q도메인 Class

        QBoard board = QBoard.board; //Q도메인 객체

        JPQLQuery<Board> query = from(board); // select * from board

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // 다중 조건일때 연산자 공식에 의해서 특수기호가 먼저 계산될 때가 있음.
        // () 괄호를 이용해서 계산을 선행해서 BooleanBuilder가 이 역할을 한다.

        // query.where(board.title.contains("1")); //where title like '%1%'
        // select * from board where title like '%1%'
        // contains 포함

        booleanBuilder.or(board.title.contains("11"));    // where title like 11
        booleanBuilder.or(board.content.contains("11"));  // where content like
        // (where title like 11 or content like 11)

        query.where(booleanBuilder);        //(where title like 11 or content like 11)
        query.where(board.bno.gt(0L)); // pk를 이용해서 빠른 검색을 함 indexing. where이 추가되면 and 조건
        //(where title like 11 or content like 11) and bno > 0

        // 페이징 처리용 코드
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); // query문 실행해서 list에 담아라.

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        // interface에서 만든 abstract method를 구현하는 class
        QBoard board = QBoard.board; // QueryDSL 객체 생성
        JPQLQuery<Board> query = from(board); // select * from board

        // front에서 검색form에 keyword가 비었을 경우와, 실제 keyword를 입력한 경우
        if ((types != null && types.length > 0) && keyword != null) {
            //제목,내용,이름 값이 있고, 검색어가 있으면 됨.

            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) { // 파라미터로 넘어온 값을 String[] types
                switch (type) {
                    case "t":
                        // 제목이면
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    // 내용이면
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    // 작성자면
                } // front에서 넘어오는 String[]값을 파악하고 적용

            } // for문 종료
            query.where(booleanBuilder); // 위에서 만든 조건을 적용 where title or content or writer
        } // if문 종료
        query.where(board.bno.gt(0L)); //pk를 활용한 indexing 처리
        //where title or content or writer and bno > 0L

        this.getQuerydsl().applyPagination(pageable, query); //페이징 처리용 코드 + 쿼리문

        //Page<t> 클래스는 3가지 return type을 만들어 준다.

        List<Board> list = query.fetch(); // 쿼리문 실행

        long count = query.fetchCount(); // 검색된 게시물 수를 count에 입력





        return new PageImpl<>(list, pageable, count); // 추후에 list보내면 작동
        //      return을   검색된 결과 board
        //                         페이징 처리
        //                                    검색된 갯수

    }

}
