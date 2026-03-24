package com.dev.k.invoice.common.exception;

/**
 * リソース未存在例外クラス
 *
 * 【役割】
 * - 指定されたリソース（データ）が存在しない場合に使用する例外
 *
 * 【想定される使用例】
 * - IDで検索したが該当データが存在しない
 * - 更新・削除対象のレコードが見つからない
 *
 * 【使い分け】
 * - ResourceNotFoundException → データが存在しない（404）
 * - BusinessException → 業務ルール違反（400）
 */
public class ResourceNotFoundException extends RuntimeException {

	/**
     * コンストラクタ
     *
     * @param message エラーメッセージ（例：〇〇が存在しません）
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}