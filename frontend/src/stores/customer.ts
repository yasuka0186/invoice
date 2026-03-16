import { defineStore } from 'pinia'
import { fetchCustomers } from '@/api/customerApi'
import { toAppError } from '@/utils/error'
import type { Customer } from '@/types/customer'

/**
 * 顧客一覧を管理する Pinia Store
 * - 顧客一覧データ
 * - ローディング状態
 * - エラーメッセージ
 * を保持する
 */
export const useCustomerStore = defineStore('customer', {
  state: () => ({
    customers: [] as Customer[],
    isLoading: false,
    errorMessage: null as string | null,
  }),

  getters: {
    /**
     * 有効な顧客のみを返す
     */
    activeCustomers: (state): Customer[] => {
      return state.customers.filter((customer) => customer.active)
    },

    /**
     * 顧客件数を返す
     */
    customerCount: (state): number => {
      return state.customers.length
    },
  },

  actions: {
    /**
     * 顧客一覧を取得する
     */
    async loadCustomers() {
      this.isLoading = true
      this.errorMessage = null

      try {
        const customers = await fetchCustomers()
        this.customers = customers
      } catch (error) {
        const appError = toAppError(error)
        console.error('顧客一覧取得失敗:', error)
        this.errorMessage = appError.message
      } finally {
        this.isLoading = false
      }
    },

    /**
     * 顧客一覧をクリアする
     */
    clearCustomers() {
      this.customers = []
    },

    /**
     * エラーメッセージをクリアする
     */
    clearError() {
      this.errorMessage = null
    },
  },
})
