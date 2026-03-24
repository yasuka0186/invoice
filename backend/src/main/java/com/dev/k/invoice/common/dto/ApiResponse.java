package com.dev.k.invoice.common.dto;

/**
 * APIレスポンス共通クラス
 *
 * 【役割】
 * - 全APIのレスポンス形式を統一するためのラッパークラス
 *
 * 【レスポンス構造】
 * {
 *   "code": "SUCCESS",
 *   "message": "Request succeeded.",
 *   "data": {...}
 * }
 *
 * 【ポイント】
 * - ジェネリクス <T> により、任意のデータ型を格納可能
 * - フロント側では data 部分のみを取り出して利用する
 * - code / message により、成功・失敗の判定やエラーハンドリングが容易になる
 */

public class ApiResponse<T> {

	/**
     * レスポンスコード
     * - SUCCESS / ERROR などを想定
     */
    private String code;
    
    /**
     * レスポンスコード
     * - SUCCESS / ERROR などを想定
     */
    private String message;
    
    /**
     * レスポンスデータ本体
     * - 実際にクライアントへ返却するデータ
     */
    private T data;

    /**
     * デフォルトコンストラクタ
     * - Jacksonによるデシリアライズ時に使用
     */
    public ApiResponse() {
    }

    /**
     * コンストラクタ
     *
     * @param code レスポンスコード
     * @param message レスポンスメッセージ
     * @param data レスポンスデータ
     */
    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功レスポンス生成用のファクトリメソッド
     *
     * 【用途】
     * - 正常系のAPIレスポンスを簡潔に生成するために使用
     *
     * @param data レスポンスデータ
     * @return 成功レスポンス（code=SUCCESS）
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "Request succeeded.", data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}