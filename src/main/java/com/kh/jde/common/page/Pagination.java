package com.kh.jde.common.page;

import org.springframework.stereotype.Component;

@Component
public class Pagination {
    
    public static PageInfo getPageInfo(int listCount, int currentPage, int pageLimit, int boardLimit) {
        
        // 최대 페이지 수 계산
        int maxPage = (int) Math.ceil((double) listCount / boardLimit);
        
        // 페이징바 시작 페이지 계산
        int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
        
        // 페이징바 끝 페이지 계산
        int endPage = startPage + pageLimit - 1;
        
        // 끝 페이지가 최대 페이지보다 크면 조정
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        
        // offset 계산 (ROWNUM 방식)
        int offset = (currentPage - 1) * boardLimit;
        
        return new PageInfo(listCount, currentPage, pageLimit, boardLimit, 
                           startPage, endPage, maxPage, offset);
    }
}
