package com.bagonationalbank.app.model;

import java.util.Date;

public class Transaction {

	private int transactionId;
	private String transactionType;
	private double amount;
	private double oldBalance;
	private double newBalance;
	private Date transactionDate;   //date and time of transaction
	private Account account;
	private Customer customer;
	private String status;
	private int linkedTransactionId;
	
	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	public Transaction(int transactionId) {
		super();
		this.transactionId = transactionId;
	}
	
	public Transaction(int transactionId, String transactionType, double amount, double oldBalance, double newBalance,
			Date transactionDate, Account account, Customer customer, String status, int linkedTransactionId) {
		super();
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
		this.transactionDate = transactionDate;
		this.account = account;
		this.customer = customer;
		this.status = status;
		this.linkedTransactionId = linkedTransactionId;
	}

	public Transaction(int transactionId, String transactionType, double amount, double oldBalance, double newBalance,
			Date transactionDate, Account account, Customer customer, String status) {
		super();
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
		this.transactionDate = transactionDate;
		this.account = account;
		this.customer = customer;
		this.status = status;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getOldBalance() {
		return oldBalance;
	}

	public void setOldBalance(double oldBalance) {
		this.oldBalance = oldBalance;
	}

	public double getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLinkedTransactionId() {
		return linkedTransactionId;
	}

	public void setLinkedTransactionId(int linkedTransactionId) {
		this.linkedTransactionId = linkedTransactionId;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", transactionType=" + transactionType + ", amount="
				+ amount + ", oldBalance=" + oldBalance + ", newBalance=" + newBalance + ", transactionDate="
				+ transactionDate + ", account=" + account + ", customer=" + customer + ", status=" + status
				+ ", linkedTransactionId=" + linkedTransactionId + "]";
	}
	
	
}

