import axiosInstance from '@/api/axios'
import type { Customer } from '@/types/customer'

/**
 * 顧客APIのレスポンス型
 * 顧客一覧取得時に使用する
 */
export type GetCustomersResponse = Customer[]

/**
 * 顧客一覧を取得するAPI
 * - MVPでは検索条件なしで全件または有効顧客一覧取得を想定
 */
export const fetchCustomers = async (): Promise<GetCustomersResponse> => {
  const response = await axiosInstance.get<GetCustomersResponse>('/customers')
  return response.data
}

/**
 * 顧客IDを指定して顧客詳細を取得するAPI
 * - 現時点では未使用でも、今後の拡張を見越して用意
 */
export const fetchCustomerById = async (customerId: string): Promise<Customer> => {
  const response = await axiosInstance.get<Customer>(`/customers/${customerId}`)
  return response.data
}
