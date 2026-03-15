import { defineStore } from 'pinia'
import { fetchCustomers } from '@/api/customerApi'
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
     * 今後、請求登録時の顧客選択などで使いやすいように用意
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
     * - 取得前にローディング開始
     * - 成功時に一覧を保存
     * - 失敗時にエラーメッセージを保存
     */
    async loadCustomers() {
      this.isLoading = true
      this.errorMessage = null

      try {
        const customers = await fetchCustomers()
        this.customers = customers
      } catch (error) {
        console.error('顧客一覧取得失敗:', error)
        this.errorMessage = '顧客一覧の取得に失敗しました。'
      } finally {
        this.isLoading = false
      }
    },

    /**
     * 顧客一覧をクリアする
     * - 画面離脱時や再初期化時に利用可能
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
