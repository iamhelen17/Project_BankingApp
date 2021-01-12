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
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public Employee(int employeeId, String firstName, String lastName, String gender, Date dob, String address1,
			String address2, String city, String state, String zip5, String zip4, String phone1, String phone2,
			String email, Date hiredDate, ArrayList<Customer> customers, ArrayList<Account> accounts) {
		super();
		this.employeeId = employeeId;
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
		this.hiredDate = hiredDate;
		this.customers = customers;
		this.accounts = accounts;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip5() {
		return zip5;
	}

	public void setZip5(String zip5) {
		this.zip5 = zip5;
	}

	public String getZip4() {
		return zip4;
	}

	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(Date hiredDate) {
		this.hiredDate = hiredDate;
	}

	public ArrayList<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", gender=" + gender + ", dob=" + dob + ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", state=" + state + ", zip5=" + zip5 + ", zip4=" + zip4 + ", phone1=" + phone1
				+ ", phone2=" + phone2 + ", email=" + email + ", hiredDate=" + hiredDate + ", customers=" + customers
				+ ", accounts=" + accounts + "]";
	}
	
	
}
