package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.webapp.dto.CartDetail;
import com.mycompany.webapp.dto.CartProductInfo;
import com.mycompany.webapp.security.CustomUserDetails;
import com.mycompany.webapp.service.CartService;
import com.mycompany.webapp.service.CartService.CartUpdateResult;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController {

	@Resource
	private CartService cartService;


	@RequestMapping("/content")
	public String cart() {
		return "cart";
	}

	@RequestMapping("/order")
	public String order(@RequestParam String pList, Model model, Authentication authentication) {
		CustomUserDetails minfo = (CustomUserDetails)authentication.getPrincipal();

		String[] productList = pList.split(",");
		model.addAttribute("pList", productList);
		model.addAttribute("mphone", minfo.getMphone());
		model.addAttribute("memail", minfo.getMemail());
		model.addAttribute("mname", minfo.getMname());
		int mpoint = cartService.getMemberMpoint(minfo.getUsername());
		model.addAttribute("mpoint", mpoint);
		return "order";
	}

	@GetMapping(value = "/getCartProductInfoList", produces = "Application/json; charset=UTF-8;")
	@ResponseBody
	public String getCartProductInfoList(Authentication authentication) {

		CustomUserDetails memberDetails = (CustomUserDetails) authentication.getPrincipal();

		JSONObject json = new JSONObject();
		Map<String,List> map = cartService.getCartProductList(memberDetails.getMno());
		List<CartProductInfo> li = map.get("cartProductInfoList");
		json.append("infoList", li);

		JSONArray jsonArr = new JSONArray();
		JSONArray jsonArr2 = new JSONArray();

		for (CartProductInfo cinfo : li) {
			JSONObject temp = new JSONObject();
			JSONObject temp2 = new JSONObject();

			// getStockAmount(cinfo.getPcode(),cinfo.getPcolor(),cinfo.getPsize()) 생성하기
			temp.append(String.valueOf(cinfo.getCartdetailno()),
					cartService.getStockAmount(cinfo.getPcode(), cinfo.getPsize(), cinfo.getPcolor()));

			temp2.put(cinfo.getPcode(), map.get(cinfo.getPcode()));

			jsonArr.put(temp);
			jsonArr2.put(temp2);
		}

		json.append("stockList", jsonArr);
		json.append("detailList", jsonArr2);
		return json.toString();
	}

	@GetMapping(value = "/getOrderList", produces = "Application/json; charset=UTF-8;")
	@ResponseBody
	public String getOrderList(String productList) {
		log.info("run");
		JSONObject json = new JSONObject();

		String[] pList = productList.split(",");
		List<CartProductInfo> li = cartService.getCartDetailList(pList);
		json.append("infoList", li);

		return json.toString();
	}

	@PostMapping(value = "/cartDetailDelete", produces = "Application/json; charset=UTF-8;")
	@ResponseBody
	public String cartDetailDelete(@RequestBody HashMap<String, Integer> map) {
		JSONObject json = new JSONObject();
		json.put("result", cartService.deleteCartDetail(map.get("cdno")));

		return json.toString();
	}

	@PostMapping(value = "/cartDetailListDelete", produces = "Application/json; charset=UTF-8;")
	@ResponseBody
	public String cartDetailList(@RequestBody HashMap<String, String[]> pList) {
		JSONObject json = new JSONObject();
		json.put("result", cartService.deleteCartDetailList(pList.get("pList")));

		return json.toString();
	}


	@PatchMapping(value = "/cartDetailUpdate", produces = "Application/json; charset=UTF-8")
	@ResponseBody
	public String cartDetailUpdate(@RequestBody HashMap<String, Integer> info, Authentication authentication) {
		JSONObject json = new JSONObject();

		CartDetail cartDetail = new CartDetail();
		cartDetail.setAmount(info.get("amount"));
		cartDetail.setCartDetailNo(info.get("cartDetailNo"));

		int mno = ((CustomUserDetails) authentication.getPrincipal()).getMno();
		CartUpdateResult result = cartService.updateCartDetail(mno, info.get("cartDetailNo"), cartDetail);

		if(result.equals(CartUpdateResult.SUCCESS)) {
			json.put("result", "성공");
		}else if(result.equals(CartUpdateResult.NOT_VALID)) {
			json.put("result", "잘못된 상품 수정 오류");
		}
		else {
			json.put("result", "알 수 없는 오류로 실패");
		}


		return json.toString();
	}

	@PatchMapping(value = "/cartDetailOptionUpdate", produces = "Application/json; charset=UTF-8")
	@ResponseBody
	public String cartDetailOptionUpdate(@RequestBody HashMap<String, String> info, Authentication authentication) {
		JSONObject json = new JSONObject();

		CartDetail cartDetail = new CartDetail();
		cartDetail.setAmount(1);
		cartDetail.setCartDetailNo(Integer.parseInt(info.get("cartDetailNo")));
		cartDetail.setPcolor(info.get("pcolor"));
		cartDetail.setPsize(info.get("psize"));

		log.info(cartDetail.toString());

		int mno = ((CustomUserDetails) authentication.getPrincipal()).getMno();
		CartUpdateResult result = cartService.updateCartDetailOption(mno, cartDetail.getCartDetailNo(), cartDetail);

		if(result.equals(CartUpdateResult.SUCCESS)) {
			json.put("result", "성공");
		}else if(result.equals(CartUpdateResult.NOT_VALID)) {
			json.put("result", "잘못된 상품 수정 오류");
		}else if(result.equals(CartUpdateResult.DUPLICATED)) {
			json.put("result", "duplicated");
		}
		else {
			json.put("result", "알 수 없는 오류로 실패");
		}


		return json.toString();
	}

}
