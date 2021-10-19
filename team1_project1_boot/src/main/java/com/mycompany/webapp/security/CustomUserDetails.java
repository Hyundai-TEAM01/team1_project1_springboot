package com.mycompany.webapp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User{
	private String useremail;

	public CustomUserDetails(String username, String password,
								Collection<? extends GrantedAuthority> authorities, String useremail) {
		super(username, password, authorities);
		this.useremail = useremail;
		
	}
	
	public String getUseremail() {
		return useremail;
	}


}
