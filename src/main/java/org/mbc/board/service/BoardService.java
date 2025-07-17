package org.mbc.board.service;

import org.mbc.board.dto.BoardDTO;

public interface BoardService {
    // 조장용 코드 -> Signature만 필요 -> Impl 구현 클래스에서 실행문 만들기.

    Long register(BoardDTO boardDTO); // front에서 form에 있는 내용이 dto로 들어온다.
    //return은 bno가 된다.

    BoardDTO readOne(Long bno); // front에서 bno가 넘어오면 객체가 return된다.

    void modify(BoardDTO boardDTO); //front에서 dto가 넘어오면 수정 작업

    void remove(Long bno); // front에서 bno가 넘어오면 삭제 작업 진행

    
    
}
