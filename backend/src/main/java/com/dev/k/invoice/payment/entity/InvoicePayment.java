package com.dev.k.invoice.payment.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.dev.k.invoice.common.entity.BaseEntity;
import com.dev.k.invoice.payment.constant.PaymentMethod;

/**
 * 支払エンティティ（InvoicePayment）
 *
 * 【役割】
 * - 請求に対する支払情報を管理するエンティティ
 * - DBテーブル「invoice_payments」とマッピングされる
 *
 * 【テーブル名】
 * - invoice_payments
 *
 * 【継承】
 * - BaseEntity
 *   → created_at / updated_at / version を共通管理
 *
 * --------------------------------------------------
 * 【フィールド説明】
 *
 * ■ paymentId
 * - 支払ID（主キー）
 * - UUIDで一意に識別
 *
 * ■ invoiceId
 * - 紐づく請求ID
 * - 外部キー（論理的リレーション）
 *
 * ■ paidAmount
 * - 支払金額
 *
 * ■ paidAt
 * - 支払日時
 *
 * ■ method
 * - 支払方法（Enum）
 * - DBには文字列として保存（EnumType.STRING）
 *
 * ■ note
 * - 備考（任意）
 * - 最大500文字
 * 
 * 【業務上の位置づけ】
 * - 支払登録時に作成される
 * - 請求の残額計算に使用される
 *
 * --------------------------------------------------
 * 【注意点】
 * - invoiceIdは外部キー制約をDB側で付与するのが望ましい
 * - 合計支払額はService層で集計する
 */
@Entity
@Table(name = "invoice_payments")
public class InvoicePayment extends BaseEntity {

	/**
     * 支払ID（主キー）
     */
    @Id
    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    /**
     * 請求ID（紐づく請求）
     */
    @Column(name = "invoice_id", nullable = false)
    private UUID invoiceId;

    /**
     * 支払金額
     */
    @Column(name = "paid_amount", nullable = false)
    private Integer paidAmount;

    /**
     * 支払日時
     */
    @Column(name = "paid_at", nullable = false)
    private OffsetDateTime paidAt;

    /**
     * 支払方法（Enum）
     * - DBには文字列として保存
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    /**
     * 備考（任意）
     */
    @Column(name = "note", length = 500)
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