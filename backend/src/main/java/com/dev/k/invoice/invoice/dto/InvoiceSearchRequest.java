package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.dev.k.invoice.invoice.constant.InvoiceStatus;

/**
 * 請求検索リクエストDTO
 *
 * 【役割】
 * - 請求検索API（POST /api/invoices/search）で受け取る検索条件を表現するクラス
 * - 複数条件による絞り込み検索に使用する
 *
 * 【検索条件】
 * - customerId：顧客IDで絞り込み
 * - status：請求ステータスで絞り込み
 * - dueDateFrom：支払期限（開始日）
 * - dueDateTo：支払期限（終了日）
 *
 * 【利用フロー】
 * Controller → Service（QueryService） → Repository
 *
 * 【補足】
 * - 条件はすべてAND検索を想定
 * - dueDateFrom / dueDateTo は期間検索として利用
 */
public class InvoiceSearchRequest {

	/**
     * 顧客ID（指定された場合のみ絞り込み）
     */
    private UUID customerId;
    
    /**
     * 請求ステータス（指定された場合のみ絞り込み）
     */
    private InvoiceStatus status;
    
    /**
     * 支払期限（開始日）
     */
    private LocalDate dueDateFrom;
    
    /**
     * 支払期限（終了日）
     */
    private LocalDate dueDateTo;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public LocalDate getDueDateFrom() {
        return dueDateFrom;
    }

    public void setDueDateFrom(LocalDate dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }

    public LocalDate getDueDateTo() {
        return dueDateTo;
    }

    public void setDueDateTo(LocalDate dueDateTo) {
        this.dueDateTo = dueDateTo;
    }
}