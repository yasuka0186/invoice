package com.dev.k.invoice.batch.overdue.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.batch.overdue.dto.BatchResult;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;

@Service
@Transactional
public class OverdueInvoiceBatchServiceImpl implements OverdueInvoiceBatchService {

    private final InvoiceRepository invoiceRepository;

    public OverdueInvoiceBatchServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public BatchResult execute() {
        List<Invoice> targetInvoices = invoiceRepository.findByDueDateBeforeAndStatus(
                LocalDate.now(),
                InvoiceStatus.ISSUED
        );

        int targetCount = targetInvoices.size();
        int updatedCount = 0;

        for (Invoice invoice : targetInvoices) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
            invoiceRepository.save(invoice);
            updatedCount++;
        }

        return new BatchResult(targetCount, updatedCount);
    }
}