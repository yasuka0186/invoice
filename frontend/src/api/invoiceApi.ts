import axiosInstance from '@/api/axios'
import type { CreateInvoiceRequest, Invoice, InvoiceSearchParams } from '@/types/invoice'

/**
 * 請求一覧取得APIのレスポンス型
 * - MVPでは単純な配列レスポンスを想定
 * - Spring 側がページング形式の場合は後で PageResponse<Invoice> に変更可能
 */
export type GetInvoicesResponse = Invoice[]

/**
 * 請求検索条件をクエリパラメータ形式に変換する関数
 * - 未入力値は送信しない
 * - APIに不要な空文字を渡さないための前処理
 */
const buildInvoiceSearchParams = (params: InvoiceSearchParams) => {
  return {
    customerId: params.customerId || undefined,
    status: params.status || undefined,
    dueDateFrom: params.dueDateFrom || undefined,
    dueDateTo: params.dueDateTo || undefined,
  }
}

/**
 * 請求一覧を検索・取得するAPI
 * - 顧客ID、ステータス、支払期限From/To を条件に検索可能
 */
export const fetchInvoices = async (
  params: InvoiceSearchParams = {},
): Promise<GetInvoicesResponse> => {
  const response = await axiosInstance.get<GetInvoicesResponse>('/invoices', {
    params: buildInvoiceSearchParams(params),
  })
  return response.data
}

/**
 * 請求IDを指定して請求詳細を取得するAPI
 * - 請求詳細画面で使用する
 */
export const fetchInvoiceById = async (invoiceId: string): Promise<Invoice> => {
  const response = await axiosInstance.get<Invoice>(`/invoices/${invoiceId}`)
  return response.data
}

/**
 * 新しい請求を登録するAPI
 * - 請求登録画面で使用する
 */
export const createInvoice = async (request: CreateInvoiceRequest): Promise<Invoice> => {
  const response = await axiosInstance.post<Invoice>('/invoices', request)
  return response.data
}
