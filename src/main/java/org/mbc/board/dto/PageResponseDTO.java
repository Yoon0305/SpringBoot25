package org.mbc.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> { // <E> Entity용 변수명 (변할 수 있는 값)
    // Paging 처리 응답용 객체
    // dto의 목록, 시작페이지/끝페이지 여부

    private int page, size, total ; // 현재페이지, 페이지 당 게시물 수, 총 게시물 수

    private int start; // 시작 페이지 번호
    private int end; // 끝 페이지 번호

    private boolean prev; // 이전 페이지 존재 여부
    private boolean next; // 다음 페이지 존재 여부

    private List<E> dtoList; // 게시물의 목록


    //Constructor
    @Builder(builderMethodName = "withAll") // PageResponseDTO.<BoardDTO>withAll()
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){
        // PageRequestDTO return link; // page=1&size=10&type=??&keyword=??
        // List<Board> dtoList / List<Member> dtoList / List<Item> dtoList
        // int total -> 총 게시물 수

        if(total <= 0) {
            // 게시물 없을 때
            return;
        }

        this.page = pageRequestDTO.getPage(); // 요청에 대한 페이지 번호
        this.size = pageRequestDTO.getSize(); // 요청에 대한 사이즈 (게시물 수)
        this.total = total; // parameter로 넘어온 값
        this.dtoList = dtoList; // parameter로 넘어온 값

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; // 화면에서 마지막 번호가 나옴
        //          3 <- 3.0<-2.1 =    21    / 10.0

        this.start = this.end - 9 ;
        //  11     =    20    - 9

        int last = (int)(Math.ceil(total/(double)size)); // 데이터 개수를 계산한 마지막 페이지 번호
        //          만약 88개의 게시물이면, 9개의 페이지 번호가 나와야 함

        this.end = (end > last) ? last : end; // 3항 연산자 -> 최종 활용되는 페이지 번호
        //            조건          참    거짓

        this.prev = this.start > 1 ;
        this.next = total > this.end * this.size; // 다음 페이지 유무

    }


}
