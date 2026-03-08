package com.dev.k.invoice.payment.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.dev.k.invoice.payment.constant.PaymentMethod;

public class PaymentCreateRequest {

    private UUID invoiceId;
    private Integer paidAmount;
    private OffsetDateTime paidAt;
    private PaymentMethod method;
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