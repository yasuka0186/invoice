package com.dev.k.invoice.invoice.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    public ApiResponse<InvoiceResponse> create(@Valid @RequestBody InvoiceCreateRequest request) {
        log.info("API start: create invoice. invoiceNo={}, customerId={}", request.getInvoiceNo(), request.getCustomerId());

        InvoiceResponse response = invoiceService.create(request);

        log.info("API success: create invoice. invoiceId={}, invoiceNo={}", response.getInvoiceId(), response.getInvoiceNo());
        return ApiResponse.success(response);
    }

    @PutMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> update(
            @PathVariable UUID invoiceId,
            @Valid @RequestBody InvoiceUpdateRequest request
    ) {
        log.info("API start: update invoice. invoiceId={}", invoiceId);

        InvoiceResponse response = invoiceService.update(invoiceId, request);

        log.info("API success: update invoice. invoiceId={}", response.getInvoiceId());
        return ApiResponse.success(response);
    }

    @GetMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> findById(@PathVariable UUID invoiceId) {
        log.info("API start: find invoice by id. invoiceId={}", invoiceId);

        InvoiceResponse response = invoiceService.findById(invoiceId);

        log.info("API success: find invoice by id. invoiceId={}, status={}", response.getInvoiceId(), response.getStatus());
        return ApiResponse.success(response);
    }

    @PostMapping("/search")
    public ApiResponse<List<InvoiceSummaryResponse>> search(@RequestBody InvoiceSearchRequest request) {
        log.info(
                "API start: search invoices. customerId={}, status={}, dueDateFrom={}, dueDateTo={}",
                request.getCustomerId(),
                request.getStatus(),
                request.getDueDateFrom(),
                request.getDueDateTo()
        );

        List<InvoiceSummaryResponse> response = invoiceQueryService.search(request);

        log.info("API success: search invoices. count={}", response.size());
        return ApiResponse.success(response);
    }
}