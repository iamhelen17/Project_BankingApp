package com.bagobank.app.dao;

import com.bagobank.app.exception.BusinessException;
import com.bagobank.app.model.Account;
import com.bagobank.app.model.Customer;
import com.bagobank.app.model.Employee;
import com.bagobank.app.model.Pin;

public interface LoginValidationDAO {

	public Customer logIn(Pin pin) throws BusinessException;
	public boolean isValidAccount(Account account) throws BusinessException;
	
	public Employee logIn();
}
