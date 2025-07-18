package org.mbc.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Board;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.dto.PageRequestDTO;
import org.mbc.board.dto.PageResponseDTO;
import org.mbc.board.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @Override // 리스트 페이지 요청에 온 값을 응답을 보낸다 (페이징 처리)
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        // pageRequestDTO에서 넘어온 값을 처리하고 PageResponseDTO로 보내야 한다.

        String[] types = pageRequestDTO.getTypes(); // front에 넘어온 type t,c,w 처리
        String keyword = pageRequestDTO.getKeyword(); // front에서 넘어온 keyword 검색 처리
        Pageable pageable = pageRequestDTO.getPageable("bno"); // 프론트에서 넘어온 bno를 이용한 정렬 처리용
        // return PageRequest.of(this.page-1 , this.size, Sort.by(props).descending());

        // Page<Board> -> List<BoardDTO> 변환하고 리턴 되어야 한다.
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        // test 에서 시행했던 코드

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class))
                .collect(Collectors.toList());
        // Entity를 DTO로 변환시키는 코드 (ModelMapper이용)

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build(); // Builder 패턴 리턴
    }
}
