import { defineStore } from 'pinia'
import { fetchPaymentsByInvoiceId } from '@/api/paymentApi'
import type { Payment } from '@/types/payment'

/**
 * 支払履歴を管理する Pinia Store
 * - 支払履歴一覧
 * - ローディング状態
 * - エラーメッセージ
 * を保持する
 */
export const usePaymentStore = defineStore('payment', {
  state: () => ({
    payments: [] as Payment[],
    isLoading: false,
    errorMessage: null as string | null,
  }),

  getters: {
    /**
     * 支払件数を返す
     */
    paymentCount: (state): number => {
      return state.payments.length
    },

    /**
     * 支払金額合計を返す
     * 請求詳細画面の補足情報などに使いやすい
     */
    totalPaymentAmount: (state): number => {
      return state.payments.reduce((sum, payment) => sum + payment.paymentAmount, 0)
    },
  },

  actions: {
    /**
     * 指定請求IDに紐づく支払履歴を取得する
     * - 請求詳細画面で使用する
     */
    async loadPaymentsByInvoiceId(invoiceId: string) {
      this.isLoading = true
      this.errorMessage = null

      try {
        const payments = await fetchPaymentsByInvoiceId(invoiceId)
        this.payments = payments
      } catch (error) {
        console.error('支払履歴取得失敗:', error)
        this.errorMessage = '支払履歴の取得に失敗しました。'
      } finally {
        this.isLoading = false
      }
    },

    /**
     * 支払履歴をクリアする
     */
    clearPayments() {
      this.payments = []
    },

    /**
     * エラーメッセージをクリアする
     */
    clearError() {
      this.errorMessage = null
    },
  },
})
