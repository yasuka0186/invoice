package com.dev.k.invoice.invoice.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.common.constant.ErrorCode;
import com.dev.k.invoice.common.exception.BusinessException;
import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.customer.repository.CustomerRepository;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.dto.InvoiceCreateRequest;
import com.dev.k.invoice.invoice.dto.InvoiceResponse;
import com.dev.k.invoice.invoice.dto.InvoiceUpdateRequest;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;
import com.dev.k.invoice.payment.repository.InvoicePaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            CustomerRepository customerRepository,
            InvoicePaymentRepository invoicePaymentRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    @Override
    public InvoiceResponse create(InvoiceCreateRequest request) {
        log.info("Service start: create invoice. invoiceNo={}, customerId={}", request.getInvoiceNo(), request.getCustomerId());

        if (!customerRepository.existsById(request.getCustomerId())) {
            log.warn("Resource not found: customer. customerId={}", request.getCustomerId());
            throw new ResourceNotFoundException("Customer not found.");
        }

        if (invoiceRepository.existsByInvoiceNo(request.getInvoiceNo())) {
            log.warn("Validation error: invoice number already exists. invoiceNo={}", request.getInvoiceNo());
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Invoice number already exists.");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setInvoiceNo(request.getInvoiceNo());
        invoice.setCustomerId(request.getCustomerId());
        invoice.setTitle(request.getTitle());
        invoice.setAmount(request.getAmount());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssuedAt(OffsetDateTime.now());
        invoice.setPaidAt(null);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        log.info("Service success: create invoice. invoiceId={}, invoiceNo={}", savedInvoice.getInvoiceId(), savedInvoice.getInvoiceNo());
        return toResponse(savedInvoice);
    }

    @Override
    public InvoiceResponse update(UUID invoiceId, InvoiceUpdateRequest request) {
        log.info("Service start: update invoice. invoiceId={}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            log.warn("Invalid status for invoice update. invoiceId={}, status={}", invoiceId, invoice.getStatus());
            throw new BusinessException(ErrorCode.INVALID_STATUS, "Cannot update paid or cancelled invoice.");
        }

        invoice.setTitle(request.getTitle());
        invoice.setAmount(request.getAmount());
        invoice.setDueDate(request.getDueDate());

        Invoice updatedInvoice = invoiceRepository.save(invoice);

        log.info("Service success: update invoice. invoiceId={}", updatedInvoice.getInvoiceId());
        return toResponse(updatedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse findById(UUID invoiceId) {
        log.info("Service start: find invoice by id. invoiceId={}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        log.info("Service success: find invoice by id. invoiceId={}", invoice.getInvoiceId());
        return toResponse(invoice);
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        Integer totalPaidAmount = invoicePaymentRepository.sumPaidAmountByInvoiceId(invoice.getInvoiceId());
        int remainingAmount = invoice.getAmount() - totalPaidAmount;

        InvoiceResponse response = new InvoiceResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setInvoiceNo(invoice.getInvoiceNo());
        response.setCustomerId(invoice.getCustomerId());
        response.setTitle(invoice.getTitle());
        response.setAmount(invoice.getAmount());
        response.setTotalPaidAmount(totalPaidAmount);
        response.setRemainingAmount(Math.max(remainingAmount, 0));
        response.setDueDate(invoice.getDueDate());
        response.setStatus(invoice.getStatus());
        response.setIssuedAt(invoice.getIssuedAt());
        response.setPaidAt(invoice.getPaidAt());
        return response;
    }
}