package com.dev.k.invoice.customer.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.dev.k.invoice.common.entity.BaseEntity;

/**
 * 顧客エンティティ
 *
 * 【役割】
 * - customersテーブルとマッピングされるJPAエンティティ
 * - 顧客情報を永続化（DB保存）するためのドメインモデル
 *
 * 【テーブル情報】
 * - テーブル名：customers
 *
 * 【主な用途】
 * - DBへのINSERT / UPDATE / SELECT
 * - Service層でのビジネスロジック処理対象
 *
 * 【注意点】
 * - Entityは外部公開しない（DTOを介してAPIとやり取りする）
 * - IDはUUIDで管理し、分散環境でも一意性を担保
 */
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

	/**
     * 顧客ID（主キー）
     * - UUIDで一意に識別
     */
    @Id
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    /**
     * 顧客コード（業務上の識別子）
     * - ユニーク制約あり
     * - 最大50文字
     */
    @Column(name = "customer_code", nullable = false, unique = true, length = 50)
    private String customerCode;

    /**
     * 顧客名
     * - 必須
     * - 最大255文字
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * メールアドレス
     * - 任意
     * - 最大255文字
     */
    @Column(name = "email", length = 255)
    private String email;

    /**
     * 有効フラグ
     * - true  ：有効
     * - false ：無効（論理削除的な扱い）
     */
    @Column(name = "is_active", nullable = false)
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