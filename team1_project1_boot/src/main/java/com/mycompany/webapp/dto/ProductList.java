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
public class ProductList {
	private int pno;
	private String ccode;
	private String pcode;
	private String pname;
	private String pbrand;
	private int pprice;
	private Date pdate;
	private char enabled;
	private List<ProductImg> color;
	
	public String getPdate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		return dateFormat.format(pdate);
	}
}