package com.mycompany.webapp.security;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import lombok.extern.slf4j.Slf4j;

// Spring Security 설정 클래스
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Resource
	private DataSource dataSource;
	
	@Resource
	private CustomUserDetailsService customUserDetailsService;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("configure(HttpSecurity http) 실행");
		
		// 로그인 방식 설정
		http.formLogin()
			.loginPage("/security/loginForm") // 로그인 페이지 지정, default '/login' (get)
			.usernameParameter("mid") // default 'username'
			.passwordParameter("mpassword") // default 'password'
			.defaultSuccessUrl("/security/content") // 로그인 시 해당 경로로  redirect
			.failureUrl("/security/loginError") // 로그인 실패 시 해당 경로로 이동, default '/login?error'
			.loginProcessingUrl("/login"); // default '/login' (post)
		
		// 로그아웃 설정
		http.logout()
			.logoutUrl("/logout") // default '/logout'
			.logoutSuccessUrl("/security/content"); // 로그아웃 성공 시 이동 페이지
		
		// URL 권한 설정
		http.authorizeRequests()
			.antMatchers("/security/admin/**").hasAuthority("ROLE_ADMIN")
			.antMatchers("/security/manager/**").hasAuthority("ROLE_MANAGER")
			.antMatchers("/security/user/**").authenticated() // 로그인만 되어있을 경우 접근 가능
			.antMatchers("/**").permitAll();
		
		// 해당 페이지에 대해 권한 없음(403)일 경우 처리
		http.exceptionHandling().accessDeniedPage("/security/accessDenied");
		
		// CSRF 비활성화
		http.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("configure(AuthenticationManagerBuilder auth) 실행");
		// 사용자 정보를 얻어올 경로 설정
		/*
		 * auth.jdbcAuthentication() .dataSource(dataSource)
		 * .usersByUsernameQuery("SELECT mid, mpassword, menabled FROM member WHERE mid=?"
		 * ) .authoritiesByUsernameQuery("SELECT mid, mrole FROM member WHERE mid=?")
		 * .passwordEncoder(passwordEncoder()); //default 'DelegatingPasswordEncoder'
		 */
		// 
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		auth.authenticationProvider(provider);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		log.info("configure(WebSecurity web) 실행");
		
		//권한 계층 설정
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchyImpl()); // RoleHierarchy 객체는 spring security 전체에서 사용하므로 관리 객체로 생성!
		web.expressionHandler(handler);
		web.ignoring() // 인증 절차 필요없는 url 설정
			.antMatchers("/images/**")
			.antMatchers("/css/**")
			.antMatchers("/jquery/**")
			.antMatchers("/bootstrap-4.6.0-dist/**")
			.antMatchers("/favicon.ico");
	}

	// method 실행 후 반환되는 객체를 spring boot 관리 객체에 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder;
	}
	/*
	 다른 class에서는 주입을 통해 사용 가능
	 @Resource
	 private PasswordEncoder passwordEncoder; 
	 */
	
	@Bean
	public RoleHierarchyImpl roleHierarchyImpl() {
		RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
		roleHierarchyImpl.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
		return roleHierarchyImpl;
	}
}
