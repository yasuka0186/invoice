package com.dev.k.invoice.invoice.service;

import java.util.List;

import com.dev.k.invoice.invoice.dto.InvoiceSearchRequest;
import com.dev.k.invoice.invoice.dto.InvoiceSummaryResponse;

public interface InvoiceQueryService {

    List<InvoiceSummaryResponse> search(InvoiceSearchRequest request);
}