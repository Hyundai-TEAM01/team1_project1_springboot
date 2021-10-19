package com.mycompany.webapp.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.webapp.dto.CartDetail;
import com.mycompany.webapp.dto.CartProductDetail;
import com.mycompany.webapp.dto.CartProductInfo;

@Mapper
public interface CartDAO {
	public CartDetail getCartDetailByOption(int cartDetailNo, String pcolor, String psize);
	
	public List<CartDetail> getCartDetailList(int cartNo);

	public List<CartProductInfo> getCartProductList(int cartNo);

	public int removeCartDetail(int cartDetailNo);

	public int removeCartDetailList(String[] cartDetailNoList);

	public int insertCartDetail(@Param("cartno")int cartno, @Param("pcode")String pcode, @Param("psize")String psize, @Param("pcolor")String pcolor, @Param("pamount")int pamount);

	public CartDetail selectByCartDetailNo(int cartDetailNo);

	public List<CartProductInfo> getCartProductListByArray(String[] pList, int cartno);
	
	public CartDetail getCartDetailByCartdetailno(int cartdetailno, int cartno);
	
	public int getCartNoByMno(int mno);
	
	public int updateCartdetail(CartDetail cartDetail);
	
	public int updateCartdetailOption(CartDetail cartDetail);
	
	public int purchaseCartDetail(int cartDetailNo);

	public int purchaseCartDetailList(String[] cartDetailNoList);
	
	public List<CartProductDetail> getCartProductDetailByPcode(String pcode);
	
	public int getCartProductCount(@Param("cartno")int cartno, @Param("pcode")String pcode, @Param("psize")String psize, @Param("pcolor")String pcolor);
}
