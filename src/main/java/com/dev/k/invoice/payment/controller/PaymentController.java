package com.dev.k.invoice.payment.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.k.invoice.common.dto.ApiResponse;
import com.dev.k.invoice.payment.dto.PaymentCreateRequest;
import com.dev.k.invoice.payment.dto.PaymentResponse;
import com.dev.k.invoice.payment.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ApiResponse<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest request) {
        log.info("API start: create payment. invoiceId={}, paidAmount={}", request.getInvoiceId(), request.getPaidAmount());

        PaymentResponse response = paymentService.create(request);

        log.info("API success: create payment. paymentId={}, invoiceId={}", response.getPaymentId(), response.getInvoiceId());
        return ApiResponse.success(response);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<PaymentResponse>> findByInvoiceId(@PathVariable UUID invoiceId) {
        log.info("API start: find payments by invoiceId. invoiceId={}", invoiceId);

        List<PaymentResponse> response = paymentService.findByInvoiceId(invoiceId);

        log.info("API success: find payments by invoiceId. invoiceId={}, count={}", invoiceId, response.size());
        return ApiResponse.success(response);
    }
}