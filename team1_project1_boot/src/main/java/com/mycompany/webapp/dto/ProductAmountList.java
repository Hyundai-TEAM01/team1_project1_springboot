package com.mycompany.webapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductAmountList {
	private String pcode;
	private String pcolor;
	private List<ProductAmount> amount;
}
