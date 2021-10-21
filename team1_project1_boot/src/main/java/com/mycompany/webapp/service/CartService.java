package com.mycompany.webapp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.CartDAO;
import com.mycompany.webapp.dao.MemberDAO;
import com.mycompany.webapp.dao.ProductDAO;
import com.mycompany.webapp.dto.CartDetail;
import com.mycompany.webapp.dto.CartProductInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartService {

	public enum CartUpdateResult{
		SUCCESS, FAIL, NOT_VALID, DUPLICATED
	}

	@Resource
	private CartDAO cartDao;

	@Resource
	private MemberDAO memberDao;

	@Resource
	private ProductDAO productDao;

	public Map<String, List> getCartProductList(int mno){
		int cartNo = cartDao.getCartNoByMno(mno);
		List<CartProductInfo> cartProductInfoList = cartDao.getCartProductList(cartNo);
		Map<String, List> map = new HashMap<>();

		for(CartProductInfo info : cartProductInfoList) {
			map.put(info.getPcode(), cartDao.getCartProductDetailByPcode(info.getPcode()));
		}
		map.put("cartProductInfoList", cartProductInfoList);
		return map;
	}

	public int deleteCartDetail(int cartdetailno) {
		return cartDao.removeCartDetail(cartdetailno);
	}

	public int deleteCartDetailList(String[] cdnoList) {
		return cartDao.removeCartDetailList(cdnoList);
	}

	public CartUpdateResult updateCartDetail(int mno, int cartdetailno, CartDetail cartDetail) {
		if (checkInCart(mno, cartdetailno)) {
			int result = cartDao.updateCartdetail(cartDetail);

			if(result == 1) {
				return CartUpdateResult.SUCCESS;
			}else {
				return CartUpdateResult.FAIL;
			}

		}else {
			return CartUpdateResult.NOT_VALID;
		}
	}

	public CartUpdateResult updateCartDetailOption(int mno, int cartdetailno, CartDetail cartDetail) {
		if (checkInCart(mno, cartdetailno)) {

			if(cartDao.getCartDetailByOption(cartDetail.getCartDetailNo(), cartDetail.getPcolor(), cartDetail.getPsize()) != null) {
				return CartUpdateResult.DUPLICATED;
			}else {
				int result = cartDao.updateCartdetailOption(cartDetail);

				if(result == 1) {
					return CartUpdateResult.SUCCESS;
				}else {
					return CartUpdateResult.FAIL;
				}
			}

		}else {
			return CartUpdateResult.NOT_VALID;
		}
	}


	public List<CartProductInfo> getCartDetailList(String[] pList){
		log.info("service run");
		return cartDao.getCartProductListByArray(pList, -1);
	}

	public boolean checkInCart(int mno, int cartdetailno) {
		int cartNo = cartDao.getCartNoByMno(mno);

		if(cartDao.getCartDetailByCartdetailno(cartdetailno, cartNo) != null) {
			return true;
		}

		return false;
	}

	public int getStockAmount(String pcode, String psize, String pcolor) {
		return productDao.getProductAmount(pcode, psize, pcolor);
	}

	public CartUpdateResult insertCartProduct(int mno, String pcode, String psize, String pcolor, int pamount) {
		// 카트 번호 조회
		int cartNo = cartDao.getCartNoByMno(mno);
		if (cartDao.getCartProductCount(cartNo, pcode, psize, pcolor) > 0) {
			return CartUpdateResult.DUPLICATED;
		} else {
			log.info("상품 등록 가능 상태");
			int result = cartDao.insertCartDetail(cartNo, pcode, psize, pcolor, pamount);

			if(result == 1) {
				return CartUpdateResult.SUCCESS;
			}else {
				return CartUpdateResult.FAIL;
			}
		}
		// non vaild 추가
	}

	public int getMemberMpoint(String mid) {
		return memberDao.getLoginMember(mid).getMpoint();
	}
}
