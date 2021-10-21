package com.mycompany.webapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.MemberDAO;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.security.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberDetailsService implements UserDetailsService {
	// Spring Security User 상속받아 로그인한 유저정보에 추가로 원하는 데이터를 넣음

	@Resource
	MemberDAO memberDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = memberDAO.getLoginMember(username);
		if (member == null) {
			throw new UsernameNotFoundException(username);
		}
		log.info(member.toString());
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(member.getMrole()));

		CustomUserDetails memberDetails = new CustomUserDetails(member.getMid(), member.getMpassword(), member.isMenable(),
				authorities, member.getMno(), member.getMphone(),member.getMemail(), member.getMpoint(), member.getMname());

		return memberDetails;
	}

}
