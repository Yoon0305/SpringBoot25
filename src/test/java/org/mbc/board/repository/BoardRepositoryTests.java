package org.mbc.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    // 영속성 계층에 테스트 용
    
    @Autowired // 생성자 자동 주입
    private BoardRepository boardRepository;
    
    @Test
    public void testInsert(){
        //데이터 베이스에 데이터 주입(c) 테스트 코드
        IntStream.rangeClosed(1,100).forEach(i -> {
            // i변수에 1~100까지 100개의 정수를 반복해서 생성
            Board board = Board.builder()
                    .title("title..."+i) // board.setTitle()
                    .content("content..."+i) // board.setContent()
                    .writer("user"+(i % 10)) // board.setWriter()
                    .build(); // @Builder (setter대신 간단하고 가독성 좋은 형태로 이용하기 위함)
            /*log.info((board));*/
            Board result = boardRepository.save(board); //db에 기록하는 코드
            //                            .save 메서드는 jpa에서 상속한 메서드로, 값을 저장하는 용도
            //                                         이미 값이 있으면, update를 진행한다.
            log.info("게시물 번호 출력: "+result.getBno()+"게시물의 제목: "+result.getTitle());


        } // forEach 종료
        ); // IntStream 종료
    }// testInsert 메서드 종료

    @Test
    public void testSelect() {
        Long bno = 100L; // 게시물 번호가 100번인 객체를 확인해보자.

        Optional<Board> result = boardRepository.findById(bno);
        // null값이 나올 경우를 대비한 객체 (NPE)
        //                                       findById(bno) = select * from board where bno = bno;
        //Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        b1_0.bno=?

        Board board = result.orElseThrow(); // 값이 있으면 넣어라

        log.info(bno+"에 데이터 베이스에 존재합니다.");
        log.info(board);
    }  // testSelect method 종료
    
    @Test
    public void testUpdate() {
        
        Long bno = 100L; // 100번 게시물을 가져와서 수정 후 테스트 종료

        Optional<Board> result = boardRepository.findById(bno); // bno를 찾아서 result에 넣는다.

        Board board = result.orElseThrow(); // 가져온 값이 있으면 board type object에 넣는다.
        
        board.change("수정 테스트 제목","수정 테스트 내용"); // 제목과 내용만 수정할 수 있는 메서드

        boardRepository.save(board); // .save라는 메서드는 pk값이 없으면 insert 하고, pk값이 있으면 update 한다.

        //Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        b1_0.bno=?
        //Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        b1_0.bno=?
        //Hibernate:
        //    update
        //        board
        //    set
        //        content=?,
        //        moddate=?,
        //        title=?,
        //        writer=?
        //    where
        //        bno=?
        // 첫번째로는 진짜 찾고, 찾은 것을 set으로 update


    }

    @Test
    public void testDelete() {

        Long bno = 1L;

        boardRepository.deleteById(bno);
        //             .deleteById(bno) -> delete from board where bno = bno
        // Hibernate: 
        //    selectbootex
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer 
        //    from
        //        board b1_0 
        //    where
        //        b1_0.bno=?
        //Hibernate: 
        //    delete 
        //    from
        //        board 
        //    where
        //        bno=?
        // 이번에도 1번째 게시물 bno=1을 찾은 후, 삭제
        
    }

    @Test
    public void testPaging() {
            // .findAll() 은 모든 리스트를 출력하는 메서드, select * from board;
            // 전체 스트에 페이징과 정렬 기법도 추가해보자.

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

                Page<Board> result = boardRepository.findAll(pageable);

                log.info("total count: "+result.getTotalElements()); //99
                log.info("total pages: "+result.getTotalPages());    //10
                log.info("page number: "+result.getNumber());        //0
                log.info("page size: "+result.getSize());            //10
                log.info("next page exist: "+result.hasNext());      //true
                log.info("start page exist: "+result.isFirst());     //true


        // 콘솔에 결과를 출력해보자.

        List<Board> boardList = result.getContent(); //paging 처리된 내용을 가져와라

       boardList.forEach(board -> log.info(board));
        // forEach는 index를 사용하지 않고 앞에서부터 객체를 리턴함.
        //               board -> log.info(board)
        //                  람다식 : 1개의 명령어가 있을 때 활용
    }

    //QueryDSL 테스트 진행

    @Test
    public void testSearch1(){

        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());

         // boardRepository.search1(pageable); -> paging기법을 이용해서 title=1 값을 찾아오나?

        Page<Board> result = boardRepository.search1(pageable);

        result.getContent().forEach(board -> log.info(board));

        //Hibernate: 검색조건이 1개일 때 (where이하 title절)
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        b1_0.title like ? escape '!' -> like 1

        
        //Hibernate: 검색조건이 2개일 때 (where 이하 title, content) , booleanbuilder applied
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer 
        //    from
        //        board b1_0 
        //    where
        //        (
        //            b1_0.title like ? escape '!' 
        //            or b1_0.content like ? escape '!'
        //        ) 
        //        and b1_0.bno>?  -> query.where(board.bno.gt(0L)
        //    order by
        //        b1_0.bno desc 
        //    limit               -> paging 처리  . this.getQuerydsl().applyPagination(pageable, query)
        //        ?, ?
        //Hibernate: 
        //    select
        //        count(b1_0.bno) 
        //    from
        //        board b1_0 
        //    where
        //        (
        //            b1_0.title like ? escape '!' 
        //            or b1_0.content like ? escape '!'
        //        ) 
        //        and b1_0.bno>?
    }

    @Test
    public void testSearchAll(){
        // front에서 t가 선택되면 title, c -> content, w -> writer

        String[] types ={"t","w"}; // 검색 조건

        String keyword = "10"; // 검색 단어

        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        (
        //            b1_0.title like ? escape '!'
        //            or b1_0.content like ? escape '!'
        //            or b1_0.writer like ? escape '!'
        //        )
        //        and b1_0.bno>?
        //    order by
        //        b1_0.bno desc
        //    limit
        //        ?, ?
        //Hibernate:
        //    select
        //        count(b1_0.bno)
        //    from
        //        board b1_0
        //    where
        //        (
        //            b1_0.title like ? escape '!'
        //            or b1_0.content like ? escape '!'
        //            or b1_0.writer like ? escape '!'
        //        )
        //        and b1_0.bno>?

        log.info("total count: "+result.getTotalElements()); //99
        log.info("total pages: "+result.getTotalPages());    //10
        log.info("page number: "+result.getNumber());        //0
        log.info("page size: "+result.getSize());            //10
        log.info("next page exist: "+result.hasNext());      //true
        log.info("start page exist: "+result.isFirst());     //true

        result.getContent().forEach(board -> log.info(board));

    }


}// Class 종료
