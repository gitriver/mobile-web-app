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
package com.catt.mobile.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import com.catt.mobile.model.Account;
import com.catt.mobile.repository.AccountRepository;

@Repository
public class JdbcAccountRepositoryImpl implements AccountRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcInsert insertAccount;

	@Autowired
	public JdbcAccountRepositoryImpl(DataSource dataSource,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

		this.insertAccount = new SimpleJdbcInsert(dataSource).withTableName(
				"account").usingGeneratedKeyColumns("id");

		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
	}

	@Override
	public Collection<Account> findByName(String name)
			throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name + "%");
		List<Account> account = this.namedParameterJdbcTemplate
				.query("SELECT id, name, address, telephone,email FROM account WHERE name like :name",
						params, ParameterizedBeanPropertyRowMapper
								.newInstance(Account.class));
		return account;
	}

	@Override
	public Account findById(int id) throws DataAccessException {
		Account account;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			account = this.namedParameterJdbcTemplate
					.queryForObject(
							"SELECT id, name, address, telephone,email FROM account WHERE id= :id",
							params, ParameterizedBeanPropertyRowMapper
									.newInstance(Account.class));
		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(Account.class, id);
		}
		return account;
	}

	@Override
	public void save(Account account) throws DataAccessException {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(
				account);
		if (account.isNew()) {
			Number newKey = this.insertAccount
					.executeAndReturnKey(parameterSource);
			account.setId(newKey.intValue());
		} else {
			this.namedParameterJdbcTemplate
					.update("UPDATE account SET name=:name, address=:address, "
							+ "email=:email, telephone=:telephone WHERE id=:id",
							parameterSource);
		}
	}

	@Override
	public List<Account> listAccount() {
		List<Account> list;
		try {

			list = this.namedParameterJdbcTemplate
					.query("SELECT id, name,  address, telephone,email FROM account",
							ParameterizedBeanPropertyRowMapper
									.newInstance(Account.class));
		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(Account.class, 1);
		}
		return list;
	}
}
