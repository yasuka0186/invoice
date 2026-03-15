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
 * API共通のエラーレスポンス型
 * バックエンドの実装に応じて後で微調整可能
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
 * バリデーションエラーの項目単位情報
 */
export interface FieldError {
  field: string
  message: string
}
