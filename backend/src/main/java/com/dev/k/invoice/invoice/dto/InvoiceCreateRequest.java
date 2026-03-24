package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * 請求作成リクエストDTO
 *
 * 【役割】
 * - 請求登録API（POST /api/invoices）で受け取る入力データを表現するクラス
 * - Controller層でのバリデーションを担う
 *
 * 【バリデーション内容】
 * - invoiceNo：必須、50文字以内
 * - customerId：必須
 * - title：必須、255文字以内
 * - amount：必須、正の数
 * - dueDate：必須
 *
 * 【利用フロー】
 * Controller → Service → Entity変換 → DB保存
 */
public class InvoiceCreateRequest {

	/**
     * 請求番号（ユニーク）
     */
	@NotBlank(message = "Invoice number is required.")
    @Size(max = 50, message = "Invoice number must be 50 characters or less.")
    private String invoiceNo;

	/**
     * 顧客ID（UUID）
     */
	@NotNull(message = "Customer ID is required.")
    private UUID customerId;

	/**
     * 請求タイトル（内容）
     */
	@NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must be 255 characters or less.")
    private String title;

	/**
     * 請求金額
     */
	@NotNull(message = "Amount is required.")
    @Positive(message = "Amount must be positive.")
    private Integer amount;
 
	/**
     * 支払期限日
     */
	@NotNull(message = "Due date is required.")
    private LocalDate dueDate;

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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}