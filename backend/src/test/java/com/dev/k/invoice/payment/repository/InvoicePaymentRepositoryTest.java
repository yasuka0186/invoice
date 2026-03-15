package com.dev.k.invoice.payment.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.dev.k.invoice.payment.constant.PaymentMethod;
import com.dev.k.invoice.payment.entity.InvoicePayment;

@DataJpaTest
@ActiveProfiles("test")
class InvoicePaymentRepositoryTest {

    @Autowired
    private InvoicePaymentRepository invoicePaymentRepository;

    @Test
    @DisplayName("invoiceIdで支払履歴を支払日時昇順で取得できる")
    void findByInvoiceIdOrderByPaidAtAsc_shouldReturnPaymentsInOrder() {
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
        payment2.setMethod(PaymentMethod.CREDIT_CARD);
        payment2.setNote("2回目");

        invoicePaymentRepository.save(payment2);
        invoicePaymentRepository.save(payment1);

        List<InvoicePayment> result = invoicePaymentRepository.findByInvoiceIdOrderByPaidAtAsc(invoiceId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPaidAmount()).isEqualTo(3000);
        assertThat(result.get(1).getPaidAmount()).isEqualTo(5000);
    }

    @Test
    @DisplayName("invoiceIdごとの支払合計を取得できる")
    void sumPaidAmountByInvoiceId_shouldReturnSum() {
        UUID invoiceId = UUID.randomUUID();

        InvoicePayment payment1 = new InvoicePayment();
        payment1.setPaymentId(UUID.randomUUID());
        payment1.setInvoiceId(invoiceId);
        payment1.setPaidAmount(4000);
        payment1.setPaidAt(OffsetDateTime.now());
        payment1.setMethod(PaymentMethod.BANK_TRANSFER);
        payment1.setNote("A");

        InvoicePayment payment2 = new InvoicePayment();
        payment2.setPaymentId(UUID.randomUUID());
        payment2.setInvoiceId(invoiceId);
        payment2.setPaidAmount(6000);
        payment2.setPaidAt(OffsetDateTime.now().plusDays(1));
        payment2.setMethod(PaymentMethod.CASH);
        payment2.setNote("B");

        invoicePaymentRepository.save(payment1);
        invoicePaymentRepository.save(payment2);

        Integer result = invoicePaymentRepository.sumPaidAmountByInvoiceId(invoiceId);

        assertThat(result).isEqualTo(10000);
    }

    @Test
    @DisplayName("支払履歴がない場合は合計0を返す")
    void sumPaidAmountByInvoiceId_shouldReturnZeroWhenNoPayment() {
        Integer result = invoicePaymentRepository.sumPaidAmountByInvoiceId(UUID.randomUUID());

        assertThat(result).isEqualTo(0);
    }
}