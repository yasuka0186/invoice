package com.dev.k.invoice.invoice.constant;

/**
 * 請求ステータス列挙型
 *
 * 【役割】
 * - 請求（Invoice）の状態を表す列挙型（Enum）
 * - DBおよびアプリケーション内で状態を一貫して管理する
 *
 * 【利用箇所】
 * - Invoiceエンティティ（@Enumerated(EnumType.STRING)）
 * - 業務ロジック（ステータス遷移制御）
 * - バッチ処理（期限切れ判定など）
 *
 * 【ステータス一覧】
 * - DRAFT      ：下書き（まだ発行されていない）
 * - ISSUED     ：発行済（支払待ち）
 * - OVERDUE    ：期限超過（支払期限を過ぎた未払い）
 * - PAID       ：完済（支払完了）
 * - CANCELLED  ：取消（無効化された請求）
 *
 * 【想定ステータス遷移】
 * - DRAFT → ISSUED → PAID
 * - ISSUED → OVERDUE → PAID
 * - 任意状態 → CANCELLED
 *
 * 【注意点】
 * - 不正なステータス遷移はService層で制御する
 */
public enum InvoiceStatus {

    DRAFT,  // 下書き
    ISSUED, // 発行済
    OVERDUE, // 期限超過
    PAID, // 完済
    CANCELLED // 取消

}