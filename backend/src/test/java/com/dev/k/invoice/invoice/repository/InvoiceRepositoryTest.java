package com.dev.k.invoice.invoice.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;

@DataJpaTest
@ActiveProfiles("test")
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    @DisplayName("invoiceNoで請求を取得できる")
    void findByInvoiceNo_shouldReturnInvoice() {
        UUID customerId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setInvoiceNo("INV-202603-0001");
        invoice.setCustomerId(customerId);
        invoice.setTitle("3月分利用料");
        invoice.setAmount(10000);
        invoice.setDueDate(LocalDate.of(2026, 3, 31));
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssuedAt(OffsetDateTime.now());
        invoice.setPaidAt(null);

        invoiceRepository.save(invoice);

        Optional<Invoice> result = invoiceRepository.findByInvoiceNo("INV-202603-0001");

        assertThat(result).isPresent();
        assertThat(result.get().getInvoiceNo()).isEqualTo("INV-202603-0001");
        assertThat(result.get().getCustomerId()).isEqualTo(customerId);
    }

    @Test
    @DisplayName("customerIdで請求一覧を取得できる")
    void findByCustomerId_shouldReturnInvoices() {
        UUID customerId = UUID.randomUUID();

        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceId(UUID.randomUUID());
        invoice1.setInvoiceNo("INV-202603-0002");
        invoice1.setCustomerId(customerId);
        invoice1.setTitle("A請求");
        invoice1.setAmount(10000);
        invoice1.setDueDate(LocalDate.of(2026, 3, 10));
        invoice1.setStatus(InvoiceStatus.ISSUED);
        invoice1.setIssuedAt(OffsetDateTime.now());

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceId(UUID.randomUUID());
        invoice2.setInvoiceNo("INV-202603-0003");
        invoice2.setCustomerId(customerId);
        invoice2.setTitle("B請求");
        invoice2.setAmount(20000);
        invoice2.setDueDate(LocalDate.of(2026, 3, 20));
        invoice2.setStatus(InvoiceStatus.PAID);
        invoice2.setIssuedAt(OffsetDateTime.now());

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        List<Invoice> result = invoiceRepository.findByCustomerId(customerId);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("期限超過かつISSUEDの請求を取得できる")
    void findByDueDateBeforeAndStatus_shouldReturnTargetInvoices() {
        Invoice target = new Invoice();
        target.setInvoiceId(UUID.randomUUID());
        target.setInvoiceNo("INV-202603-0004");
        target.setCustomerId(UUID.randomUUID());
        target.setTitle("期限超過対象");
        target.setAmount(15000);
        target.setDueDate(LocalDate.now().minusDays(1));
        target.setStatus(InvoiceStatus.ISSUED);
        target.setIssuedAt(OffsetDateTime.now());

        Invoice notTargetPaid = new Invoice();
        notTargetPaid.setInvoiceId(UUID.randomUUID());
        notTargetPaid.setInvoiceNo("INV-202603-0005");
        notTargetPaid.setCustomerId(UUID.randomUUID());
        notTargetPaid.setTitle("完済済み");
        notTargetPaid.setAmount(15000);
        notTargetPaid.setDueDate(LocalDate.now().minusDays(2));
        notTargetPaid.setStatus(InvoiceStatus.PAID);
        notTargetPaid.setIssuedAt(OffsetDateTime.now());

        Invoice notTargetFuture = new Invoice();
        notTargetFuture.setInvoiceId(UUID.randomUUID());
        notTargetFuture.setInvoiceNo("INV-202603-0006");
        notTargetFuture.setCustomerId(UUID.randomUUID());
        notTargetFuture.setTitle("未来日付");
        notTargetFuture.setAmount(15000);
        notTargetFuture.setDueDate(LocalDate.now().plusDays(3));
        notTargetFuture.setStatus(InvoiceStatus.ISSUED);
        notTargetFuture.setIssuedAt(OffsetDateTime.now());

        invoiceRepository.save(target);
        invoiceRepository.save(notTargetPaid);
        invoiceRepository.save(notTargetFuture);

        List<Invoice> result = invoiceRepository.findByDueDateBeforeAndStatus(
                LocalDate.now(),
                InvoiceStatus.ISSUED
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInvoiceNo()).isEqualTo("INV-202603-0004");
    }
}