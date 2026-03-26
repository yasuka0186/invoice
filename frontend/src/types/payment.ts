/**
 * 支払方法の型定義
 */
export type PaymentMethod = 'BANK_TRANSFER' | 'CREDIT_CARD' | 'CASH'

/**
 * 支払情報の型定義
 */
export interface Payment {
  paymentId: string
  invoiceId: string
  paidAmount: number
  paidAt: string
  method: PaymentMethod
  note?: string | null
}

/**
 * 支払登録リクエストの型定義
 */
export interface CreatePaymentRequest {
  invoiceId: string
  paidAmount: number
  paidAt: string
  method: PaymentMethod
  note?: string
}
