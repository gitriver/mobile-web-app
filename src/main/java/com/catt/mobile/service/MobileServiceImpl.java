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
package com.catt.mobile.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catt.mobile.model.Account;
import com.catt.mobile.repository.AccountRepository;

@Service
public class MobileServiceImpl implements MobileService {

    private AccountRepository accountRepository;

    @Autowired
    public MobileServiceImpl(AccountRepository ownerRepository) {
        this.accountRepository = ownerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountById(int id) throws DataAccessException {
        return this.accountRepository.findById(id);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public Collection<Account> findAccountByName(String name) throws DataAccessException {
        return this.accountRepository.findByName(name);
    }


    @Override
    @Transactional
    public void saveAccount(Account account) throws DataAccessException {
    	this.accountRepository.save(account);
    }
    
    @Override
    @Transactional
    public List<Account> listAccount(){
    	return this.accountRepository.listAccount();
    }
    
    @Override
    @Transactional
    public List<Account> listAccount2(){
    	return this.accountRepository.listAccount2();
    }
}
