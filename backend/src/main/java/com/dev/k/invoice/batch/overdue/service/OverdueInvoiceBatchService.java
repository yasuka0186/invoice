package com.dev.k.invoice.batch.overdue.service;

import com.dev.k.invoice.batch.overdue.dto.BatchResult;

/**
 * 期限切れ請求バッチサービス
 *
 * 【役割】
 * - 支払期限を過ぎた請求（Overdue）のステータスを更新するバッチ処理のインターフェース
 *
 * 【処理概要】
 * 1. 支払期限（due_date）が現在日時より過去の請求を抽出
 * 2. 未払い状態の請求を「期限切れ（OVERDUE）」に更新
 * 3. 更新結果を BatchResult として返却
 *
 * 【想定利用シーン】
 * - 定期バッチ（例：1日1回のスケジューラ実行）
 * - 手動実行（管理画面・APIからのトリガー）
 *
 * 【設計ポイント】
 * - インターフェース化により実装を分離（テスト・拡張しやすい）
 * - 戻り値で処理結果（件数）を返すことで運用監視に利用可能
 */
public interface OverdueInvoiceBatchService {

	/**
     * 期限切れ請求のステータス更新バッチを実行する
     *
     * @return BatchResult
     *         - targetCount：処理対象件数
     *         - updatedCount：更新成功件数
     */
    BatchResult execute();
}