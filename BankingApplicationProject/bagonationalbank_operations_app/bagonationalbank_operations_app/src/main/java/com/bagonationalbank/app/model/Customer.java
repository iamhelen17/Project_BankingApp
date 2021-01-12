package com.bagonationalbank.app.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Customer {
	private int customerId;  //id number of customer
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
	private Date joinDate;
	private ArrayList<Account> accounts; //list of customer accounts
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	
	public Customer(int customerId) {
		this.customerId = customerId;
	}

	public Customer(int customerId, String firstName, String lastName, String gender, Date dob, String address1,
			String address2, String city, String state, String zip5, String zip4, String phone1, String phone2,
			String email, Date joinDate, ArrayList<Account> accounts) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dob = dob;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip5 = zip5;
		this.zip4 = zip4;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.email = email;
		this.joinDate = joinDate;
		this.accounts = accounts;
	}

	public Customer(String firstName, String lastName, String gender, Date dob, String address1,
			String address2, String city, String state, String zip5, String zip4, String phone1, String phone2,
			String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dob = dob;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip5 = zip5;
		this.zip4 = zip4;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.email = email;
		this.joinDate = Calendar.getInstance().getTime();
	}

	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getCity() {
		return city;
	}

	public int getCustomerId() {
		return customerId;
	}

	public Date getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getGender() {
		return gender;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone1() {
		return phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public String getState() {
		return state;
	}

	public String getZip4() {
		return zip4;
	}

	public String getZip5() {
		return zip5;
	}


	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	public void setZip5(String zip5) {
		this.zip5 = zip5;
	}

	
	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", gender=" + gender + ", dob=" + dob + ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", state=" + state + ", zip5=" + zip5 + ", zip4=" + zip4 + ", phone1=" + phone1
				+ ", phone2=" + phone2 + ", email=" + email + ", joinDate=" + joinDate + ", accounts=" + accounts + "]";
	}
}
