package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.dev.k.invoice.invoice.constant.InvoiceStatus;

public class InvoiceSearchRequest {

    private UUID customerId;
    private InvoiceStatus status;
    private LocalDate dueDateFrom;
    private LocalDate dueDateTo;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public LocalDate getDueDateFrom() {
        return dueDateFrom;
    }

    public void setDueDateFrom(LocalDate dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }

    public LocalDate getDueDateTo() {
        return dueDateTo;
    }

    public void setDueDateTo(LocalDate dueDateTo) {
        this.dueDateTo = dueDateTo;
    }
}