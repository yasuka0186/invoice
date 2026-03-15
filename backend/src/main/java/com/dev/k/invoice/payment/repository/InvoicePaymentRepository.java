package com.dev.k.invoice.payment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.k.invoice.payment.entity.InvoicePayment;

public interface InvoicePaymentRepository extends JpaRepository<InvoicePayment, UUID> {

    List<InvoicePayment> findByInvoiceIdOrderByPaidAtAsc(UUID invoiceId);

    @Query("""
            select coalesce(sum(ip.paidAmount), 0)
            from InvoicePayment ip
            where ip.invoiceId = :invoiceId
            """)
    Integer sumPaidAmountByInvoiceId(UUID invoiceId);
}