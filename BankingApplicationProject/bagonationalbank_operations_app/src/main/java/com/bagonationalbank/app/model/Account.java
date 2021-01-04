package com.bagonationalbank.app.model;

import java.util.ArrayList;
import java.util.Date;

public class Account {

	private int accountId;
	private String type;  //checking or saving
	private double balance;
	private Date openedDate;
	private Date closedDate;
	private String status;
	private String approvedBy;
	private Customer customer;
	private ArrayList<Transactions> transactions;  //account transactions
	
	public Account() {
		// TODO Auto-generated constructor stub
	}

	public Account(int accountId, String type, double balance, Date openedDate, Date closedDate, String status,
			String approvedBy, Customer customer, ArrayList<Transactions> transactions) {
		super();
		this.accountId = accountId;
		this.type = type;
		this.balance = balance;
		this.openedDate = openedDate;
		this.closedDate = closedDate;
		this.status = status;
		this.approvedBy = approvedBy;
		this.customer = customer;
		this.transactions = transactions;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public ArrayList<Transactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<Transactions> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", type=" + type + ", balance=" + balance + ", openedDate="
				+ openedDate + ", closedDate=" + closedDate + ", status=" + status + ", approvedBy=" + approvedBy
				+ ", customer=" + customer + ", transactions=" + transactions + "]";
	}
	
	
}
