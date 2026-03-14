package com.dev.k.invoice.batch.overdue.service;

import com.dev.k.invoice.batch.overdue.dto.BatchResult;

public interface OverdueInvoiceBatchService {

    BatchResult execute();
}