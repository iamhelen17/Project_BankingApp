package com.bagonationalbank.app.model;

import java.util.ArrayList;
import java.util.Date;

public class Employee {

	private int employeeId;  //id number of employee
	private String firstName;
	private String lastName;
	private String gender;
	private Date dob;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip5;
	private String zip4;
	private String phone1;
	private String phone2;
	private String email;
	private Date hiredDate;
	//private Username username;
	//private Pin pin;
	private ArrayList<Customer> customers;
	private ArrayList<Account> accounts; //list of customer accounts
}
