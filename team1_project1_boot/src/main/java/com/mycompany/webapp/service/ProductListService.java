package com.mycompany.webapp.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.ProductDAO;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.ProductAmountList;
import com.mycompany.webapp.dto.ProductDetail;
import com.mycompany.webapp.dto.ProductList;

@Service
public class ProductListService {
	private static final Logger logger = LoggerFactory.getLogger(ProductListService.class);
	
	@Resource
	private ProductDAO productDAO;
	
	public List<ProductList> getProductList(String ccode, Pager pager){
		return productDAO.selectByCategoryPage(ccode, pager);
	}
	public int getTotalProducListtNum(String ccode) {
		return productDAO.productListCount(ccode);
	}
	public ProductDetail getProductDetail(String pcode) {
		return productDAO.getProductDetail(pcode);
	}
	
	public ProductAmountList getProductDetailAmountList(String pcode, String pcolor) {
		return productDAO.getProductDetailAmountList(pcode, pcolor);
	}
	
}
