package com.mycompany.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImg {
	private String pcode;
	private String pcolor;
	private String imgurl1;
	private String imgurl2;
	private String imgurl3;
	private String colorurl;
}
