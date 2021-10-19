package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetail {
	private int cartDetailNo;
	private int cartNo;
	private String pcode;
	private String psize;
	private String pcolor;
	private int amount;
	private Date regdate;
	private char isDeleted;
	
}
