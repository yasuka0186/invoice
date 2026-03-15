package com.dev.k.invoice.invoice.service;

import java.util.UUID;

public interface InvoiceStatusUpdateService {

    void markAsPaid(UUID invoiceId);

    void markAsOverdue(UUID invoiceId);
}