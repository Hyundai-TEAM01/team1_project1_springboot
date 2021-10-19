package com.mycompany.webapp.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User{
	private static final long serialVersionUID = 1L;
	private int mno;
	private String mphone;
	private String memail;
	private int mpoint;
	private String mname;

	public CustomUserDetails(String mid, String mpassword, boolean menabled, List<GrantedAuthority> mAuthorities, int mno,
			String mphone, String memail, int mpoint, String mname) {
		super(mid, mpassword, menabled, true, true, true, mAuthorities);
		this.mno = mno;
		this.mphone = mphone;
		this.memail = memail;
		this.mpoint = mpoint;
		this.mname = mname;
	}
	
	public int getMno() {
		return mno;
	}
	
	public int getMpoint() {
		return mpoint;
	}

	public String getMemail() {
		return memail;
	}

	public String getMphone() {
		return mphone;
	}
	public String getMname() {
		return mname;
	}

}
