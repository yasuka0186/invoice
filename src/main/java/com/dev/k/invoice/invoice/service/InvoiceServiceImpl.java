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
        if (!customerRepository.existsById(request.getCustomerId())) {
            throw new ResourceNotFoundException("Customer not found.");
        }

        if (invoiceRepository.existsByInvoiceNo(request.getInvoiceNo())) {
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
        return toResponse(savedInvoice);
    }

    @Override
    public InvoiceResponse update(UUID invoiceId, InvoiceUpdateRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.INVALID_STATUS, "Cannot update paid or cancelled invoice.");
        }

        invoice.setTitle(request.getTitle());
        invoice.setAmount(request.getAmount());
        invoice.setDueDate(request.getDueDate());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return toResponse(updatedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse findById(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));
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