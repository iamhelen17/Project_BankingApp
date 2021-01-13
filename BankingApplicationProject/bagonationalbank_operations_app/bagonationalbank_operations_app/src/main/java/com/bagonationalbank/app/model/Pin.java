package com.bagonationalbank.app.model;

public class Pin {

	private int pin_id;
	private String pin; 
	private Username username;
	
	public Pin() {
		// TODO Auto-generated constructor stub
	}
	
	public Pin(String pin, Username username) {
		super();
		this.pin = pin;
		this.username = username;
	}

	public Pin(int pin_id, String pin, Username username) {
		super();
		this.pin_id = pin_id;
		this.pin = pin;
		this.username = username;
	}

	public int getPin_id() {
		return pin_id;
	}

	public void setPin_id(int pin_id) {
		this.pin_id = pin_id;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Username getUsername() {
		return username;
	}

	public void setUsername(Username username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Pin [pin_id=" + pin_id + ", pin=" + pin + ", username=" + username + "]";
	}
}
