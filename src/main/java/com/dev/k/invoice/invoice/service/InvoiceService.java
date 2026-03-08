package com.dev.k.invoice.invoice.service;

import java.util.UUID;

import com.dev.k.invoice.invoice.dto.InvoiceCreateRequest;
import com.dev.k.invoice.invoice.dto.InvoiceResponse;
import com.dev.k.invoice.invoice.dto.InvoiceUpdateRequest;

public interface InvoiceService {

    InvoiceResponse create(InvoiceCreateRequest request);

    InvoiceResponse update(UUID invoiceId, InvoiceUpdateRequest request);

    InvoiceResponse findById(UUID invoiceId);
}