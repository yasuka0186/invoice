import axiosInstance from '@/api/axios'
import type { ApiResponse } from '@/types/api'
import type {
  CreateInvoiceRequest,
  Invoice,
  InvoiceSearchParams,
  InvoiceSummary,
} from '@/types/invoice'

/**
 * 請求一覧検索APIのレスポンス型
 */
export type SearchInvoicesResponse = InvoiceSummary[]

/**
 * 請求検索条件をリクエストボディ形式に変換する関数
 */
const buildInvoiceSearchRequest = (params: InvoiceSearchParams) => {
  return {
    customerId: params.customerId || undefined,
    status: params.status || undefined,
    dueDateFrom: params.dueDateFrom || undefined,
    dueDateTo: params.dueDateTo || undefined,
  }
}

/**
 * 請求一覧を検索・取得するAPI
 */
export const fetchInvoices = async (
  params: InvoiceSearchParams = {},
): Promise<SearchInvoicesResponse> => {
  const response = await axiosInstance.post<ApiResponse<SearchInvoicesResponse>>(
    '/invoices/search',
    buildInvoiceSearchRequest(params),
  )
  return response.data.data
}

/**
 * 請求詳細取得API
 */
export const fetchInvoiceById = async (invoiceId: string): Promise<Invoice> => {
  const response = await axiosInstance.get<ApiResponse<Invoice>>(`/invoices/${invoiceId}`)
  return response.data.data
}

/**
 * 請求登録API
 */
export const createInvoice = async (request: CreateInvoiceRequest): Promise<Invoice> => {
  const response = await axiosInstance.post<ApiResponse<Invoice>>('/invoices', request)
  return response.data.data
}
