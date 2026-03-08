package com.dev.k.invoice.customer.dto;

public class CustomerCreateRequest {

	private String customerCode;
	private String name;
	private String email;

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode=customerCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email=email;
    }
}