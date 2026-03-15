package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class InvoiceUpdateRequest {

	@Size(max = 255, message = "Title must be 255 characters or less.")
	private String title;
	
	@Positive(message = "Amount must be positive.")
    private Integer amount;
	
    private LocalDate dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}