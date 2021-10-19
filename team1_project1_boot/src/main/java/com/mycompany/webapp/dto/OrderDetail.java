package com.mycompany.webapp.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
	private int porderno;
	private int mno;
	private String pordermphone;
	private String pordername;
	private String porderphone;
	private String porderaddr1;
	private String porderaddr2;
	private String porderaddr3;
	private String pordertel;
	private String porderrequest;
	private String porderemail;
	private int pordertotalorgprice;
	private int porderdiscount;
	private int porderpayprice;
	private int pordertotalpoint;
	private String porderstatus;
	private Date porderdate;
	private Date porderdonedate;
	private char porderpayment;
	private String porderpayname;
	private String porderpayno;
	private String porderpayinstallment;
	private List<OrderItem> orderItems;

	public String getPorderdate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return dateFormat.format(porderdate);
	}
}
