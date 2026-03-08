package com.dev.k.invoice.invoice.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;

@Service
@Transactional
public class InvoiceStatusUpdateServiceImpl implements InvoiceStatusUpdateService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceStatusUpdateServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void markAsPaid(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(OffsetDateTime.now());

        invoiceRepository.save(invoice);
    }

    @Override
    public void markAsOverdue(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            return;
        }

        if (invoice.getDueDate().isBefore(LocalDate.now())) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
            invoiceRepository.save(invoice);
        }
    }
}