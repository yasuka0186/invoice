/**
 * 請求ステータスの型定義
 */
export type InvoiceStatus = 'ISSUED' | 'PARTIALLY_PAID' | 'PAID' | 'OVERDUE'

/**
 * 請求一覧・詳細で使用する請求情報の型定義
 */
export interface Invoice {
  invoiceId: string
  invoiceNumber: string
  customerId: string
  title: string
  amount: number
  paidAmount: number
  balance: number
  status: InvoiceStatus
  dueDate: string
  issuedAt: string
  paidAt?: string | null
}

/**
 * 請求一覧表示用のサマリー型
 *
 * バックエンドの InvoiceSummaryResponse に対応
 * 一覧画面で使用する軽量データ
 *
 * ※ 詳細画面用の Invoice とは別
 */
export type InvoiceSummary = {
  /** 請求ID（UUID） */
  invoiceId: string

  /** 請求番号 */
  invoiceNo: string

  /** 顧客ID（UUID） */
  customerId: string

  /** 請求タイトル */
  title: string

  /** 請求金額 */
  amount: number

  /** 支払期限（ISO文字列） */
  dueDate: string

  /** ステータス（未払い / 支払済 / 期限切れなど） */
  status: string
}

/**
 * 請求検索条件の型定義
 */
export interface InvoiceSearchParams {
  customerId?: string
  status?: InvoiceStatus | ''
  dueDateFrom?: string
  dueDateTo?: string
}

/**
 * 請求登録リクエストの型定義
 */
export interface CreateInvoiceRequest {
  invoiceNumber: string
  customerId: string
  title: string
  amount: number
  dueDate: string
}
