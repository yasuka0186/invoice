package com.dev.k.invoice.payment.service;

import java.util.List;
import java.util.UUID;

import com.dev.k.invoice.payment.dto.PaymentCreateRequest;
import com.dev.k.invoice.payment.dto.PaymentResponse;

/**
 * 支払サービスインターフェース（PaymentService）
 *
 * 【役割】
 * - 支払に関する業務ロジックを定義するサービス層
 * - ControllerとRepositoryの中間に位置し、ビジネスルールを管理する
 *
 * 【提供機能】
 *
 * ■ create
 * - 支払を新規登録する
 * - 請求存在チェック、過払いチェックなどの業務ロジックを実行
 *
 * ■ findByInvoiceId
 * - 指定した請求に紐づく支払一覧を取得する
 *
 * ■ calculateTotalPaidAmount
 * - 指定した請求の支払合計金額を算出する
 *
 * --------------------------------------------------
 * 【利用フロー】
 * Controller → Service → Repository → Entity → DTO変換 → Controller → フロント
 *
 * --------------------------------------------------
 * 【補足】
 * - 支払登録時には以下のチェックをService側で行う想定
 *   ・請求が存在するか
 *   ・支払金額が請求金額を超えていないか（過払い防止）
 *   ・支払後のステータス更新（PAID判定など）
 */
public interface PaymentService {

	/**
     * 支払新規登録
     *
     * @param request 支払登録リクエスト
     * @return 登録された支払情報
     */
    PaymentResponse create(PaymentCreateRequest request);

    /**
     * 請求IDで支払一覧を取得
     *
     * @param invoiceId 請求ID
     * @return 支払一覧
     */
    List<PaymentResponse> findByInvoiceId(UUID invoiceId);

    /**
     * 支払合計金額を計算
     *
     * @param invoiceId 請求ID
     * @return 支払合計金額
     */
    int calculateTotalPaidAmount(UUID invoiceId);
}