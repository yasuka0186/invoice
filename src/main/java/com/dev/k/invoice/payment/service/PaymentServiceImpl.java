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

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        Integer currentTotal = paymentRepository.sumPaidAmountByInvoiceId(invoice.getInvoiceId());
        int newTotal = currentTotal + request.getPaidAmount();

        if (newTotal > invoice.getAmount()) {
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
            invoiceStatusUpdateService.markAsPaid(invoice.getInvoiceId());
        }

        return toResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findByInvoiceId(UUID invoiceId) {
        return paymentRepository.findByInvoiceIdOrderByPaidAtAsc(invoiceId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public int calculateTotalPaidAmount(UUID invoiceId) {
        return paymentRepository.sumPaidAmountByInvoiceId(invoiceId);
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