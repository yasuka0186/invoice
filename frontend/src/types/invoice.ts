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
