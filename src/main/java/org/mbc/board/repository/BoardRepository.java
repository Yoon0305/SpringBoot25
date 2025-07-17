package org.mbc.board.repository;

import org.mbc.board.domain.Board;
import org.mbc.board.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch { // 다중 검색용, BoardSearch 추가

    // extends JpaRepository<entity class, pk type>
    // JpaRepository는 jpa에서 미리 만들어놓은 interface로, crud와 paging 처리, 정렬등이 존재한다.

    // test코드에서 jpaRepository에 내장된 CRUD와 Paging과 정렬 기법을 테스트 함
    // Board result = boardRepository.save(board) -> 데이터베이스에 INSERT 쿼리가 움직인다.
    // Optional<Board> result = boardRepository.findById(bno); -> bno값을 select 쿼리가 동작한다.
    // boardRepository.save(board); -> bno(pk)가 있으면 update 쿼리가 동작
    // boardRepository.deleteById(bno); -> bno가 있으면 delete 쿼리가 동작한다

    // 쿼리 메서드 우리가 만드는 메서드의 이름이 쿼리문이 된다.
    // https://https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    // 단점, 실제로 사용하려면 상당히 길고 복잡한 메서드 명이 됨으로 많이 사용되지는 않는다.

    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);

    // 인터페이스에 구현 메서드 이므로 실행문이 없음. -> ;
    // findBy => 찾아오겠다. (select)
    // Title 제목 필드 containing
    // OrderBy 정렬 진행 Bno를 Desc 내림차순

    
    // @Query 쿼리메서드와 병합 -> JPQL
    @Query("select b from Board b where b.title like concat('%',:keyword,'%') ")
    Page<Board> findKeyword(String keyword, Pageable pageable);
    // findkeyword 메서드가 실행하면 파라미터로 keyword를 받는다.(제목 검색 단어 where)
    // 쿼리문에 객체가 넘어가야 되므로 Board가 클래스 명이 되어야 한다.
    // select * from board where title like '%keyword%' => 와일드 카드 이용
    // 단점, 조인과 같은 복잡한 쿼리를 사용하지 못함.
    //      원하는 속성들만 추출해서 객체배열 처리하거나 DTO로 처리 안됨
    //      Native 쿼리 속성값을 true로 지정해서 특정 데이터베이스에서 동작하는 sql 사용이 안됨

    // Native Query : 진짜 Query문을 사용하는 기법

    @Query(value="select now()", nativeQuery=true) // 실제 Query문으로 동작하게 만드는 형태
    String getTime();
    


}
