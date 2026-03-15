import { defineStore } from 'pinia'
import { createInvoice, fetchInvoiceById, fetchInvoices } from '@/api/invoiceApi'
import type { CreateInvoiceRequest, Invoice, InvoiceSearchParams } from '@/types/invoice'

/**
 * 請求情報を管理する Pinia Store
 * - 請求一覧
 * - 請求詳細
 * - 検索条件
 * - ローディング状態
 * - エラーメッセージ
 * を保持する
 */
export const useInvoiceStore = defineStore('invoice', {
  state: () => ({
    invoices: [] as Invoice[],
    selectedInvoice: null as Invoice | null,
    searchParams: {} as InvoiceSearchParams,
    isLoading: false,
    errorMessage: null as string | null,
  }),

  getters: {
    /**
     * 請求件数を返す
     */
    invoiceCount: (state): number => {
      return state.invoices.length
    },

    /**
     * 未完済の請求のみ返す
     * 一覧画面での絞り込みや件数表示に転用しやすい
     */
    unpaidInvoices: (state): Invoice[] => {
      return state.invoices.filter((invoice) => invoice.status !== 'PAID')
    },
  },

  actions: {
    /**
     * 請求一覧を検索条件付きで取得する
     * - 検索条件を state に保持
     * - 一覧を更新
     */
    async loadInvoices(params: InvoiceSearchParams = {}) {
      this.isLoading = true
      this.errorMessage = null
      this.searchParams = params

      try {
        const invoices = await fetchInvoices(params)
        this.invoices = invoices
      } catch (error) {
        console.error('請求一覧取得失敗:', error)
        this.errorMessage = '請求一覧の取得に失敗しました。'
      } finally {
        this.isLoading = false
      }
    },

    /**
     * 請求IDを指定して請求詳細を取得する
     * - 請求詳細画面で使用する
     */
    async loadInvoiceById(invoiceId: string) {
      this.isLoading = true
      this.errorMessage = null

      try {
        const invoice = await fetchInvoiceById(invoiceId)
        this.selectedInvoice = invoice
      } catch (error) {
        console.error('請求詳細取得失敗:', error)
        this.errorMessage = '請求詳細の取得に失敗しました。'
      } finally {
        this.isLoading = false
      }
    },

    /**
     * 新しい請求を登録する
     * - 登録成功時は登録結果を返す
     * - 登録失敗時は errorMessage を設定して例外を再送出する
     */
    async registerInvoice(request: CreateInvoiceRequest): Promise<Invoice> {
      this.isLoading = true
      this.errorMessage = null

      try {
        const createdInvoice = await createInvoice(request)
        return createdInvoice
      } catch (error) {
        console.error('請求登録失敗:', error)
        this.errorMessage = '請求登録に失敗しました。入力内容を確認してください。'
        throw error
      } finally {
        this.isLoading = false
      }
    },

    /**
     * 現在の検索条件で請求一覧を再取得する
     * - 再検索や登録後の再読込で使いやすい補助メソッド
     */
    async reloadInvoices() {
      await this.loadInvoices(this.searchParams)
    },

    /**
     * 請求一覧をクリアする
     */
    clearInvoices() {
      this.invoices = []
    },

    /**
     * 選択中の請求詳細をクリアする
     */
    clearSelectedInvoice() {
      this.selectedInvoice = null
    },

    /**
     * エラーメッセージをクリアする
     */
    clearError() {
      this.errorMessage = null
    },
  },
})
