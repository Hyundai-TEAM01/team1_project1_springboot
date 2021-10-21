package com.mycompany.webapp.controller;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.webapp.dao.TestDao;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/testevent")
@Slf4j
public class TestRedisEventController {
	// ExecutorService 객체 생성
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	@Resource
	private RedisTemplate<String, String> template;

	private static final LocalDateTime EVENT_TIME = LocalDateTime.of(2021, 10, 10, 19, 10, 00);

	// 쿠폰 수량 여부
	private static boolean isLeft = true;

	//
	private static final int COUPON_AMOUNT = 10000;


	@Resource
	TestDao dao;

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

				log.info(Thread.currentThread().getName() + " : 이벤트 처리");

				if (isLeft) {
					template.boundZSetOps(couponName).add(String.valueOf(mno), System.nanoTime());
					if (!template.boundZSetOps(couponName).range(0, COUPON_AMOUNT-1).contains(String.valueOf(mno))) {
						isLeft = false;
					}
					return 1;
				} else {
					template.boundZSetOps(couponName).addIfAbsent(String.valueOf(mno), System.nanoTime());
					return 0;
				}
			}
		};

		Future<Integer> future = executorService.submit(task);
		log.info(Thread.currentThread().getName() + ": 큐에 작업을 저장");


		// 이벤트 처리가 완료될 때까지 대기
		int result = future.get();
		if (result == 0) {
			jsonObject.put("result", "fail");
		} else if (result == 1) {
			jsonObject.put("result", "success");
		}

		return jsonObject.toString();
	}
}
