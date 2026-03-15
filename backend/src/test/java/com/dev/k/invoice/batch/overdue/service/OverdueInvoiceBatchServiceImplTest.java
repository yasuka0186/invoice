package com.dev.k.invoice.batch.overdue.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dev.k.invoice.batch.overdue.dto.BatchResult;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class OverdueInvoiceBatchServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private OverdueInvoiceBatchServiceImpl overdueInvoiceBatchService;

    @Test
    @DisplayName("期限超過かつISSUEDの請求をOVERDUEに更新できる")
    void execute_shouldUpdateOverdueInvoices() {
        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceId(UUID.randomUUID());
        invoice1.setInvoiceNo("INV-202603-0001");
        invoice1.setCustomerId(UUID.randomUUID());
        invoice1.setTitle("3月分利用料");
        invoice1.setAmount(10000);
        invoice1.setDueDate(LocalDate.now().minusDays(2));
        invoice1.setStatus(InvoiceStatus.ISSUED);
        invoice1.setIssuedAt(OffsetDateTime.now().minusDays(10));

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceId(UUID.randomUUID());
        invoice2.setInvoiceNo("INV-202603-0002");
        invoice2.setCustomerId(UUID.randomUUID());
        invoice2.setTitle("3月分保守費");
        invoice2.setAmount(20000);
        invoice2.setDueDate(LocalDate.now().minusDays(1));
        invoice2.setStatus(InvoiceStatus.ISSUED);
        invoice2.setIssuedAt(OffsetDateTime.now().minusDays(8));

        when(invoiceRepository.findByDueDateBeforeAndStatus(LocalDate.now(), InvoiceStatus.ISSUED))
                .thenReturn(List.of(invoice1, invoice2));

        BatchResult result = overdueInvoiceBatchService.execute();

        assertThat(result).isNotNull();
        assertThat(result.getTargetCount()).isEqualTo(2);
        assertThat(result.getUpdatedCount()).isEqualTo(2);

        assertThat(invoice1.getStatus()).isEqualTo(InvoiceStatus.OVERDUE);
        assertThat(invoice2.getStatus()).isEqualTo(InvoiceStatus.OVERDUE);

        verify(invoiceRepository, times(1))
                .findByDueDateBeforeAndStatus(LocalDate.now(), InvoiceStatus.ISSUED);
        verify(invoiceRepository, times(2)).save(any(Invoice.class));
    }

    @Test
    @DisplayName("対象データがない場合は更新件数0を返す")
    void execute_shouldReturnZeroWhenNoTargetInvoices() {
        when(invoiceRepository.findByDueDateBeforeAndStatus(LocalDate.now(), InvoiceStatus.ISSUED))
                .thenReturn(List.of());

        BatchResult result = overdueInvoiceBatchService.execute();

        assertThat(result).isNotNull();
        assertThat(result.getTargetCount()).isEqualTo(0);
        assertThat(result.getUpdatedCount()).isEqualTo(0);

        verify(invoiceRepository, times(1))
                .findByDueDateBeforeAndStatus(LocalDate.now(), InvoiceStatus.ISSUED);
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    @DisplayName("取得した全件に対してsaveが呼ばれる")
    void execute_shouldSaveAllTargetInvoices() {
        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceId(UUID.randomUUID());
        invoice1.setInvoiceNo("INV-202603-0003");
        invoice1.setCustomerId(UUID.randomUUID());
        invoice1.setTitle("A請求");
        invoice1.setAmount(5000);
        invoice1.setDueDate(LocalDate.now().minusDays(5));
        invoice1.setStatus(InvoiceStatus.ISSUED);
        invoice1.setIssuedAt(OffsetDateTime.now().minusDays(12));

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceId(UUID.randomUUID());
        invoice2.setInvoiceNo("INV-202603-0004");
        invoice2.setCustomerId(UUID.randomUUID());
        invoice2.setTitle("B請求");
        invoice2.setAmount(8000);
        invoice2.setDueDate(LocalDate.now().minusDays(3));
        invoice2.setStatus(InvoiceStatus.ISSUED);
        invoice2.setIssuedAt(OffsetDateTime.now().minusDays(11));

        Invoice invoice3 = new Invoice();
        invoice3.setInvoiceId(UUID.randomUUID());
        invoice3.setInvoiceNo("INV-202603-0005");
        invoice3.setCustomerId(UUID.randomUUID());
        invoice3.setTitle("C請求");
        invoice3.setAmount(12000);
        invoice3.setDueDate(LocalDate.now().minusDays(1));
        invoice3.setStatus(InvoiceStatus.ISSUED);
        invoice3.setIssuedAt(OffsetDateTime.now().minusDays(9));

        when(invoiceRepository.findByDueDateBeforeAndStatus(LocalDate.now(), InvoiceStatus.ISSUED))
                .thenReturn(List.of(invoice1, invoice2, invoice3));

        BatchResult result = overdueInvoiceBatchService.execute();

        assertThat(result.getTargetCount()).isEqualTo(3);
        assertThat(result.getUpdatedCount()).isEqualTo(3);

        verify(invoiceRepository, times(3)).save(any(Invoice.class));
    }
}