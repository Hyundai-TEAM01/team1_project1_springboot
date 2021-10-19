package com.mycompany.webapp.controller;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.webapp.dao.CartDAO;
import com.mycompany.webapp.dao.MemberDAO;
import com.mycompany.webapp.security.CustomUserDetails;

@Controller
@RequestMapping("/member")
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Resource
	MemberDAO memberDao;
	
	@Resource
	CartDAO cartDao;
	
	@RequestMapping("/loginForm")
	public String loginForm() {
		logger.info("실행");
		return "member/loginForm";
	}
	
	@RequestMapping("/loginError")
	public String loginError(Model model) {
		logger.info("실행");
		model.addAttribute("loginError", true);
		return "member/loginForm";
	}
	
	@RequestMapping("/joinForm")
	public String joinForm() {
		logger.info("실행");
		return "member/joinForm";
	}
	
	@RequestMapping(value = "/memberdata", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String memberData(Authentication authentication) {
		JSONObject json = new JSONObject();
		if(authentication == null) {
			json.put("cartcnt",0);
		}else {
			json.put("cartcnt", memberDao.getCartCnt(cartDao.getCartNoByMno(((CustomUserDetails)authentication.getPrincipal()).getMno())));
		}
		
		return json.toString();
	}
}  
