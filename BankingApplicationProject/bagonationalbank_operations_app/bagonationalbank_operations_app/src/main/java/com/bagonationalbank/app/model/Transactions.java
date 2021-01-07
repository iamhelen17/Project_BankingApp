package com.bagonationalbank.app.model;

import java.util.Date;

public class Transactions {

	private int transactionId;
	private String type;
	private double amount;
	private int oldBalance;
	private int newBalance;
	private Date transactionDate;   //date and time of transaction
	private Account account;
	private Customer customer;
	
	public Transactions() {
		// TODO Auto-generated constructor stub
	}

	public Transactions(int transactionId, Date transactionDate, String type, double amount, int oldBalance,
			int newBalance, Account account, Customer customer) {
		super();
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.type = type;
		this.amount = amount;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
		this.account = account;
		this.customer = customer;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getOldBalance() {
		return oldBalance;
	}

	public void setOldBalance(int oldBalance) {
		this.oldBalance = oldBalance;
	}

	public int getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(int newBalance) {
		this.newBalance = newBalance;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Transactions [transactionId=" + transactionId + ", transactionDate=" + transactionDate + ", type="
				+ type + ", amount=" + amount + ", oldBalance=" + oldBalance + ", newBalance=" + newBalance
				+ ", account=" + account + ", customer=" + customer + "]";
	}
	
	
}

