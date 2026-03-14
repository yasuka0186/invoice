package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class InvoiceCreateRequest {

	@NotBlank(message = "Invoice number is required.")
    @Size(max = 50, message = "Invoice number must be 50 characters or less.")
    private String invoiceNo;
	
	@NotNull(message = "Customer ID is required.")
    private UUID customerId;
	
	@NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must be 255 characters or less.")
    private String title;
	
	@NotNull(message = "Amount is required.")
    @Positive(message = "Amount must be positive.")
    private Integer amount;
    
	@NotNull(message = "Due date is required.")
    private LocalDate dueDate;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

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