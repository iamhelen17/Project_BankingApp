package com.bagonationalbank.app.model;

import java.util.Date;

public class Transactions {

	private int transactionId;
	private Date transactionDate;   //date and time of transaction
	private double amount;
	private Account account;
	private Customer customer;
}
