package com.dev.k.invoice.payment.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.dev.k.invoice.payment.constant.PaymentMethod;

/**
 * 支払レスポンスDTO（PaymentResponse）
 *
 * 【役割】
 * - 支払情報をAPIレスポンスとして返却するDTO
 * - Service → Controller → フロントへデータを受け渡す
 *
 * 【利用箇所】
 * - PaymentController
 * - 支払一覧画面 / 支払履歴表示
 *
 * 【補足】
 * - Entityをそのまま返さずDTOに変換することで
 *   セキュリティ・保守性・柔軟性を確保する
 */
public class PaymentResponse {

	/**
     * 支払ID
     * - 支払レコードの一意識別子
     */
    private UUID paymentId;
    
    /**
     * 請求ID
     * - この支払が紐づく請求のID
     */
    private UUID invoiceId;
    
    /**
     * 支払金額
     * - 実際に支払われた金額
     */
    private Integer paidAmount;
    
    /**
     * 支払日時
     * - 支払が実行された日時
     */
    private OffsetDateTime paidAt;
    
    /**
     * 支払方法
     * - BANK_TRANSFER / CREDIT_CARD / CASH など
     */
    private PaymentMethod method;
    
    /**
     * 備考
     * - 任意のメモ情報
     */
    private String note;

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

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