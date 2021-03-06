package com.bagobank.app.model;

import java.util.ArrayList;
import java.util.Date;

public class Account {

	private int accountId;
	private String accountType;  //checking or saving
	private double balance;
	private Date openedDate;
	private Date closedDate;
	private String status;
	private String approvedBy;
	private Customer customer;
	private ArrayList<Transaction> transaction;  //account transactions
	
	public Account() {
		// TODO Auto-generated constructor stub
	}

	public Account(int accountId) {
		super();
		this.accountId = accountId;
	}

	public Account(int accountId, Customer customer) {
		super();
		this.accountId = accountId;
		this.customer = customer;
	}

	public Account(int accountId, String accountType, double balance, Date openedDate, Date closedDate, String status,
			String approvedBy, Customer customer, ArrayList<Transaction> transaction) {
		super();
		this.accountId = accountId;
		this.accountType = accountType;
		this.balance = balance;
		this.openedDate = openedDate;
		this.closedDate = closedDate;
		this.status = status;
		this.approvedBy = approvedBy;
		this.customer = customer;
		this.transaction = transaction;
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

	public void setAccountType(String accountType) {
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

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public ArrayList<Transaction> getTransaction() {
		return transaction;
	}

	public void setTransaction(ArrayList<Transaction> transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountType=" + accountType + ", balance=" + balance
				+ ", openedDate=" + openedDate + ", closedDate=" + closedDate + ", status=" + status + ", approvedBy="
				+ approvedBy + "]";
	}
	
	
}
