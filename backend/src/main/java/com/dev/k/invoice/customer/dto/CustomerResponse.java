package com.dev.k.invoice.customer.dto;

import java.util.UUID;

/**
 * 顧客レスポンスDTO
 *
 * 【役割】
 * - 顧客APIのレスポンスとして返却するデータを保持する
 * - Entityの情報を外部公開用に整形したオブジェクト
 *
 * 【利用箇所】
 * - 顧客登録API
 * - 顧客更新API
 * - 顧客詳細取得API
 * - 顧客一覧取得API
 *
 * 【項目説明】
 * - customerId   ：顧客ID（UUID）
 * - customerCode ：顧客コード（業務上の識別子）
 * - name         ：顧客名
 * - email        ：メールアドレス
 * - isActive     ：有効フラグ
 * - createdAt    ：作成日時
 * - updatedAt    ：更新日時
 */
public class CustomerResponse {

	/**
     * 顧客ID（主キー）
     */
    private UUID customerId;
    
    /**
     * 顧客コード（業務上ユニーク）
     */
    private String customerCode;
    
    /**
     * 顧客名
     */
    private String name;
    
    /**
     * メールアドレス
     */
    private String email;
    
    /**
     * 有効フラグ
     */
    private Boolean isActive;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

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