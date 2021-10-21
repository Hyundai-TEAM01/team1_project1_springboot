package com.mycompany.webapp.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.CouponDAO;
import com.mycompany.webapp.dto.CouponList;
import com.mycompany.webapp.dto.CouponListQuery;
import com.mycompany.webapp.dto.Pager;

@Service
public class CouponService {

	@Resource
	private CouponDAO couponDAO;

	// 쿠폰 목록 갯수 가져오기(유저번호 + 페이징 + 검색쿼리[미사용, 사용완료, 기간만료])
	public List<CouponList> getCouponListByPage(int mno, Pager pager, CouponListQuery query) {
		// int sampleMno = 1;
		return couponDAO.getCouponListByPage(mno, pager, query);
	}

	// 쿠폰 목록 갯수 가져오기(유저번호 + 페이징 + 검색쿼리[미사용, 사용완료, 기간만료])
	public int getCouponListCount(int mno, CouponListQuery query) {
		return couponDAO.getCouponListCount(mno, query);
	}

	public enum CouponEventResult {
		SUCCESS, FAIL, HASCOUPON, ERROR;
	};

	/* 이벤트 쿠폰 관련 */
	public CouponEventResult couponEvent(int mno, int couno) {
		// 이벤트 쿠폰 발급 갯수
		if (couponDAO.getCouponEventAmount(couno) < 10) {
			// 이벤트 쿠폰 있는 지 확인하기
			if (couponDAO.hasCouponEvent(mno, couno) > 0) {
				return CouponEventResult.HASCOUPON;
			} else {
				// 이벤트 쿠폰 발급하기
				if (couponDAO.insertCouponEvent(mno, couno) == 1)
					return CouponEventResult.SUCCESS;
				else
					return CouponEventResult.ERROR;
			}
		} else {
			return CouponEventResult.FAIL;
		}
	}
}
