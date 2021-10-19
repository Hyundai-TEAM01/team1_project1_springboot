package com.mycompany.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDetail {
	private String pcode;
	private String pcolor;
	private String psize;
	private int pamount;
	private String colorurl;
}
