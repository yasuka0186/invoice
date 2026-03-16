import axios from 'axios'
import type { ApiErrorResponse, AppError, FieldError } from '@/types/api'

/**
 * 項目単位エラーを画面表示用文字列へ変換する
 * 例:
 * - customerId: 顧客IDは必須です。
 * - amount: 金額は0より大きい値を指定してください。
 */
const formatFieldErrors = (fieldErrors?: FieldError[]): string | null => {
  if (!fieldErrors || fieldErrors.length === 0) {
    return null
  }

  return fieldErrors.map((fieldError) => `${fieldError.field}: ${fieldError.message}`).join('\n')
}

/**
 * AxiosError または一般例外を、画面表示用の AppError に変換する
 * 画面側は message だけ見ればよいように統一する
 */
export const toAppError = (error: unknown): AppError => {
  /**
   * Axios由来のエラー
   */
  if (axios.isAxiosError(error)) {
    /**
     * レスポンスあり = サーバーから何らかの応答が返っている
     */
    if (error.response) {
      const data = error.response.data as ApiErrorResponse | undefined
      const fieldErrorMessage = formatFieldErrors(data?.fieldErrors)

      return {
        status: error.response.status,
        message:
          fieldErrorMessage ||
          data?.message ||
          `リクエストに失敗しました。（status: ${error.response.status}）`,
        fieldErrors: data?.fieldErrors ?? [],
      }
    }

    /**
     * タイムアウト
     */
    if (error.code === 'ECONNABORTED') {
      return {
        message: 'タイムアウトが発生しました。しばらくしてから再度お試しください。',
      }
    }

    /**
     * レスポンスなし = ネットワークエラーやサーバーダウンなど
     */
    return {
      message: 'サーバーへ接続できませんでした。起動状態や通信設定を確認してください。',
    }
  }

  /**
   * Error オブジェクト
   */
  if (error instanceof Error) {
    return {
      message: error.message || '予期しないエラーが発生しました。',
    }
  }

  /**
   * それ以外の未知のエラー
   */
  return {
    message: '予期しないエラーが発生しました。',
  }
}
