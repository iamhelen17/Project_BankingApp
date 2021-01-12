package com.bagobank.app.model;

public class Username {

	private int usernameId;
	private String username;
	private Customer customer;
	private Employee employee;
	
	public Username() {
		// TODO Auto-generated constructor stub
	}

	public Username(String username) {
		super();
		this.username = username;
	}

	public Username(int usernameId, String username, Customer customer, Employee employee) {
		super();
		this.usernameId = usernameId;
		this.username = username;
		this.customer = customer;
		this.employee = employee;
	}

	public int getUsernameId() {
		return usernameId;
	}

	public void setUsernameId(int usernameId) {
		this.usernameId = usernameId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "Username [usernameId=" + usernameId + ", username=" + username + ", customer=" + customer
				+ ", employee=" + employee + "]";
	}
	
	
}
