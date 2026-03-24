package com.dev.k.invoice.common.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;

/**
 * 全エンティティ共通の基底クラス
 *
 * 【役割】
 * - 作成日時 / 更新日時の自動管理
 * - 楽観ロック用のバージョン管理
 *
 * 【ポイント】
 * - @MappedSuperclass により、このクラスのフィールドは各テーブルに継承される
 * - 各エンティティで同じ監査項目を持たせるための共通化
 */
@MappedSuperclass
public abstract class BaseEntity {

	/**
     * 作成日時
     * - レコード作成時に自動設定
     * - 更新不可（updatable = false）
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * 更新日時
     * - レコード更新時に自動更新される
     */
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    /**
     * バージョン（楽観ロック用）
     * - 更新時に自動インクリメントされる
     * - 同時更新の競合検知に使用
     */
    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        // version が未設定の場合のみ初期値を設定        
        if (this.version == null) {
            this.version = 0;
        }
    }

    /**
     * エンティティ更新時に呼ばれる
     *
     * 【処理内容】
     * - updated_at を現在時刻に更新
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}