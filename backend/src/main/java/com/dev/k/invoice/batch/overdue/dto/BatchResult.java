package com.dev.k.invoice.batch.overdue.dto;

/**
 * バッチ処理結果DTO
 *
 * 【役割】
 * - バッチ処理の実行結果を保持するためのデータクラス
 *
 * 【使用例】
 * - 期限切れ請求のステータス更新バッチ
 * - 定期実行ジョブの処理結果集計
 *
 * 【レスポンスイメージ】
 * {
 *   "targetCount": 10,
 *   "updatedCount": 8
 * }
 *
 * 【ポイント】
 * - targetCount：処理対象となった件数
 * - updatedCount：実際に更新された件数
 * - 差分により「更新不要」「エラー」などの分析が可能
 */
public class BatchResult {

	/**
     * 処理対象件数
     * - 条件に一致したデータの総数
     */
    private int targetCount;
    
    /**
     * 更新件数
     * - 実際に更新処理が成功した件数
     */
    private int updatedCount;

    /**
     * デフォルトコンストラクタ
     * - フレームワーク（Jackson等）で使用
     */
    public BatchResult() {
    }

    /**
     * コンストラクタ
     *
     * @param targetCount 処理対象件数
     * @param updatedCount 更新件数
     */
    public BatchResult(int targetCount, int updatedCount) {
        this.targetCount = targetCount;
        this.updatedCount = updatedCount;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }
}