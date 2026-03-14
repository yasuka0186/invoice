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

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ApiResponse<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest request) {
        return ApiResponse.success(paymentService.create(request));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<PaymentResponse>> findByInvoiceId(@PathVariable UUID invoiceId) {
        return ApiResponse.success(paymentService.findByInvoiceId(invoiceId));
    }
}