package com.mycompany.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.java.Log;

@Controller
@Log
public class HomeController {
	
	@RequestMapping("/")
	public String home(Model model) {
		log.info("실행");
		log.warning("실행");
		model.addAttribute("str", "Model : Hello World");
		return "home"; // template 폴더에서 찾음, 무슨 엔진을 쓰냐에 따라 확장자명이 변경
	}
}
