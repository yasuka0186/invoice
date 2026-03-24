package com.dev.k.invoice.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dev.k.invoice.common.dto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * グローバル例外ハンドラクラス
 *
 * 【役割】
 * - アプリケーション全体で発生した例外を一元管理する
 * - 例外ごとに適切なHTTPステータスとレスポンス形式を返却する
 *
 * 【ポイント】
 * - @RestControllerAdvice により全Controllerに適用される
 * - 例外ごとに @ExceptionHandler でハンドリングを分岐
 * - ApiResponse 形式でレスポンスを統一
 * - ログ出力により障害調査を容易にする
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
     * 業務例外ハンドリング
     *
     * 【対象】
     * - BusinessException（業務ロジック上のエラー）
     *
     * 【例】
     * - 支払超過
     * - ステータス不正
     *
     * 【レスポンス】
     * - HTTP 400 (Bad Request)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("Business exception occurred. errorCode={}, message={}", e.getErrorCode(), e.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(
                e.getErrorCode().name(),
                e.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * リソース未存在例外ハンドリング
     *
     * 【対象】
     * - ResourceNotFoundException
     *
     * 【例】
     * - 指定IDのデータが存在しない
     *
     * 【レスポンス】
     * - HTTP 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(ResourceNotFoundException e) {
        log.warn("Resource not found exception occurred. message={}", e.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(
                "NOT_FOUND",
                e.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /**
     * バリデーションエラーハンドリング
     *
     * 【対象】
     * - @Valid による入力チェックエラー
     *
     * 【例】
     * - 必須項目未入力
     * - フォーマット不正
     *
     * 【処理】
     * - 最初のエラーメッセージのみ取得して返却
     *
     * 【レスポンス】
     * - HTTP 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error.");

        log.warn("Validation exception occurred. message={}", message);

        ApiResponse<Void> response = new ApiResponse<>(
                "VALIDATION_ERROR",
                message,
                null
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    /**
     * 想定外例外ハンドリング
     *
     * 【対象】
     * - 上記以外のすべての例外
     *
     * 【例】
     * - NullPointerException
     * - DB接続エラー
     *
     * 【ポイント】
     * - 詳細な例外内容はログに出力
     * - クライアントには汎用メッセージのみ返却（セキュリティ対策）
     *
     * 【レスポンス】
     * - HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected exception occurred.", e);

        ApiResponse<Void> response = new ApiResponse<>(
                "INTERNAL_ERROR",
                "Unexpected error occurred.",
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}