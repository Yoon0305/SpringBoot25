package org.mbc.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.dto.PageRequestDTO;
import org.mbc.board.dto.PageResponseDTO;
import org.mbc.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor // final을 붙은 필드로 생성자 만든다.
public class BoardController {

    private  final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        // paging 처리와 정렬과 검색이 추가된 list가 나옴.

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        // paging 처리가 되는 요청을 처리하고, 결과를 response로 받는다.

        log.info(responseDTO);

        model.addAttribute("responseDTO",responseDTO); // 결과를 Spring이 관리하는 model 객체로 전달.

    }

}
