package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductOrder {
	private int porderno;
	private int mno;
	private String pordermphone; // 주문자 폰 번호
	private String pordername;  // 수령인 이름
	private String porderphone; // 수령인 폰 번호
	private String porderaddr1; // 우편번호
	private String porderaddr2; // 기본 주소
	private String porderaddr3; // 상세 주소
	private String pordertel;  // 수령인 유선 전화번호
	private String porderrequest; // 배송 요청사항
	private String porderemail; // 수령인 이메일
	private int pordertotalorgprice; // 구매 제품 원가 총 합
	private int porderdiscount; // 할인 받은 금액
	private int porderpayprice; // 최종 결제 금액
	private int pordertotalpoint; // 해당 주문으로 적립 예정인 포인트
	private Date porderdate;  // 주문일
	private Date porderdonedate;  // 주문 완료 또는 취소 일
	private char porderpayment; // 0 카드, 1 무통장입금
	private String porderpayname; // 카드사 또는 은행사
	private String porderpayno; // 카드 번호 또는 입금 계좌 
	private String porderpayinstallment; // 할부 개월 수  ex) 입력값 : 일시불, 3개월
}
