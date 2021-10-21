package com.mycompany.webapp.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.ProductDAO;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.ProductAmountList;
import com.mycompany.webapp.dto.ProductDetail;
import com.mycompany.webapp.dto.ProductList;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductListService {
	
	@Resource
	private ProductDAO productDAO;
	
	public List<ProductList> getProductList(String ccode, Pager pager){
		log.info("실행");
		return productDAO.selectByCategoryPage(ccode, pager);
	}
	public int getTotalProducListtNum(String ccode) {
		log.info("실행");
		return productDAO.productListCount(ccode);
	}
	public ProductDetail getProductDetail(String pcode) {
		log.info("실행");
		return productDAO.getProductDetail(pcode);
	}
	
	public ProductAmountList getProductDetailAmountList(String pcode, String pcolor) {
		log.info("실행");
		return productDAO.getProductDetailAmountList(pcode, pcolor);
	}
	
}
