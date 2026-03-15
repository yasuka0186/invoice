package com.dev.k.invoice.invoice.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InvoicePaymentRepository invoicePaymentRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    @DisplayName("請求登録：正常系")
    void createInvoice_success() {
        UUID customerId = UUID.randomUUID();

        InvoiceCreateRequest request = new InvoiceCreateRequest();
        request.setInvoiceNo("INV-202603-0001");
        request.setCustomerId(customerId);
        request.setTitle("3月分利用料");
        request.setAmount(10000);
        request.setDueDate(LocalDate.of(2026, 3, 31));

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(invoiceRepository.existsByInvoiceNo("INV-202603-0001")).thenReturn(false);

        Invoice savedInvoice = new Invoice();
        savedInvoice.setInvoiceId(UUID.randomUUID());
        savedInvoice.setInvoiceNo("INV-202603-0001");
        savedInvoice.setCustomerId(customerId);
        savedInvoice.setTitle("3月分利用料");
        savedInvoice.setAmount(10000);
        savedInvoice.setDueDate(LocalDate.of(2026, 3, 31));
        savedInvoice.setStatus(InvoiceStatus.ISSUED);
        savedInvoice.setIssuedAt(OffsetDateTime.now());
        savedInvoice.setPaidAt(null);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);
        when(invoicePaymentRepository.sumPaidAmountByInvoiceId(savedInvoice.getInvoiceId())).thenReturn(0);

        InvoiceResponse result = invoiceService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceNo()).isEqualTo("INV-202603-0001");
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getStatus()).isEqualTo(InvoiceStatus.ISSUED);
        assertThat(result.getTotalPaidAmount()).isEqualTo(0);
        assertThat(result.getRemainingAmount()).isEqualTo(10000);

        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    @DisplayName("請求登録：顧客が存在しない場合は例外")
    void createInvoice_customerNotFound() {
        UUID customerId = UUID.randomUUID();

        InvoiceCreateRequest request = new InvoiceCreateRequest();
        request.setInvoiceNo("INV-202603-0002");
        request.setCustomerId(customerId);
        request.setTitle("3月分利用料");
        request.setAmount(10000);
        request.setDueDate(LocalDate.of(2026, 3, 31));

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThatThrownBy(() -> invoiceService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found.");

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    @DisplayName("請求登録：請求番号重複の場合は例外")
    void createInvoice_duplicateInvoiceNo() {
        UUID customerId = UUID.randomUUID();

        InvoiceCreateRequest request = new InvoiceCreateRequest();
        request.setInvoiceNo("INV-202603-0003");
        request.setCustomerId(customerId);
        request.setTitle("3月分利用料");
        request.setAmount(10000);
        request.setDueDate(LocalDate.of(2026, 3, 31));

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(invoiceRepository.existsByInvoiceNo("INV-202603-0003")).thenReturn(true);

        assertThatThrownBy(() -> invoiceService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Invoice number already exists.")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.VALIDATION_ERROR);

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    @DisplayName("請求更新：正常系")
    void updateInvoice_success() {
        UUID invoiceId = UUID.randomUUID();

        Invoice existingInvoice = new Invoice();
        existingInvoice.setInvoiceId(invoiceId);
        existingInvoice.setInvoiceNo("INV-202603-0004");
        existingInvoice.setCustomerId(UUID.randomUUID());
        existingInvoice.setTitle("旧タイトル");
        existingInvoice.setAmount(10000);
        existingInvoice.setDueDate(LocalDate.of(2026, 3, 31));
        existingInvoice.setStatus(InvoiceStatus.ISSUED);
        existingInvoice.setIssuedAt(OffsetDateTime.now());

        InvoiceUpdateRequest request = new InvoiceUpdateRequest();
        request.setTitle("新タイトル");
        request.setAmount(12000);
        request.setDueDate(LocalDate.of(2026, 4, 10));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(existingInvoice);
        when(invoicePaymentRepository.sumPaidAmountByInvoiceId(invoiceId)).thenReturn(0);

        InvoiceResponse result = invoiceService.update(invoiceId, request);

        assertThat(result.getTitle()).isEqualTo("新タイトル");
        assertThat(result.getAmount()).isEqualTo(12000);
        assertThat(result.getDueDate()).isEqualTo(LocalDate.of(2026, 4, 10));
    }

    @Test
    @DisplayName("請求更新：請求が存在しない場合は例外")
    void updateInvoice_notFound() {
        UUID invoiceId = UUID.randomUUID();

        InvoiceUpdateRequest request = new InvoiceUpdateRequest();
        request.setTitle("新タイトル");
        request.setAmount(12000);
        request.setDueDate(LocalDate.of(2026, 4, 10));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.update(invoiceId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invoice not found.");

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    @DisplayName("請求更新：PAID は更新不可")
    void updateInvoice_paidStatus_shouldThrowException() {
        UUID invoiceId = UUID.randomUUID();

        Invoice existingInvoice = new Invoice();
        existingInvoice.setInvoiceId(invoiceId);
        existingInvoice.setStatus(InvoiceStatus.PAID);

        InvoiceUpdateRequest request = new InvoiceUpdateRequest();
        request.setTitle("更新不可");
        request.setAmount(9999);
        request.setDueDate(LocalDate.of(2026, 4, 1));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));

        assertThatThrownBy(() -> invoiceService.update(invoiceId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot update paid or cancelled invoice.")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_STATUS);

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    @DisplayName("請求更新：CANCELLED は更新不可")
    void updateInvoice_cancelledStatus_shouldThrowException() {
        UUID invoiceId = UUID.randomUUID();

        Invoice existingInvoice = new Invoice();
        existingInvoice.setInvoiceId(invoiceId);
        existingInvoice.setStatus(InvoiceStatus.CANCELLED);

        InvoiceUpdateRequest request = new InvoiceUpdateRequest();
        request.setTitle("更新不可");
        request.setAmount(9999);
        request.setDueDate(LocalDate.of(2026, 4, 1));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));

        assertThatThrownBy(() -> invoiceService.update(invoiceId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot update paid or cancelled invoice.")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_STATUS);

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    @DisplayName("請求取得：正常系")
    void findById_success() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setInvoiceNo("INV-202603-0005");
        invoice.setCustomerId(UUID.randomUUID());
        invoice.setTitle("請求詳細");
        invoice.setAmount(15000);
        invoice.setDueDate(LocalDate.of(2026, 3, 31));
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssuedAt(OffsetDateTime.now());
        invoice.setPaidAt(null);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(invoicePaymentRepository.sumPaidAmountByInvoiceId(invoiceId)).thenReturn(5000);

        InvoiceResponse result = invoiceService.findById(invoiceId);

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceId()).isEqualTo(invoiceId);
        assertThat(result.getInvoiceNo()).isEqualTo("INV-202603-0005");
        assertThat(result.getAmount()).isEqualTo(15000);
        assertThat(result.getTotalPaidAmount()).isEqualTo(5000);
        assertThat(result.getRemainingAmount()).isEqualTo(10000);
    }

    @Test
    @DisplayName("請求取得：請求が存在しない場合は例外")
    void findById_notFound() {
        UUID invoiceId = UUID.randomUUID();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.findById(invoiceId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invoice not found.");
    }

    @Test
    @DisplayName("請求登録時に保存される請求情報が正しい")
    void createInvoice_shouldSaveCorrectInvoiceData() {
        UUID customerId = UUID.randomUUID();

        InvoiceCreateRequest request = new InvoiceCreateRequest();
        request.setInvoiceNo("INV-202603-0006");
        request.setCustomerId(customerId);
        request.setTitle("保存確認");
        request.setAmount(18000);
        request.setDueDate(LocalDate.of(2026, 3, 25));

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(invoiceRepository.existsByInvoiceNo("INV-202603-0006")).thenReturn(false);

        Invoice savedInvoice = new Invoice();
        savedInvoice.setInvoiceId(UUID.randomUUID());
        savedInvoice.setInvoiceNo("INV-202603-0006");
        savedInvoice.setCustomerId(customerId);
        savedInvoice.setTitle("保存確認");
        savedInvoice.setAmount(18000);
        savedInvoice.setDueDate(LocalDate.of(2026, 3, 25));
        savedInvoice.setStatus(InvoiceStatus.ISSUED);
        savedInvoice.setIssuedAt(OffsetDateTime.now());

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);
        when(invoicePaymentRepository.sumPaidAmountByInvoiceId(savedInvoice.getInvoiceId())).thenReturn(0);

        invoiceService.create(request);

        var captor = forClass(Invoice.class);
        verify(invoiceRepository).save(captor.capture());

        Invoice captured = captor.getValue();
        assertThat(captured.getInvoiceId()).isNotNull();
        assertThat(captured.getInvoiceNo()).isEqualTo("INV-202603-0006");
        assertThat(captured.getCustomerId()).isEqualTo(customerId);
        assertThat(captured.getTitle()).isEqualTo("保存確認");
        assertThat(captured.getAmount()).isEqualTo(18000);
        assertThat(captured.getDueDate()).isEqualTo(LocalDate.of(2026, 3, 25));
        assertThat(captured.getStatus()).isEqualTo(InvoiceStatus.ISSUED);
        assertThat(captured.getIssuedAt()).isNotNull();
        assertThat(captured.getPaidAt()).isNull();
    }
}