package com.dev.k.invoice.invoice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.invoice.dto.InvoiceSearchRequest;
import com.dev.k.invoice.invoice.dto.InvoiceSummaryResponse;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;
import com.dev.k.invoice.payment.repository.InvoicePaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class InvoiceQueryServiceImpl implements InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;

    public InvoiceQueryServiceImpl(
            InvoiceRepository invoiceRepository,
            InvoicePaymentRepository invoicePaymentRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    @Override
    public List<InvoiceSummaryResponse> search(InvoiceSearchRequest request) {
        log.info(
                "Service start: search invoices. customerId={}, status={}, dueDateFrom={}, dueDateTo={}",
                request.getCustomerId(),
                request.getStatus(),
                request.getDueDateFrom(),
                request.getDueDateTo()
        );

        List<InvoiceSummaryResponse> response = invoiceRepository.findAll()
                .stream()
                .filter(invoice -> request.getCustomerId() == null
                        || invoice.getCustomerId().equals(request.getCustomerId()))
                .filter(invoice -> request.getStatus() == null
                        || invoice.getStatus() == request.getStatus())
                .filter(invoice -> request.getDueDateFrom() == null
                        || !invoice.getDueDate().isBefore(request.getDueDateFrom()))
                .filter(invoice -> request.getDueDateTo() == null
                        || !invoice.getDueDate().isAfter(request.getDueDateTo()))
                .map(this::toSummaryResponse)
                .toList();

        log.info("Service success: search invoices. count={}", response.size());
        return response;
    }

    private InvoiceSummaryResponse toSummaryResponse(Invoice invoice) {
        Integer totalPaidAmount = invoicePaymentRepository.sumPaidAmountByInvoiceId(invoice.getInvoiceId());
        int remainingAmount = invoice.getAmount() - totalPaidAmount;

        InvoiceSummaryResponse response = new InvoiceSummaryResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setInvoiceNo(invoice.getInvoiceNo());
        response.setTitle(invoice.getTitle());
        response.setAmount(invoice.getAmount());
        response.setRemainingAmount(Math.max(remainingAmount, 0));
        response.setDueDate(invoice.getDueDate());
        response.setStatus(invoice.getStatus());
        return response;
    }
}