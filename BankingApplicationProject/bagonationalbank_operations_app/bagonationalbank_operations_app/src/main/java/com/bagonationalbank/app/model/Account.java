package com.bagonationalbank.app.model;

import java.util.ArrayList;
import java.util.Date;

public class Account {

	private int accountId;
	private String accountType;  
	private double balance;
	private Date openedDate;
	private Date closedDate;
	private String status;
	private int approvedBy;
	private Customer customer;
	
	
	public Account() {
		// TODO Auto-generated constructor stub
	}
	
	public Account (int accountId) {
		super();
		this.accountId = accountId;		
	}
	
	public Account (int accountId, Customer customer) {
		super();
		this.accountId = accountId;
		this.customer = customer;		
	}
	
	public Account (int accountId, String accountType, double balance, Date openedDate, String status, Customer customer) {
		super();
		this.accountId = accountId;
		this.accountType = accountType;
		this.balance = balance;
		this.openedDate = openedDate;
		this.status = status;
		this.customer = customer;
	}
	
	

	public Account(int accountId, String accountType, double balance, Date openedDate, Date closedDate, String status,
			int approvedBy, Customer customer) {
		super();
		this.accountId = accountId;    
		this.accountType = accountType;
		this.balance = balance;
		this.openedDate = openedDate;
		this.closedDate = closedDate;
		this.status = status;
		this.approvedBy = approvedBy;
		this.customer = customer;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setType(String accountType) {
		this.accountType = accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(Date openedDate) {
		this.openedDate = openedDate;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountType=" + accountType + ", balance=" + balance
				+ ", openedDate=" + openedDate + ", closedDate=" + closedDate + ", status=" + status + ", approvedBy="
				+ approvedBy + ", customer=" + customer + "]";
	}
	
}
