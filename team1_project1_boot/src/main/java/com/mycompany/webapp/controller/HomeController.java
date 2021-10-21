package com.mycompany.webapp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.ProductList;
import com.mycompany.webapp.security.CustomUserDetails;
import com.mycompany.webapp.service.ProductListService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@Resource
	private ProductListService productListService;

	@RequestMapping(value = { "/", "/productList" })
	public String content(Authentication authentication) {
		log.info("실행");

		if (authentication == null) {
			log.info("로그인한 사용자 정보 없음!!!");
		} else {
			CustomUserDetails memberDetails = (CustomUserDetails) authentication.getPrincipal();
			int mno = memberDetails.getMno();
			log.info("로그인한 사용자 정보 : " + mno);
		}
		return "home";
	}

	@Resource
	private RedisTemplate<String, String> template;

	@GetMapping(value = "/getProductList", produces = "Application/json; charset=UTF-8;")
	@ResponseBody
	public String getProductList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "ccode", defaultValue = "WOMEN_Top_Shirts") String ccode) {
		log.info("실행");
		String radisKey = ccode + "_" + pageNo;
		// Redis에 캐시된 값이 없다면
		if (template.boundValueOps(radisKey).get() == null) {
			log.info("캐시가 없습니다.");
			JSONObject jsonObject = new JSONObject();
			int totalRows = productListService.getTotalProducListtNum(ccode);
			Pager pager = new Pager(12, 10, totalRows, pageNo);

			String pagerInString = new Gson().toJson(pager);
			JSONObject pagerObject = new JSONObject(pagerInString);
			pagerObject.put("ccode", ccode);

			List<ProductList> productList = productListService.getProductList(ccode, pager);

			jsonObject.put("pager", pagerObject);
			jsonObject.put("productList", productList);

			// Redis에 값을 캐시한다.
			template.boundValueOps(radisKey).set(jsonObject.toString());
			return jsonObject.toString();
		}else {
			log.info("Cache Hit!!");
		}

		// Redis에 캐시된 값을 반환함.
		return template.boundValueOps(radisKey).get();
	}

	@RequestMapping("/error/403")
	public String error403() {
		log.info("실행");
		return "error/403";
	}
}