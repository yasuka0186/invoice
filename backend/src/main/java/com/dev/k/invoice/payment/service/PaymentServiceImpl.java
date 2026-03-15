package com.dev.k.invoice.payment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.common.constant.ErrorCode;
import com.dev.k.invoice.common.exception.BusinessException;
import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;
import com.dev.k.invoice.invoice.service.InvoiceStatusUpdateService;
import com.dev.k.invoice.payment.dto.PaymentCreateRequest;
import com.dev.k.invoice.payment.dto.PaymentResponse;
import com.dev.k.invoice.payment.entity.InvoicePayment;
import com.dev.k.invoice.payment.repository.InvoicePaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceRepository invoiceRepository;
    private final InvoicePaymentRepository paymentRepository;
    private final InvoiceStatusUpdateService invoiceStatusUpdateService;

    public PaymentServiceImpl(
            InvoiceRepository invoiceRepository,
            InvoicePaymentRepository paymentRepository,
            InvoiceStatusUpdateService invoiceStatusUpdateService
    ) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.invoiceStatusUpdateService = invoiceStatusUpdateService;
    }

    @Override
    public PaymentResponse create(PaymentCreateRequest request) {
        log.info("Service start: create payment. invoiceId={}, paidAmount={}", request.getInvoiceId(), request.getPaidAmount());

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", request.getInvoiceId());
                    return new ResourceNotFoundException("Invoice not found.");
                });

        Integer currentTotal = paymentRepository.sumPaidAmountByInvoiceId(invoice.getInvoiceId());
        int newTotal = currentTotal + request.getPaidAmount();

        log.info(
                "Payment amount check. invoiceId={}, invoiceAmount={}, currentTotal={}, requestAmount={}, newTotal={}",
                invoice.getInvoiceId(),
                invoice.getAmount(),
                currentTotal,
                request.getPaidAmount(),
                newTotal
        );

        if (newTotal > invoice.getAmount()) {
            log.warn(
                    "Over payment detected. invoiceId={}, invoiceAmount={}, currentTotal={}, requestAmount={}, newTotal={}",
                    invoice.getInvoiceId(),
                    invoice.getAmount(),
                    currentTotal,
                    request.getPaidAmount(),
                    newTotal
            );
            throw new BusinessException(ErrorCode.OVER_PAYMENT, "Payment exceeds invoice amount.");
        }

        InvoicePayment payment = new InvoicePayment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setInvoiceId(request.getInvoiceId());
        payment.setPaidAmount(request.getPaidAmount());
        payment.setPaidAt(request.getPaidAt());
        payment.setMethod(request.getMethod());
        payment.setNote(request.getNote());

        InvoicePayment savedPayment = paymentRepository.save(payment);

        if (newTotal == invoice.getAmount()) {
            log.info("Invoice fully paid. invoiceId={}", invoice.getInvoiceId());
            invoiceStatusUpdateService.markAsPaid(invoice.getInvoiceId());
        }

        log.info("Service success: create payment. paymentId={}, invoiceId={}", savedPayment.getPaymentId(), savedPayment.getInvoiceId());
        return toResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findByInvoiceId(UUID invoiceId) {
        log.info("Service start: find payments by invoiceId. invoiceId={}", invoiceId);

        List<PaymentResponse> response = paymentRepository.findByInvoiceIdOrderByPaidAtAsc(invoiceId)
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Service success: find payments by invoiceId. invoiceId={}, count={}", invoiceId, response.size());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public int calculateTotalPaidAmount(UUID invoiceId) {
        log.info("Service start: calculate total paid amount. invoiceId={}", invoiceId);

        int totalPaidAmount = paymentRepository.sumPaidAmountByInvoiceId(invoiceId);

        log.info("Service success: calculate total paid amount. invoiceId={}, totalPaidAmount={}", invoiceId, totalPaidAmount);
        return totalPaidAmount;
    }

    private PaymentResponse toResponse(InvoicePayment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setInvoiceId(payment.getInvoiceId());
        response.setPaidAmount(payment.getPaidAmount());
        response.setPaidAt(payment.getPaidAt());
        response.setMethod(payment.getMethod());
        response.setNote(payment.getNote());
        return response;
    }
}