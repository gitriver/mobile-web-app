/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.catt.mobile.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.catt.mobile.model.Account;
import com.catt.mobile.service.MobileService;

@Controller
@SessionAttributes(types = Account.class)
public class AccountController {

	private final MobileService mobileService;

	@Autowired
	public AccountController(MobileService clinicService) {
		this.mobileService = clinicService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@RequestMapping(value = "/account/new", method = RequestMethod.GET)
	public String initCreationForm(Map<String, Object> model) {
		Account account = new Account();
		model.put("account", account);
		return "accounts/createOrUpdateAccountForm";
	}

	@RequestMapping(value = "/account/new", method = RequestMethod.POST)
	public String processCreationForm(@Valid Account account,
			BindingResult result, SessionStatus status) {
		if (result.hasErrors()) {
			return "accounts/createOrUpdateAccountForm";
		} else {
			this.mobileService.saveAccount(account);
			status.setComplete();
			return "redirect:/accounts/" + account.getId();
		}
	}

	@RequestMapping(value = "/accounts/find", method = RequestMethod.GET)
	public String initFindForm(Map<String, Object> model) {
		model.put("account", new Account());
		return "accounts/findAccounts";
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public String processFindForm(Account account, BindingResult result,
			Map<String, Object> model) {

		// allow parameterless GET request for /accounts to return all records
		if (account.getName() == null) {
			account.setName(""); // empty string signifies broadest possible
									// search
		}

		// find accounts by last name
		Collection<Account> results = this.mobileService
				.findAccountByName(account.getName());
		if (results.size() < 1) {
			// no accounts found
			result.rejectValue("lastName", "notFound", "not found");
			return "accounts/findaccounts";
		}
		if (results.size() > 1) {
			// multiple accounts found
			model.put("selections", results);
			return "accounts/accountsList";
		} else {
			// 1 account found
			account = results.iterator().next();
			return "redirect:/accounts/" + account.getId();
		}
	}

	@RequestMapping(value = "/accounts/{accountId}/edit", method = RequestMethod.GET)
	public String initUpdateAccountForm(
			@PathVariable("accountId") int accountId, Model model) {
		Account account = this.mobileService.findAccountById(accountId);
		model.addAttribute(account);
		return "accounts/createOrUpdateAccountForm";
	}

	@RequestMapping(value = "/accounts/{accountId}/edit", method = RequestMethod.PUT)
	public String processUpdateAccountForm(@Valid Account account,
			BindingResult result, SessionStatus status) {
		if (result.hasErrors()) {
			return "accounts/createOrUpdateAccountForm";
		} else {
			this.mobileService.saveAccount(account);
			status.setComplete();
			return "redirect:/accounts/{accountId}";
		}
	}

	/**
	 * Custom handler for displaying an account.
	 * 
	 * @param accountId
	 *            the ID of the account to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@RequestMapping("/accounts/{accountId}")
	public ModelAndView showAccount(@PathVariable("accountId") int accountId) {
		ModelAndView mav = new ModelAndView("accounts/accountDetails");
		mav.addObject(this.mobileService.findAccountById(accountId));
		return mav;
	}

	@RequestMapping(value = "/accounts2/{accountId}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> showAccount2(@PathVariable("accountId") int accountId) {
		List<String> list=new ArrayList<String>();
		list.add("中国");
		list.add("0国");
		return list;
	}
	
	@RequestMapping(value = "/accounts/list", method = RequestMethod.GET)
	@ResponseBody
	public List<String> listAccount() {
		List list=this.mobileService.listAccount();
		return list;
	}

}
