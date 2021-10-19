package com.mycompany.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderDetail {
	private int podno; 
	private int porderno; 
	private String pcode;
	private String pcolor;
	private String psize;
	private int podprice;
	private int podamount;
	private String podstatus;
}
