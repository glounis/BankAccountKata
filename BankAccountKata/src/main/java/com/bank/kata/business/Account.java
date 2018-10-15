package com.bank.kata.business;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
	@Id
	private long id; 
	private String countNumer; 
	private float balance;
	private int pinCode;
	//other attributes if need... 
	
	//getters and setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCountNumer() {
		return countNumer;
	}
	public void setCountNumer(String countNumer) {
		this.countNumer = countNumer;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public int getPinCode() {
		return pinCode;
	}
	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}
	

}
