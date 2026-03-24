package com.dev.k.invoice.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 顧客作成リクエストDTO
 *
 * 【役割】
 * - 顧客登録API（POST /api/customers）で受け取るリクエストデータを保持する
 * - 入力値のバリデーションを定義する
 *
 * 【バリデーション内容】
 * - customerCode：必須、最大50文字
 * - name        ：必須、最大255文字
 * - email       ：任意、メール形式、最大255文字
 */
public class CustomerCreateRequest {

	/**
     * 顧客コード（業務上ユニークな識別子）
     */
	@NotBlank(message = "Customer code is required.")
    @Size(max = 50, message = "Customer code must be 50 characters or less.")
	private String customerCode;

	/**
     * 顧客名
     */
	@NotBlank(message = "Customer name is required.")
    @Size(max = 255, message = "Customer name must be 255 characters or less.")
	private String name;

	/**
     * メールアドレス（任意）
     */
	@Email(message = "Email must be a valid email address.")
    @Size(max = 255, message = "Email must be 255 characters or less.")
	private String email;

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode=customerCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email=email;
    }
}