package com.dev.k.invoice.invoice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.k.invoice.common.dto.ApiResponse;
import com.dev.k.invoice.invoice.dto.InvoiceCreateRequest;
import com.dev.k.invoice.invoice.dto.InvoiceResponse;
import com.dev.k.invoice.invoice.dto.InvoiceSearchRequest;
import com.dev.k.invoice.invoice.dto.InvoiceSummaryResponse;
import com.dev.k.invoice.invoice.dto.InvoiceUpdateRequest;
import com.dev.k.invoice.invoice.service.InvoiceQueryService;
import com.dev.k.invoice.invoice.service.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceQueryService invoiceQueryService;

    public InvoiceController(
            InvoiceService invoiceService,
            InvoiceQueryService invoiceQueryService
    ) {
        this.invoiceService = invoiceService;
        this.invoiceQueryService = invoiceQueryService;
    }

    @PostMapping
    public ApiResponse<InvoiceResponse> create(@RequestBody InvoiceCreateRequest request) {
        return ApiResponse.success(invoiceService.create(request));
    }

    @PutMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> update(
            @PathVariable UUID invoiceId,
            @RequestBody InvoiceUpdateRequest request
    ) {
        return ApiResponse.success(invoiceService.update(invoiceId, request));
    }

    @GetMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> findById(@PathVariable UUID invoiceId) {
        return ApiResponse.success(invoiceService.findById(invoiceId));
    }

    @PostMapping("/search")
    public ApiResponse<List<InvoiceSummaryResponse>> search(@RequestBody InvoiceSearchRequest request) {
        return ApiResponse.success(invoiceQueryService.search(request));
    }
}