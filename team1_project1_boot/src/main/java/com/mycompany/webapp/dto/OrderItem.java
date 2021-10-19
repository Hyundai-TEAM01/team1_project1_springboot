package com.mycompany.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	private int podno; // 상품디테일 pk
	private int porderno; // 상품주문 fk
	private String pcode;
	private String pcolor;
	private String psize;
	private int podprice;
	private int podamount;
	private String podstatus;
	private String imgurl1;
	private String pbrand;
	private String pname;
	private String pprice;
}
