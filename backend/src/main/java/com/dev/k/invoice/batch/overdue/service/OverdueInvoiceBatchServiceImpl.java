package com.dev.k.invoice.batch.overdue.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.batch.overdue.dto.BatchResult;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OverdueInvoiceBatchServiceImpl implements OverdueInvoiceBatchService {

    private final InvoiceRepository invoiceRepository;

    public OverdueInvoiceBatchServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public BatchResult execute() {
        log.info("Batch start: overdue invoice update.");

        List<Invoice> targetInvoices = invoiceRepository.findByDueDateBeforeAndStatus(
                LocalDate.now(),
                InvoiceStatus.ISSUED
        );

        int targetCount = targetInvoices.size();
        int updatedCount = 0;

        log.info("Batch target invoices found. targetCount={}", targetCount);

        for (Invoice invoice : targetInvoices) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
            invoiceRepository.save(invoice);
            updatedCount++;
            log.info("Batch updated invoice to overdue. invoiceId={}", invoice.getInvoiceId());
        }

        log.info("Batch finished: overdue invoice update. targetCount={}, updatedCount={}", targetCount, updatedCount);
        return new BatchResult(targetCount, updatedCount);
    }
}