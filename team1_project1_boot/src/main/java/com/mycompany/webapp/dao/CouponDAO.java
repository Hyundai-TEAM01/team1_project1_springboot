package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.webapp.dto.CouponList;
import com.mycompany.webapp.dto.CouponListQuery;
import com.mycompany.webapp.dto.Pager;

@Mapper
public interface CouponDAO {
	// 쿠폰 목록 가져오기(유저번호 + 페이징 + 검색쿼리[사용, 미사용, 기간만료])
	public List<CouponList> getCouponListByPage(@Param("mno") int mno, @Param("pager") Pager pager,
			@Param("query") CouponListQuery query);

	// 쿠폰 목록 갯수 가져오기(유저번호 + 페이징 + 검색쿼리[사용, 미사용, 기간만료])
	public int getCouponListCount(@Param("mno") int mno, @Param("query") CouponListQuery query);

	/* 이벤트 쿠폰 관련 */
	// 이벤트 쿠폰 발급 갯수
	public int getCouponEventAmount(@Param("couno") int couno);

	// 이벤트 쿠폰 있는 지 확인하기
	public int hasCouponEvent(@Param("mno") int mno, @Param("couno") int couno);

	// 이벤트 쿠폰 발급하기
	public int insertCouponEvent(@Param("mno") int mno, @Param("couno") int couno);
}
