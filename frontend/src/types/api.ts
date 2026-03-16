/**
 * API共通のページングレスポンス型
 */
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

/**
 * 項目単位のバリデーションエラー型
 */
export interface FieldError {
  field: string
  message: string
}

/**
 * バックエンドのAPIエラーレスポンス型
 * Spring Boot 側の例外レスポンスを想定する
 */
export interface ApiErrorResponse {
  timestamp?: string
  status?: number
  error?: string
  message?: string
  path?: string
  fieldErrors?: FieldError[]
}

/**
 * フロント側で統一的に扱うアプリケーションエラー型
 * - 画面表示用メッセージ
 * - HTTPステータス
 * - 項目エラー
 * を持つ
 */
export interface AppError {
  message: string
  status?: number
  fieldErrors?: FieldError[]
}
