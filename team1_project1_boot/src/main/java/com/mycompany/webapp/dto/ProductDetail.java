package com.mycompany.webapp.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {
	private String pcode;
	private String pname;
	private String pcontent;
	private String pbrand;
	private int pprice;
	private Date pdate;
	private char enabled;
	private List<ProductImg> color;
}
