package com.bagobank.app.dao;

import com.bagobank.app.exception.BusinessException;
import com.bagobank.app.model.Account;

public interface AccountDAO {

	public Account getBalance(Account account) throws BusinessException;
	public int updateBalance(Account account, double amount) throws BusinessException;
}
