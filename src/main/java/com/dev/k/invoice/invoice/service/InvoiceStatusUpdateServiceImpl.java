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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class InvoiceStatusUpdateServiceImpl implements InvoiceStatusUpdateService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceStatusUpdateServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void markAsPaid(UUID invoiceId) {
        log.info("Service start: mark invoice as paid. invoiceId={}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(OffsetDateTime.now());
        invoiceRepository.save(invoice);

        log.info("Service success: mark invoice as paid. invoiceId={}", invoiceId);
    }

    @Override
    public void markAsOverdue(UUID invoiceId) {
        log.info("Service start: mark invoice as overdue. invoiceId={}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            log.info("Skip overdue update. invoiceId={}, status={}", invoiceId, invoice.getStatus());
            return;
        }

        if (invoice.getDueDate().isBefore(LocalDate.now())) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
            invoiceRepository.save(invoice);
            log.info("Service success: mark invoice as overdue. invoiceId={}", invoiceId);
            return;
        }

        log.info("Skip overdue update. invoiceId={}, dueDate={}", invoiceId, invoice.getDueDate());
    }
}