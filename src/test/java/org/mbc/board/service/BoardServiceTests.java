package org.mbc.board.service;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.dto.PageRequestDTO;
import org.mbc.board.dto.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){

        log.info("등록용 테스트 서비스 실행중....");
        log.info(boardService.getClass().getName());
        //org.mbc.board.service.BoardServiceImpl~

        BoardDTO boardDTO = BoardDTO.builder()
                .title("서비스에서 만든 제목")
                .content("서비스에서 만든 내용")
                .writer(" 서비스 님")
                .build(); //setter 대신 @Builder

        Long bno = boardService.register(boardDTO);

        log.info("테스트 결과 bno: "+bno);
        //Hibernate:
        //    insert
        //    into
        //        board
        //        (content, moddate, regdate, title, writer)
        //    values
        //        (?, ?, ?, ?, ?)

    }

    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("서비스에서수정된제목2")
                .content("서비스에서수정된내용2")
                .build();
        boardService.modify(boardDTO); // front에서 객체가 넘어가 수정이 되었는지 테스트

    }

    @Test
    public void testList() {
        // front에서 넘어오는 데이터를 이용해서 paging과 검색과 정렬 처리 용
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw") // 제목 내용 작성자 중에
                .keyword("1") // 1이 들어간걸 찾는다
                .page(1) // 현재 페이지는 1
                .size(10) // 10개씩 보여달라
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

}
