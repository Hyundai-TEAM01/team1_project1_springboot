package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class CouponListQuery {
	// 쿠폰목록페이지를 요청하는 URL 쿼리 객체입니다.
	private String searchType; // 검색구분 : 미사용, 사용완료, 기간만료
	private int pageNo; // 페이지 번호

	public CouponListQuery(String searchType, String pageNo) {
		this.searchType = searchType == null ? "0" : searchType; // 공백이면 기본값(미사용:0), 1: 사용완료, 2: 기간만료
		this.pageNo = pageNo == null ? 1 : Integer.parseInt(pageNo); // 공백이면 1, 아니면 정수변환
	}
}
