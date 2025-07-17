package org.mbc.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Board;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor // 필드값을 보고 생성자를 만든다. final 필드나 @NotNull 이 붙은 필드용
@Transactional // commit용 (여러개의 테이블이 조합될 때 해결 역할)
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper; // Entity <-> DTO
    private final BoardRepository boardRepository; // JPA용 클래스 (CRUD, PAGING, 다중검색)

    @Override
    public Long register(BoardDTO boardDTO) { // 조원이 실행코드를 만든다.
        // FORM에서 넘어온 DTO가 데이터베이스에 기록되어야 함.

        Board board = modelMapper.map(boardDTO, Board.class); // Entity가 dto로 변환

        Long bno = boardRepository.save(board).getBno();
        //                  insert into board ~~~ -> bno를 받는다.

        return bno;// front에 게시물 저장 후 번호가 전달 된다.
    }

    @Override
    public BoardDTO readOne(Long bno) {

        Optional<Board> result = boardRepository.findById(bno);
        // select * from board where bno = bno
        // Optional : NPE 대비 (NULL이 나와도 예외처리 하지 않음)

        Board board = result.orElseThrow(); // 정상값이 나오면 Entity로 받는다

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        // 모델 매퍼를 이용해서 Entity로 나온 board를 dto로 변환한다.

        return boardDTO; // front에 dto로 보낸다.
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        // select * from board where bno = bno -> Entity로 나옴

        Board board = result.orElseThrow(); // 성공 시, (Null이 아닌) 결과를 Entity로 저장
        board.change(boardDTO.getTitle(), boardDTO.getContent()); // 제목과 내용을 수정

        boardRepository.save(board); // 데이터베이스에 pk가 있으면 업데이트하고, 없으면 insert 함.


    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
        // delete from board where bno = bno


    }
}
