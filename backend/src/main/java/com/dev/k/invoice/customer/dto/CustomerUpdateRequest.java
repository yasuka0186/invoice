package com.dev.k.invoice.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class CustomerUpdateRequest {

	@Size(max = 255, message = "Customer name must be 255 characters or less.")
    private String name;
	
	@Email(message = "Email must be a valid email address.")
    @Size(max = 255, message = "Email must be 255 characters or less.")
    private String email;
	
    private Boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}