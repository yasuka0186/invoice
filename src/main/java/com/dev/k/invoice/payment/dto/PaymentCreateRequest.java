package com.dev.k.invoice.payment.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.dev.k.invoice.payment.constant.PaymentMethod;

public class PaymentCreateRequest {

	@NotNull(message = "Invoice ID is required.")
	private UUID invoiceId;
	
	@NotNull(message = "Paid amount is required.")
    @Positive(message = "Paid amount must be positive.")
    private Integer paidAmount;
	
	@NotNull(message = "Paid date-time is required.")
    private OffsetDateTime paidAt;
	
	@NotNull(message = "Payment method is required.")
    private PaymentMethod method;
	
	@Size(max = 500, message = "Note must be 500 characters or less.")
    private String note;

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}