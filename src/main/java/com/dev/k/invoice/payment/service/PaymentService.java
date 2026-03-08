package com.dev.k.invoice.payment.service;

import java.util.List;
import java.util.UUID;

import com.dev.k.invoice.payment.dto.PaymentCreateRequest;
import com.dev.k.invoice.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse create(PaymentCreateRequest request);

    List<PaymentResponse> findByInvoiceId(UUID invoiceId);

    int calculateTotalPaidAmount(UUID invoiceId);
}