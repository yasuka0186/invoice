package com.dev.k.invoice.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerCreateRequest {

	@NotBlank(message = "Customer code is required.")
    @Size(max = 50, message = "Customer code must be 50 characters or less.")
	private String customerCode;
	
	@NotBlank(message = "Customer name is required.")
    @Size(max = 255, message = "Customer name must be 255 characters or less.")
	private String name;
	
	@Email(message = "Email must be a valid email address.")
    @Size(max = 255, message = "Email must be 255 characters or less.")
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