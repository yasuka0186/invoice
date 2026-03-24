package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.dev.k.invoice.invoice.constant.InvoiceStatus;

/**
 * 請求レスポンスDTO
 *
 * 【役割】
 * - 請求情報をクライアント（フロントエンド）へ返却するためのデータ構造
 * - Entityの情報をAPIレスポンス用に整形したもの
 *
 * 【主な項目】
 * - invoiceId：請求ID（UUID）
 * - invoiceNo：請求番号（ユニーク）
 * - customerId：顧客ID
 * - title：請求タイトル
 * - amount：請求金額（総額）
 * - totalPaidAmount：支払済金額（合計）
 * - remainingAmount：未払い残額
 * - dueDate：支払期限
 * - status：請求ステータス（DRAFT / ISSUED / OVERDUE / PAID / CANCELLED）
 * - issuedAt：請求発行日時
 * - paidAt：完済日時（未完済の場合はnull）
 *
 * 【利用フロー】
 * Entity → Serviceで変換 → Controller → フロントへ返却
 *
 * 【補足】
 * - totalPaidAmount / remainingAmount はDBカラムではなく、
 *   Service層で計算される派生データ
 */
public class InvoiceResponse {

	/**
     * 請求ID（UUID）
     */
    private UUID invoiceId;

    /**
     * 請求番号（ユニーク）
     */
    private String invoiceNo;
    
    /**
     * 顧客ID
     */
    private UUID customerId;
    
    /**
     * 請求タイトル
     */
    private String title;
    
    /**
     * 請求金額（総額）
     */
    private Integer amount;
    
    /**
     * 支払済金額（合計）
     */
    private Integer totalPaidAmount;
    
    /**
     * 未払い残額
     */
    private Integer remainingAmount;
    
    /**
     * 支払期限日
     */
    private LocalDate dueDate;
    
    /**
     * 請求ステータス
     */
    private InvoiceStatus status;
    
    /**
     * 請求発行日時
     */
    private OffsetDateTime issuedAt;
    
    /**
     * 完済日時（未完済の場合はnull）
     */
    private OffsetDateTime paidAt;

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(Integer totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public OffsetDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(OffsetDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }
}