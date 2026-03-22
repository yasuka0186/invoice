import axiosInstance from '@/api/axios'
import type { CreatePaymentRequest, Payment } from '@/types/payment'
import type { ApiResponse } from '@/types/api'

/**
 * 支払一覧取得APIのレスポンス型
 * - 請求に紐づく支払履歴一覧を想定
 */
export type GetPaymentsResponse = Payment[]

/**
 * 請求IDを指定して支払履歴一覧を取得するAPI
 * - 請求詳細画面の支払履歴テーブルで使用する
 */
export const fetchPaymentsByInvoiceId = async (invoiceId: string): Promise<Payment[]> => {
  const response = await axiosInstance.get<ApiResponse<Payment[]>>(`/payments/invoice/${invoiceId}`)
  return response.data.data
}

/**
 * 支払IDを指定して支払詳細を取得するAPI
 * - 現時点では未使用でも、今後の拡張を見越して用意
 */
export const fetchPaymentById = async (paymentId: string): Promise<Payment> => {
  const response = await axiosInstance.get<Payment>(`/payments/${paymentId}`)
  return response.data
}

/**
 * 新しい支払を登録するAPI
 * - 支払登録画面で使用する
 */
export const createPayment = async (request: CreatePaymentRequest): Promise<Payment> => {
  const response = await axiosInstance.post<ApiResponse<Payment>>('/payments', request)
  return response.data.data
}
