package com.mycompany.webapp.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponList {
	private int couno;
	private String couname;
	private String coucontent;
	private char cousaletype;
	private int coubenefit;
	private int couperiod; // 기간만료 ex) 100일
	private String couimgurl;
	private int coupricelimit;
	// membercoupon table
	private Date regdate; // 쿠폰 발급일
	private Date usedate; // 쿠폰 사용일
	private char used; // 사용여부

	public String getRegdate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return dateFormat.format(regdate);
	}

	public String getUsedate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return dateFormat.format(usedate);
	}
}