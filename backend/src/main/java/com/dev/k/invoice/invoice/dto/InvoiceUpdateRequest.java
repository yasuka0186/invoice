package com.dev.k.invoice.invoice.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * 請求更新リクエストDTO
 *
 * 【役割】
 * - 請求更新API（PUT /api/invoices/{invoiceId}）で受け取る入力データを表現するクラス
 * - 更新可能な項目のみを定義し、部分更新（Partial Update）を想定
 *
 * 【更新対象項目】
 * - title：請求タイトル（255文字以内）
 * - amount：請求金額（正の数）
 * - dueDate：支払期限日
 *
 * 【利用フロー】
 * Controller → Service → Entityへ反映 → DB更新
 *
 * 【補足】
 * - nullの項目は更新しない（Service側で制御）
 * - ステータス変更など業務ロジックに関わる項目は本DTOでは扱わない
 */
public class InvoiceUpdateRequest {

	/**
     * 請求タイトル（255文字以内）
     */
	@Size(max = 255, message = "Title must be 255 characters or less.")
	private String title;

	/**
     * 請求金額（正の数）
     */
	@Positive(message = "Amount must be positive.")
    private Integer amount;

	/**
     * 支払期限日
     */
    private LocalDate dueDate;

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