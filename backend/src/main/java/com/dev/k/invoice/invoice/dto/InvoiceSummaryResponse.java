package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.dev.k.invoice.invoice.constant.InvoiceStatus;

/**
 * 請求一覧用レスポンスDTO（サマリー）
 *
 * 【役割】
 * - 請求一覧画面で表示するための軽量なデータ構造
 * - 詳細情報ではなく「一覧表示に必要な最小限の情報」を保持
 *
 * 【主な項目】
 * - invoiceId：請求ID
 * - invoiceNo：請求番号
 * - title：請求タイトル
 * - amount：請求金額
 * - remainingAmount：未払い残額
 * - dueDate：支払期限
 * - status：請求ステータス
 *
 * 【利用シーン】
 * - 請求一覧画面（テーブル表示）
 * - 検索結果一覧
 *
 * 【利用フロー】
 * Entity → QueryService → 本DTOへ変換 → Controller → フロントへ返却
 *
 * 【補足】
 * - remainingAmount はDBカラムではなく、Service層で計算される値
 * - 詳細が必要な場合は InvoiceResponse を使用する
 */
public class InvoiceSummaryResponse {

	/**
     * 請求ID
     */
    private UUID invoiceId;
    
    /**
     * 請求番号
     */
    private String invoiceNo;
    
    /**
     * 請求タイトル
     */
    private String title;
    
    /**
     * 請求金額
     */
    private Integer amount;
    
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
}