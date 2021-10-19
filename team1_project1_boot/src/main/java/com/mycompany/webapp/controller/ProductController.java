package com.mycompany.webapp.controller;

import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mycompany.webapp.dto.ProductAmountList;
import com.mycompany.webapp.dto.ProductDetail;
import com.mycompany.webapp.dto.ProductImg;
import com.mycompany.webapp.security.CustomUserDetails;
import com.mycompany.webapp.service.CartService;
import com.mycompany.webapp.service.CartService.CartUpdateResult;
import com.mycompany.webapp.service.ProductListService;

@Controller
@RequestMapping("/product")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Resource
	private ProductListService productListService;
	@Resource
	private CartService cartService;

	@RequestMapping("/{pcode}")
	public String getProduct(@PathVariable("pcode") final String pcode, @RequestParam("pcolor") String pcolor,
			@RequestParam("ccode") String ccode, Model model) {
		logger.info("실행");
		ProductDetail productDetail = productListService.getProductDetail(pcode);

		JSONObject jsonObject = new JSONObject();

		for (ProductImg img : productDetail.getColor()) {
			jsonObject.put(img.getPcolor(), Arrays.asList(img.getImgurl1(), img.getImgurl2(), img.getImgurl3()));
		}

		model.addAttribute("product", productDetail);
		model.addAttribute("pcolor", pcolor);
		model.addAttribute("imgurl", jsonObject.toString());

		return "product";
	}

	@GetMapping(value = "/getSizeAmount", produces = "Application/json; charset=UTF-8;")
	@ResponseBody
	public String getSizeAmount(@RequestParam("pcode") String pcode, @RequestParam("pcolor") String pcolor) {
		logger.info("실행");

		ProductAmountList productAmountList = productListService.getProductDetailAmountList(pcode, pcolor);
		String productAmountListInString = new Gson().toJson(productAmountList);
		JSONObject productObject = new JSONObject(productAmountListInString);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("productAmountList", productObject);

		return jsonObject.toString();
	}

	@PostMapping(value = "/addCart", produces = "Application/json; charset=UTF-8;")
	@ResponseBody
	public String addCart(@RequestBody HashMap<String, String> product, Authentication authentication) {
		logger.info("실행");
		JSONObject jsonObject = new JSONObject();

		if (authentication == null) {
			logger.info("로그인한 사용자 정보 없음!!!");
			jsonObject.put("msg", "needLogin");
		} else {
			CustomUserDetails memberDetails = (CustomUserDetails) authentication.getPrincipal();
			logger.info("로그인한 사용자 정보 : " + memberDetails.getMno());
			int mno = memberDetails.getMno();
			String pcode = product.get("pcode");
			String psize = product.get("psize");
			String pcolor = product.get("pcolor");
			int pamount = Integer.parseInt(product.get("pamount"));

			CartUpdateResult result = cartService.insertCartProduct(mno, pcode, psize, pcolor, pamount);
			if (result.equals(CartUpdateResult.SUCCESS)) {
				jsonObject.put("msg", "addCart");
			} else if (result.equals(CartUpdateResult.DUPLICATED)) {
				jsonObject.put("msg", "duplicated");
			} else {
				jsonObject.put("msg", "fail");
			}
		}
		return jsonObject.toString();
	}
}
