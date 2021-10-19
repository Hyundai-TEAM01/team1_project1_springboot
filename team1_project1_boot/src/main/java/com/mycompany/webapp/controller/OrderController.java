package com.mycompany.webapp.controller;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.webapp.dto.ProductOrder;
import com.mycompany.webapp.security.CustomUserDetails;
import com.mycompany.webapp.service.OrderService;
import com.mycompany.webapp.service.OrderService.OrderResult;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Resource
	OrderService orderService;

	@PostMapping(value = "/newOrder", produces = "Application/json; charset=UTF-8;")
	public String newOrder(ProductOrder order, String plist, Model model, Authentication authentication) {
		CustomUserDetails minfo = (CustomUserDetails) authentication.getPrincipal();

		order.setMno(minfo.getMno());
		OrderResult result = orderService.newOrder(order, plist, minfo.getUsername());

		if (result.equals(OrderResult.SUCCESS)) {
			return "redirect:/mypage/orderdetail?code=" + order.getPorderno();
		} else if (result.equals(OrderResult.ENOUGH_MPOINT)) {
			model.addAttribute("errorTitle", "사용 가능한 마일리지가 부족합니다.");
			model.addAttribute("errorContent", "정상적인 방식으로 마일리지를 적용해주세요.");
			return "error/custom";
		} else if (result.equals(OrderResult.NOT_VALID)) {
			model.addAttribute("errorTitle", "잘못된 상품 결제입니다.");
			model.addAttribute("errorContent", "정상적인 방식으로 결제를 진행해주세요.");
			return "error/custom";
		} else if (result.equals(OrderResult.SOLDOUT)) {
			model.addAttribute("errorTitle", "품절된 상품이 포함되어 있습니다.");
			model.addAttribute("errorContent", "품절된 상품이 포함되어 결제가 이루어지지 않았습니다." + "<br/>해당 상품을 제외하고 다시 주문해주시기 바랍니다.");
			return "error/custom";
		} else {
			model.addAttribute("errorTitle", "알 수 없는 서버 오류입니다.");
			model.addAttribute("errorContent", "잠시후 다시 실행해주세요.");
			return "error/custom";
		}
	}
}
