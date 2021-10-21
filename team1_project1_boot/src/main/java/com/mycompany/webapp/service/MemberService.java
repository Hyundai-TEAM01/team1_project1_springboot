package com.mycompany.webapp.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.MemberDAO;
import com.mycompany.webapp.dto.Member;

@Service
public class MemberService {

	@Resource
	private MemberDAO memberDAO;

	public Member getLoginMember(String mid) {

		return memberDAO.getLoginMember(mid);
	}
}
