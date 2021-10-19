package com.mycompany.webapp.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestDao {
	public int CouponAmount(String couname);
	public int insertCoupon(@Param("mno") int mno, @Param("couname") String couname);
}
