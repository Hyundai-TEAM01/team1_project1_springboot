package com.mycompany.webapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.webapp.dao.CartDAO;
import com.mycompany.webapp.dao.MemberDAO;
import com.mycompany.webapp.dao.OrderDAO;
import com.mycompany.webapp.dao.ProductDAO;
import com.mycompany.webapp.dto.CartProductInfo;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.OrderDetail;
import com.mycompany.webapp.dto.OrderList;
import com.mycompany.webapp.dto.OrderListQuery;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.ProductOrder;
import com.mycompany.webapp.dto.ProductOrderDetail;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	public enum OrderResult {
		SUCCESS, ENOUGH_MPOINT, NOT_VALID, FAIL, SOLDOUT
	}

	@Resource
	private OrderDAO orderDao;

	@Resource
	private MemberDAO memberDao;

	@Resource
	private CartDAO cartDao;

	@Resource
	private ProductDAO productDao;

	// 주문 목록 갯수 가져오기(유저번호 + 페이징 + 검색쿼리[상품명, 주문번호])
	public List<OrderList> getOrderListByPage(int mno, Pager pager, OrderListQuery query) {
		// int sampleMno = 1;
		return orderDao.getOrderListByPage(mno, pager, query);
	}

	// 주문 목록 갯수 가져오기(유저번호 + 페이징 + 검색쿼리[상품명, 주문번호])
	public int getOrderListCount(int mno, OrderListQuery query) {
		return orderDao.getOrderListCount(mno, query);
	}

	// 유저 넘버와 그에 맞는 주문번호로 상품주문상세정보 가져오기
	public List<OrderDetail> getOrderDetail(int mno, int porderno) {
		return orderDao.getOrderDetail(mno, porderno);
	}

	@Transactional
	public OrderResult newOrder(ProductOrder productOrder, String plist, String mid) {
		int mno = productOrder.getMno();
		int cartno = cartDao.getCartNoByMno(mno);
		Member member = memberDao.getLoginMember(mid);

		// 마일리지가 부족한 경우
		if (member.getMpoint() < productOrder.getPorderdiscount()) {
			return OrderResult.ENOUGH_MPOINT;
		}

		// 해당 member의 cart에 담겨 있는 제품인지 확인
		String[] productlist = plist.split(",");
		List<CartProductInfo> pInfoList = cartDao.getCartProductListByArray(productlist, cartno);

		if (pInfoList.size() != productlist.length) { // cart에 없는 물건을 구매하고자 하는 경우
			return OrderResult.NOT_VALID;
		}

		if(productDao.getProductAmountList(productlist) != null) {
			return OrderResult.SOLDOUT;
		}


		int sum = 0;
		for (CartProductInfo p : pInfoList) {
			sum += p.getAmount() * p.getPprice();
		}

		sum = sum < 30000 ? sum + 2500 : sum; // 배송비 추가

		productOrder.setPordertotalorgprice(sum); // 구매 상품들의 가격 합 추가

		productOrder.setPorderpayprice(sum - productOrder.getPorderdiscount());

		productOrder.setPordertotalpoint((int)Math.ceil(productOrder.getPorderpayprice()*0.05));

		orderDao.createOrder(productOrder); // productOrder db에 추가

		int result = productOrder.getPorderno(); // 새롭게 생성된 sequence 값
		if (result > 0) {

			// 사용자 마일리지 차감
			if(productOrder.getPorderdiscount() != 0)
				memberDao.updateMemberMpoint(mno,member.getMpoint() - productOrder.getPorderdiscount());

			// cart에서 주문한 row isdelete 변경
			cartDao.purchaseCartDetailList(productlist);

			// order detail 생성
			List<ProductOrderDetail> porderDetailList = new ArrayList<>();
			for(CartProductInfo info : pInfoList) {
				ProductOrderDetail pod = new ProductOrderDetail();
				pod.setPcode(info.getPcode());
				pod.setPcolor(info.getPcolor());
				pod.setPodamount(info.getAmount());
				pod.setPodprice(info.getPprice() * info.getAmount());
				pod.setPorderno(result);
				pod.setPsize(info.getPsize());
				pod.setPodstatus("결제완료");
				porderDetailList.add(pod);
			}

			log.info("카트 디테일 생성 전");
			orderDao.createOrderDetailByList(porderDetailList);

			// 상품 재고 감소 시키기!

			for(ProductOrderDetail pod : porderDetailList) {
				int amount = productDao.getProductAmount(pod.getPcode(), pod.getPsize(), pod.getPcolor());
				amount -= pod.getPodamount();
				productDao.updateProductStock(pod.getPcode(), pod.getPsize(), pod.getPcolor(), amount);
			}

			for(ProductOrderDetail pod : porderDetailList) {
				productDao.productEnabledUpdate(pod.getPcode());
			}

			return OrderResult.SUCCESS;
		}
		return OrderResult.FAIL;
	}

}
