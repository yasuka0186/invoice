package com.dev.k.invoice.payment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dev.k.invoice.common.constant.ErrorCode;
import com.dev.k.invoice.common.exception.BusinessException;
import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;
import com.dev.k.invoice.invoice.service.InvoiceStatusUpdateService;
import com.dev.k.invoice.payment.constant.PaymentMethod;
import com.dev.k.invoice.payment.dto.PaymentCreateRequest;
import com.dev.k.invoice.payment.dto.PaymentResponse;
import com.dev.k.invoice.payment.entity.InvoicePayment;
import com.dev.k.invoice.payment.repository.InvoicePaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoicePaymentRepository paymentRepository;

    @Mock
    private InvoiceStatusUpdateService invoiceStatusUpdateService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("支払登録：正常系（未完済）")
    void createPayment_success_notFullyPaid() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setInvoiceNo("INV-202603-0001");
        invoice.setCustomerId(UUID.randomUUID());
        invoice.setTitle("3月分利用料");
        invoice.setAmount(10000);
        invoice.setDueDate(LocalDate.of(2026, 3, 31));

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setInvoiceId(invoiceId);
        request.setPaidAmount(3000);
        request.setPaidAt(OffsetDateTime.parse("2026-03-10T10:00:00+09:00"));
        request.setMethod(PaymentMethod.BANK_TRANSFER);
        request.setNote("1回目支払");

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(paymentRepository.sumPaidAmountByInvoiceId(invoiceId)).thenReturn(2000);

        InvoicePayment savedPayment = new InvoicePayment();
        savedPayment.setPaymentId(UUID.randomUUID());
        savedPayment.setInvoiceId(invoiceId);
        savedPayment.setPaidAmount(3000);
        savedPayment.setPaidAt(request.getPaidAt());
        savedPayment.setMethod(request.getMethod());
        savedPayment.setNote(request.getNote());

        when(paymentRepository.save(any(InvoicePayment.class))).thenReturn(savedPayment);

        PaymentResponse result = paymentService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceId()).isEqualTo(invoiceId);
        assertThat(result.getPaidAmount()).isEqualTo(3000);
        assertThat(result.getMethod()).isEqualTo(PaymentMethod.BANK_TRANSFER);
        assertThat(result.getNote()).isEqualTo("1回目支払");

        verify(paymentRepository, times(1)).save(any(InvoicePayment.class));
        verify(invoiceStatusUpdateService, never()).markAsPaid(any(UUID.class));
    }

    @Test
    @DisplayName("支払登録：正常系（完済）")
    void createPayment_success_fullyPaid() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setInvoiceNo("INV-202603-0002");
        invoice.setCustomerId(UUID.randomUUID());
        invoice.setTitle("3月分サポート費");
        invoice.setAmount(10000);
        invoice.setDueDate(LocalDate.of(2026, 3, 31));

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setInvoiceId(invoiceId);
        request.setPaidAmount(4000);
        request.setPaidAt(OffsetDateTime.parse("2026-03-11T10:00:00+09:00"));
        request.setMethod(PaymentMethod.CREDIT_CARD);
        request.setNote("完済分");

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(paymentRepository.sumPaidAmountByInvoiceId(invoiceId)).thenReturn(6000);

        InvoicePayment savedPayment = new InvoicePayment();
        savedPayment.setPaymentId(UUID.randomUUID());
        savedPayment.setInvoiceId(invoiceId);
        savedPayment.setPaidAmount(4000);
        savedPayment.setPaidAt(request.getPaidAt());
        savedPayment.setMethod(request.getMethod());
        savedPayment.setNote(request.getNote());

        when(paymentRepository.save(any(InvoicePayment.class))).thenReturn(savedPayment);

        PaymentResponse result = paymentService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceId()).isEqualTo(invoiceId);
        assertThat(result.getPaidAmount()).isEqualTo(4000);

        verify(paymentRepository, times(1)).save(any(InvoicePayment.class));
        verify(invoiceStatusUpdateService, times(1)).markAsPaid(invoiceId);
    }

    @Test
    @DisplayName("支払登録：請求が存在しない場合は例外")
    void createPayment_invoiceNotFound() {
        UUID invoiceId = UUID.randomUUID();

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setInvoiceId(invoiceId);
        request.setPaidAmount(3000);
        request.setPaidAt(OffsetDateTime.now());
        request.setMethod(PaymentMethod.CASH);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invoice not found.");

        verify(paymentRepository, never()).save(any(InvoicePayment.class));
        verify(invoiceStatusUpdateService, never()).markAsPaid(any(UUID.class));
    }

    @Test
    @DisplayName("支払登録：過入金の場合は例外")
    void createPayment_overPayment() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setAmount(10000);

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setInvoiceId(invoiceId);
        request.setPaidAmount(3000);
        request.setPaidAt(OffsetDateTime.now());
        request.setMethod(PaymentMethod.BANK_TRANSFER);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(paymentRepository.sumPaidAmountByInvoiceId(invoiceId)).thenReturn(8000);

        assertThatThrownBy(() -> paymentService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Payment exceeds invoice amount.")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.OVER_PAYMENT);

        verify(paymentRepository, never()).save(any(InvoicePayment.class));
        verify(invoiceStatusUpdateService, never()).markAsPaid(any(UUID.class));
    }

    @Test
    @DisplayName("支払合計取得：正常系")
    void calculateTotalPaidAmount_success() {
        UUID invoiceId = UUID.randomUUID();

        when(paymentRepository.sumPaidAmountByInvoiceId(invoiceId)).thenReturn(9000);

        int result = paymentService.calculateTotalPaidAmount(invoiceId);

        assertThat(result).isEqualTo(9000);
        verify(paymentRepository, times(1)).sumPaidAmountByInvoiceId(invoiceId);
    }

    @Test
    @DisplayName("支払履歴取得：正常系")
    void findByInvoiceId_success() {
        UUID invoiceId = UUID.randomUUID();

        InvoicePayment payment1 = new InvoicePayment();
        payment1.setPaymentId(UUID.randomUUID());
        payment1.setInvoiceId(invoiceId);
        payment1.setPaidAmount(3000);
        payment1.setPaidAt(OffsetDateTime.parse("2026-03-01T10:00:00+09:00"));
        payment1.setMethod(PaymentMethod.BANK_TRANSFER);
        payment1.setNote("1回目");

        InvoicePayment payment2 = new InvoicePayment();
        payment2.setPaymentId(UUID.randomUUID());
        payment2.setInvoiceId(invoiceId);
        payment2.setPaidAmount(5000);
        payment2.setPaidAt(OffsetDateTime.parse("2026-03-02T10:00:00+09:00"));
        payment2.setMethod(PaymentMethod.CASH);
        payment2.setNote("2回目");

        when(paymentRepository.findByInvoiceIdOrderByPaidAtAsc(invoiceId))
                .thenReturn(List.of(payment1, payment2));

        List<PaymentResponse> result = paymentService.findByInvoiceId(invoiceId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPaidAmount()).isEqualTo(3000);
        assertThat(result.get(1).getPaidAmount()).isEqualTo(5000);
        assertThat(result.get(0).getInvoiceId()).isEqualTo(invoiceId);
    }

    @Test
    @DisplayName("支払登録時に保存される支払情報が正しい")
    void createPayment_shouldSaveCorrectPaymentData() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setAmount(10000);

        OffsetDateTime paidAt = OffsetDateTime.parse("2026-03-15T12:00:00+09:00");

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setInvoiceId(invoiceId);
        request.setPaidAmount(2500);
        request.setPaidAt(paidAt);
        request.setMethod(PaymentMethod.CREDIT_CARD);
        request.setNote("検証用");

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(paymentRepository.sumPaidAmountByInvoiceId(invoiceId)).thenReturn(0);

        InvoicePayment savedPayment = new InvoicePayment();
        savedPayment.setPaymentId(UUID.randomUUID());
        savedPayment.setInvoiceId(invoiceId);
        savedPayment.setPaidAmount(2500);
        savedPayment.setPaidAt(paidAt);
        savedPayment.setMethod(PaymentMethod.CREDIT_CARD);
        savedPayment.setNote("検証用");

        when(paymentRepository.save(any(InvoicePayment.class))).thenReturn(savedPayment);

        paymentService.create(request);

        ArgumentCaptor<InvoicePayment> captor = ArgumentCaptor.forClass(InvoicePayment.class);
        verify(paymentRepository).save(captor.capture());

        InvoicePayment captured = captor.getValue();
        assertThat(captured.getInvoiceId()).isEqualTo(invoiceId);
        assertThat(captured.getPaidAmount()).isEqualTo(2500);
        assertThat(captured.getPaidAt()).isEqualTo(paidAt);
        assertThat(captured.getMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
        assertThat(captured.getNote()).isEqualTo("検証用");
        assertThat(captured.getPaymentId()).isNotNull();
    }
}