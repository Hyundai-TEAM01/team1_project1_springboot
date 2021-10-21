package com.mycompany.webapp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/*
SimpleUrlAuthenticationSuccessHandler
	로그인 성공후에 무조건 defaultTartUrl으로 이동
SavedRequestAwareAuthenticationSuccessHandler
	로그인 성공후에 사용자가 접근하고자 했던 페이지로 이동
 */

//public class Ch17AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
