package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductInfo {
	private int cartdetailno;
	private int cartno;
	private String pcode;
	private String psize;
	private String pcolor;
	private int amount;
	private Date regdate;
	private char isdeleted;
	private String imgurl;
	private String pbrand;
	private String pname;
	private int pprice;
}
