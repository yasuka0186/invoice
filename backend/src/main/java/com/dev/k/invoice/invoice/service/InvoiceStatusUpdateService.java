package com.dev.k.invoice.invoice.service;

import java.util.UUID;

/**
 * 請求ステータス更新サービスインターフェース
 *
 * 【役割】
 * - 請求の「ステータス変更」に特化したサービス
 * - 単純な更新ではなく、業務ルールに基づく状態遷移を管理する
 *
 * 【設計意図】
 * - InvoiceService（CRUD系）と責務を分離
 * - ステータス変更ロジックを専用サービスに集約することで可読性・保守性を向上
 *
 * 【対象ステータス】
 * - ISSUED → PAID（支払完了）
 * - ISSUED → OVERDUE（期限超過）
 *
 * 【主な業務ルール（想定）】
 * - 支払済の請求は再度変更できない
 * - キャンセル済の請求は変更不可
 * - 支払金額が請求額を満たしている必要がある（markAsPaid）
 * - 期限を過ぎていること（markAsOverdue）
 *
 * 【利用シーン】
 * - 支払登録後のステータス更新
 * - バッチ処理による期限超過更新
 *
 * --------------------------------------------------
 * 【提供メソッド】
 *
 * ■ markAsPaid
 * - 指定した請求を「支払済（PAID）」に変更する
 *
 * ■ markAsOverdue
 * - 指定した請求を「期限超過（OVERDUE）」に変更する
 *
 * --------------------------------------------------
 * 【利用フロー】
 * Controller / Batch → StatusUpdateService → Repository → Entity更新
 */
public interface InvoiceStatusUpdateService {

    /**
     * 請求を支払済に更新する
     *
     * @param invoiceId 請求ID
     */
    void markAsPaid(UUID invoiceId);

    /**
     * 請求を期限超過に更新する
     *
     * @param invoiceId 請求ID
     */
    void markAsOverdue(UUID invoiceId);
}