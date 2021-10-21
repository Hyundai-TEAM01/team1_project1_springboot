package com.mycompany.webapp.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

//객체로 생성해서 관리하도록 설정
@Component
//모든 컨트롤러에 영향을 미치는 설정
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler
	public String handleOtherException(Exception e) {
		log.info("실행");
		e.printStackTrace();
		return "error/500";
	}
}
