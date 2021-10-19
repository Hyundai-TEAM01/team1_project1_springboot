package com.mycompany.webapp.controller;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.webapp.dao.TestDao;
import com.mycompany.webapp.security.CustomUserDetails;
import com.mycompany.webapp.service.CouponService;
import com.mycompany.webapp.service.CouponService.CouponEventResult;

@Controller
@RequestMapping("/eventtest")
public class TestCouponController {
	private static final Logger logger = LoggerFactory.getLogger(TestCouponController.class);

	// ExecutorService 객체 생성
	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	@Resource
	TestDao dao;

	private static final LocalDateTime EVENT_TIME = LocalDateTime.of(2021, 10, 10, 19, 10, 00);

	// 10명이 넘어가면 true로 바꿈
	private static boolean isLeft = true;

	@RequestMapping(value = "/getcouponevent", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getCouponEvent(int mno, String couponName) throws Exception {
		JSONObject jsonObject = new JSONObject();

		if (!LocalDateTime.now().isEqual(EVENT_TIME) && !LocalDateTime.now().isAfter(EVENT_TIME)) {
			jsonObject.put("result", "notstart");
			return jsonObject.toString();
		}


		Callable<Integer> task = new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				logger.info(Thread.currentThread().getName() + " : 이벤트 처리");
				if (isLeft) {
					/* 쿠폰 발급 서비스 */
					dao.insertCoupon(mno, couponName);
					if(dao.CouponAmount(couponName) > 10000) {
						isLeft = false;
					}

					return 1;
				} else {
					return 0;
				}
			}
		};

		Future<Integer> future = executorService.submit(task);
		logger.info(Thread.currentThread().getName() + ": 큐에 작업을 저장");

		// 이벤트 처리가 완료될 때까지 대기
		int result = future.get();
		if (result == 0) {
			jsonObject.put("result", "fail");
		} else if (result == 2) {
			jsonObject.put("result", "hascoupon");
		} else if (result == -1) {
			jsonObject.put("result", "error");
		} else {
			jsonObject.put("result", "success");
		}
		return jsonObject.toString();
	}
}
