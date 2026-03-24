package com.dev.k.invoice.payment.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.k.invoice.common.dto.ApiResponse;
import com.dev.k.invoice.payment.dto.PaymentCreateRequest;
import com.dev.k.invoice.payment.dto.PaymentResponse;
import com.dev.k.invoice.payment.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

/**
 * 支払APIコントローラー（PaymentController）
 *
 * 【役割】
 * - 支払に関するHTTPリクエストを受け付けるエントリポイント
 * - リクエストの受信 → Service呼び出し → レスポンス返却を担当
 *
 * 【ベースURL】
 * - /api/payments
 *
 * 【提供API】
 *
 * ■ 支払登録
 * - POST /api/payments
 * - 請求に対する支払情報を登録する
 *
 * ■ 支払一覧取得（請求単位）
 * - GET /api/payments/invoice/{invoiceId}
 * - 指定した請求IDに紐づく支払履歴を取得する
 *
 * --------------------------------------------------
 * 【処理フロー】
 * Controller
 *   → Service（業務ロジック）
 *     → Repository（DBアクセス）
 *       → Entity
 *   → DTO変換
 * → ApiResponseで返却
 * --------------------------------------------------
 * 【ログ設計】
 * - API開始ログ（入力値）
 * - API成功ログ（結果・件数）
 *
 * --------------------------------------------------
 * 【補足】
 * - 支払登録時はService側で以下を実施
 *   ・請求存在チェック
 *   ・過払いチェック
 *   ・ステータス更新（必要に応じて）
 */
@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 支払登録API
     *
     * @param request 支払登録リクエスト
     * @return 登録された支払情報
     */
    @PostMapping
    public ApiResponse<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest request) {
        log.info("API start: create payment. invoiceId={}, paidAmount={}", request.getInvoiceId(), request.getPaidAmount());

        PaymentResponse response = paymentService.create(request);

        log.info("API success: create payment. paymentId={}, invoiceId={}", response.getPaymentId(), response.getInvoiceId());
        return ApiResponse.success(response);
    }

    /**
     * 支払一覧取得API（請求ID単位）
     *
     * @param invoiceId 請求ID
     * @return 支払一覧
     */
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<PaymentResponse>> findByInvoiceId(@PathVariable UUID invoiceId) {
        log.info("API start: find payments by invoiceId. invoiceId={}", invoiceId);

        List<PaymentResponse> response = paymentService.findByInvoiceId(invoiceId);

        log.info("API success: find payments by invoiceId. invoiceId={}, count={}", invoiceId, response.size());
        return ApiResponse.success(response);
    }
}