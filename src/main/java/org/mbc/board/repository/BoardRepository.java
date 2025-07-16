package org.mbc.board.repository;

import org.mbc.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // extends JpaRepository<entity class, pk type>
    // JpaRepository는 jpa에서 미리 만들어놓은 interface로, crud와 paging 처리, 정렬등이 존재한다.
}
