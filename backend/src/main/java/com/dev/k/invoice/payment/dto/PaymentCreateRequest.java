package com.dev.k.invoice.payment.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.dev.k.invoice.payment.constant.PaymentMethod;

/**
 * 支払登録リクエストDTO（PaymentCreateRequest）
 *
 * 【役割】
 * - 支払登録APIで受け取る入力データを保持するDTO
 * - Controller → Serviceへデータを受け渡すためのオブジェクト
 *
 * 【利用箇所】
 * - PaymentController#create
 *
 * --------------------------------------------------
 * 【フィールド説明】
 *
 * ■ invoiceId
 * - 対象となる請求ID
 * - 必須項目
 *
 * ■ paidAmount
 * - 支払金額
 * - 必須項目
 * - 正の値のみ許可（0以下は不可）
 *
 * ■ paidAt
 * - 支払日時
 * - 必須項目
 *
 * ■ method
 * - 支払方法（Enum）
 * - 必須項目
 * - BANK_TRANSFER / CREDIT_CARD / CASH など
 *
 * ■ note
 * - 備考（任意）
 * - 最大500文字
 *
 * --------------------------------------------------
 * 【バリデーション】
 * - @NotNull：必須チェック
 * - @Positive：正の数値チェック
 * - @Size：文字数制限
 * --------------------------------------------------
 * 【補足】
 * - 業務ロジック（過払いチェックなど）はService側で実施
 */
public class PaymentCreateRequest {

	/**
     * 請求ID
     * - 支払対象となる請求の識別子
     */
	@NotNull(message = "Invoice ID is required.")
	private UUID invoiceId;

	/**
     * 支払金額
     * - 実際に支払われた金額
     * - 正の値のみ許可
     */
	@NotNull(message = "Paid amount is required.")
    @Positive(message = "Paid amount must be positive.")
    private Integer paidAmount;

	/**
     * 支払日時
     * - 支払が実行された日時
     */
	@NotNull(message = "Paid date-time is required.")
    private OffsetDateTime paidAt;

	/**
     * 支払方法
     * - BANK_TRANSFER / CREDIT_CARD / CASH など
     */
	@NotNull(message = "Payment method is required.")
    private PaymentMethod method;

	/**
     * 備考
     * - 任意入力
     * - メモや補足情報を格納
     */
	@Size(max = 500, message = "Note must be 500 characters or less.")
    private String note;

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}