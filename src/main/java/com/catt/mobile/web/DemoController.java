package com.catt.mobile.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catt.mobile.model.Account;

@Controller
@RequestMapping("/demo")
public class DemoController {
	private Logger logger = LoggerFactory.getLogger(DemoController.class);

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getUserList() {
		logger.info("列表");
		List<Account> list = new ArrayList<Account>();
		Random rand = new Random(47);
		for (int i = 0; i < 10; i++) {
			Account account = new Account();
			account.setId(i);
			account.setName("张三" + i);
			account.setAddress(i * rand.nextInt(10) + 2+"gz china");
			list.add(account);
		}
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", "10");
		modelMap.put("data", list);
		modelMap.put("success", "true");
		return modelMap;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> addAccount(@RequestBody Account model) {
		logger.info("新增");
		logger.info("捕获到前台传递过来的Model，名称为：" + model.getName());
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("success", "true");
		return map;
	}

	@RequestMapping(value = "/assign", method = RequestMethod.POST)
	@ResponseBody
	public String assign(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "add[]", required = false) int[] add,
			@RequestParam(value = "del[]", required = false) int[] del) {
		logger.info("userId:" + userId);
		logger.info("add:" + add);
		logger.info("del:" + del);
		return "";
	}
}

