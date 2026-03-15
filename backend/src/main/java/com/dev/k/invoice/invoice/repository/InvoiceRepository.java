package com.dev.k.invoice.invoice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByInvoiceNo(String invoiceNo);

    boolean existsByInvoiceNo(String invoiceNo);

    List<Invoice> findByCustomerId(UUID customerId);

    List<Invoice> findByStatus(InvoiceStatus status);

    List<Invoice> findByDueDateBeforeAndStatus(LocalDate dueDate, InvoiceStatus status);
}