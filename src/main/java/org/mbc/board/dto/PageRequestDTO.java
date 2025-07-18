package org.mbc.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder // Setter 없이 Builder 패턴을 사용, @AllArgs~, @NoArgs~ 필수
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class PageRequestDTO {
    //Paging 처리에 요청용 (front에서 요청이 오면 동작)
    //Paging에 관련된 정보 (Page,size ...), 검색종류 (types -> generic으로 제목 t, 내용c, 작성자w), 키워드(검색 단어)

    @Builder.Default // Builder 시작 시 초기값이 들어감
    private int page = 1;

    @Builder.Default // Builder 시작 시 초기 게시물 개수 들어감
    private int size = 10;

    private String type; // t, c, w (다중 검색용)

    private String keyword; //form 박스에 검색 내용

    private String link; // front에 Paging 번호 클릭 시 처리되는 문자열
    // ex) list?page=3&type=w&keyword=kww

    public String getLink() {

        if (link == null) {

            StringBuilder builder = new StringBuilder();
            // String타입에 + 연산자로 link를 작성하면 객체가 많이 생겨서 메모리 이용이 늘어남
            // StringBuilder는 이를 해결 하기 위한 방식.
            builder.append("page=" + this.page); // page=1
            builder.append("&size=" + this.size); // page=1&size=10

            if (type != null && type.length() > 0) {
                // 타입이 있을 때
                builder.append("&type=" + type); // page=1&size=10&type=??

            } //if문 종료 (type있을 때)

            if (keyword != null) { // 검색어가 있을 때
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.info(e.getStackTrace());
                    log.info("UTF-8 처리중 오류 발생");
                }
            }
        }
        link = builder().toString();

        return link;
    }


    // 추가 method
    public String[] getTypes(){
        // front에서 문자열이 여러개가 넘어오면 배열로 변환
        if(type==null || type.isEmpty()){
            //넘어온 값이 Null 또는 비었을 경우,
            return null;
        }
        return type.split(" "); // 차후에 front에 form박스 확인하고 조절.
        // String으로 넘어온 값을 split하여 각 타입-단어를 배열에 꽂아넣음

    }

    //테스트용 코드를 dto로 만들어 메서드 처리함
    public Pageable getPageable(String...props){ // String ... props 배열이 몇개 들어올지 모를 때
        return PageRequest.of(this.page-1, this.size, Sort.by(props).descending());
        //                      페이지 번호             게시물 수            정렬 기법 (내림차순)
    }


}
