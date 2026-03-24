package com.dev.k.invoice.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * 顧客更新リクエストDTO
 *
 * 【役割】
 * - 顧客更新API（PUT /api/customers/{customerId}）で受け取るリクエストデータを保持する
 * - 更新対象項目のみを受け取るためのDTO
 *
 * 【バリデーション内容】
 * - name     ：最大255文字
 * - email    ：メール形式、最大255文字
 * - isActive ：有効/無効フラグ（true/false）
 *
 * 【注意点】
 * - Service層で「nullチェック」を行い、更新対象項目のみ反映する必要がある
 * - 全項目を必須にしたい場合は @NotBlank 等を追加する
 */
public class CustomerUpdateRequest {

	/**
     * 顧客名（任意）
     */
	@Size(max = 255, message = "Customer name must be 255 characters or less.")
    private String name;

	/**
     * メールアドレス（任意）
     */
	@Email(message = "Email must be a valid email address.")
    @Size(max = 255, message = "Email must be 255 characters or less.")
    private String email;

	/**
     * 有効フラグ（任意）
     * - true  ：有効
     * - false ：無効
     */
    private Boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}