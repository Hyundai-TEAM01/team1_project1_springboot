package com.mycompany.webapp.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private int mno;
	private String mid;
	private String mpassword;
	private String memail;
	private String mname;
	private Date mbirth;
	private String mphone;
	private Date mregdate;
	private boolean menable;
	private int mpoint;
	private String mrole;

}
