package com.bank.kata.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Account {
	@Id
	private long id; 
	private String countNumer; 
	@Column(columnDefinition="Decimal(10,2)")
	private double balance;
	@Column(unique=true)
	private int pinCode;
	//other attributes if need... 
	
	//here, I consider that on client can have many accounts, but one account is private for 
	// just only one client. I don't manage the case when an account is for many persons. 
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name="client_id" , nullable = false)
	private Client client;
	
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
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getPinCode() {
		return pinCode;
	}
	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
}
