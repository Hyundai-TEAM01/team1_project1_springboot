package com.mycompany.webapp.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.webapp.dto.Member;

@Mapper
public interface MemberDAO {
	public Member getLoginMember(String mid);

	public int updateMemberMpoint(@Param("mno") int mno, @Param("mpoint") int mpoint);
	
	public int getCartCnt(int cartno);
}
