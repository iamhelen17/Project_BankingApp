package com.bagobank.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bagobank.app.dao.LoginValidationDAO;
import com.bagobank.app.dao.dbutil.PostgresqlConnection;
import com.bagobank.app.exception.BusinessException;
import com.bagobank.app.model.Account;
import com.bagobank.app.model.Customer;
import com.bagobank.app.model.Employee;
import com.bagobank.app.model.Pin;
import com.bagonationalbank.app.dao.dbutil.PostresqlConnection;



public class LoginValidationDAOImpl implements LoginValidationDAO{
	private static Logger log = Logger.getLogger(LoginValidationDAOImpl.class);
	private Account account;

	@Override
	public Customer logIn(Pin pin) throws BusinessException {
		Customer customer = null;
		
		try (Connection connection = PostgresqlConnection.getConnection()) {
			String sql = "select distinct u.username, u.customer_id, p.pin, c.first_name, c.last_name "
					+ "from bago_national_bank.username u "
					+ "join bago_national_bank.pin p on u.username_id = p.username_id "
					+ "join bago_national_bank.customer c on u.customer_id = c.customer_id "
					+ "join bago_national_bank.account a on a.customer_id = c.customer_id where u.username = ? and p.pin = ? and a.status = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, customerCredentials.getUsername().getUsername());
			preparedStatement.setString(2, customerCredentials.getPin());
			preparedStatement.setString(3, "active");
			
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				customer = new Customer();
				customer.setCustomerId(resultSet.getInt("customer_id"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
			} else {
				throw new BusinessException("\nNo Customer found with Username: "+customerCredentials.getUsername().getUsername()+ " and Pin: "+customerCredentials.getPin());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException("Internal error occurred. Contact Admin!!! logIn");
		}
				
				
		return customer;
	}

	@Override
	public boolean isValidAccount(Account account) throws BusinessException {
		try (Connection connection = PostgresqlConnection.getConnection()) {
			
			String sql = "select distinct account_id from bago_national_bank.account where account_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, account.getAccountId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException("Internal error. Contact SYSADMIN! isValidAccount");
		}
	}

	@Override
	public Employee logIn() {
		// TODO Auto-generated method stub
		return null;
	}

}
