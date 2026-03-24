package com.dev.k.invoice.common.exception;

import com.dev.k.invoice.common.constant.ErrorCode;

/**
 * 業務例外クラス（Business Exception）
 *
 * 【役割】
 * - 業務ロジック上のエラーを表現するためのカスタム例外
 *
 * 【想定される使用例】
 * - データが存在しない（NOT_FOUND）
 * - 入力値が不正（VALIDATION_ERROR）
 * - 請求金額を超える支払い（OVER_PAYMENT）
 * - 不正なステータス遷移（INVALID_STATUS）
 *
 * 【ポイント】
 * - RuntimeException を継承しているため、throws宣言不要
 * - ErrorCode を保持することで、フロントへ一貫したエラー情報を返却可能
 * - GlobalExceptionHandler と組み合わせて使用する
 */
public class BusinessException extends RuntimeException {

	/**
     * エラーコード（業務エラー種別）
     */
    private final ErrorCode errorCode;

    /**
     * コンストラクタ（エラーメッセージはErrorCode名を使用）
     *
     * @param errorCode エラーコード
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    /**
     * コンストラクタ（任意メッセージ指定）
     *
     * @param errorCode エラーコード
     * @param message エラーメッセージ（詳細内容）
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * エラーコード取得
     *
     * @return エラーコード
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}