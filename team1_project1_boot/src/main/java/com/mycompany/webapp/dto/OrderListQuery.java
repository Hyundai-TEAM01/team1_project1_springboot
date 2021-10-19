package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class OrderListQuery {
	// 주문목록페이지를 요청하는 URL 쿼리 객체입니다.
	private String sterm; // 조회 시작날짜
	private String eterm; // 조회 종료날짜
	private String searchType; // 검색구분 : 상품명, 주문번호
	private String searchWord; // 검색명
	private int pageNo; // 페이지 번호

	public OrderListQuery(String sterm, String eterm, String searchType, String serachWord, String pageNo) {
		this.sterm = sterm == null ? "" : sterm;
		this.eterm = eterm == null ? "" : eterm;
		this.searchType = searchType == null ? "pname" : searchType; // 공백이면 pname 기본값(상품명), 다른 값 porderno(주문번호)
		this.searchWord = searchWord == null ? "" : searchWord;
		this.pageNo = pageNo == null ? 1 : Integer.parseInt(pageNo); // 공백이면 1, 아니면 정수변환
	}
}
