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

/**
 * 支払サービス実装クラス（PaymentServiceImpl）
 *
 * 【役割】
 * - 支払に関する業務ロジックを実装するクラス
 * - 支払登録・取得・集計処理を担当
 *
 * 【アノテーション】
 * - @Service：Spring管理のサービスクラス
 * - @Transactional：トランザクション制御（更新系）
 *
 * 【依存コンポーネント】
 * - InvoiceRepository：請求の取得
 * - InvoicePaymentRepository：支払の登録・取得・集計
 * - InvoiceStatusUpdateService：請求ステータス更新
 *
 * --------------------------------------------------
 * 【処理概要】
 *
 * ■ create（支払登録）
 * - 請求存在チェック
 * - 現在の支払合計を取得
 * - 過払いチェック（newTotal > 請求金額）
 * - 支払データを登録
 * - 完済時はステータスをPAIDに更新
 *
 * ■ findByInvoiceId（支払一覧取得）
 * - 請求IDに紐づく支払一覧を取得
 * - DTOに変換して返却
 *
 * ■ calculateTotalPaidAmount（支払合計取得）
 * - DBから支払合計を取得
 *
 * --------------------------------------------------
 * 【業務ロジック】
 *
 * ■ 過払いチェック
 * - (現在支払合計 + 今回支払額) > 請求金額 の場合エラー
 *
 * ■ 完済判定
 * - 支払合計 == 請求金額 の場合
 *   → ステータスを PAID に更新
 *
 * 【利用フロー】
 * Controller → 本Service → Repository → Entity操作 → DTO変換 → Controller → フロント
 */
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

    /**
     * 支払登録
     *
     * @param request 支払登録リクエスト
     * @return 登録された支払情報
     */
    @Override
    public PaymentResponse create(PaymentCreateRequest request) {
        log.info("Service start: create payment. invoiceId={}, paidAmount={}", request.getInvoiceId(), request.getPaidAmount());

        // 請求存在チェック
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", request.getInvoiceId());
                    return new ResourceNotFoundException("Invoice not found.");
                });

        // 現在の支払合計取得
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

        // 過払いチェック
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

        // 支払登録
        InvoicePayment payment = new InvoicePayment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setInvoiceId(request.getInvoiceId());
        payment.setPaidAmount(request.getPaidAmount());
        payment.setPaidAt(request.getPaidAt());
        payment.setMethod(request.getMethod());
        payment.setNote(request.getNote());

        InvoicePayment savedPayment = paymentRepository.save(payment);

        // 完済時ステータス更新
        if (newTotal == invoice.getAmount()) {
            log.info("Invoice fully paid. invoiceId={}", invoice.getInvoiceId());
            invoiceStatusUpdateService.markAsPaid(invoice.getInvoiceId());
        }

        log.info("Service success: create payment. paymentId={}, invoiceId={}", savedPayment.getPaymentId(), savedPayment.getInvoiceId());
        return toResponse(savedPayment);
    }

    /**
     * 支払一覧取得
     */
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

    /**
     * 支払合計取得
     */
    @Override
    @Transactional(readOnly = true)
    public int calculateTotalPaidAmount(UUID invoiceId) {
        log.info("Service start: calculate total paid amount. invoiceId={}", invoiceId);

        int totalPaidAmount = paymentRepository.sumPaidAmountByInvoiceId(invoiceId);

        log.info("Service success: calculate total paid amount. invoiceId={}, totalPaidAmount={}", invoiceId, totalPaidAmount);
        return totalPaidAmount;
    }

    /**
     * Entity → DTO変換
     */
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