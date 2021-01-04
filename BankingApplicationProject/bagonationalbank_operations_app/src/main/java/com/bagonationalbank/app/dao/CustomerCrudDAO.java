package com.bagonationalbank.app.dao;

import java.util.List;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Customer;

public interface CustomerCrudDAO {

	public int createCustomer(Customer customer) throws BusinessException;
	public List<Customer> getAllCustomers();
}
