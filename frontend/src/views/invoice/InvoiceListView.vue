<template>
  <section class="space-y-4">
    <div>
      <h1 class="text-2xl font-bold text-slate-800">請求一覧</h1>
      <p class="text-sm text-slate-500">請求の検索・一覧表示を行う画面です。</p>
    </div>

    <div class="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
      <p class="text-slate-600">ここに請求検索フォームと請求一覧テーブルを実装します。</p>
    </div>

    <!-- エラーメッセージ表示 -->
    <ErrorMessage :message="errorMessage" />

    <!-- 検索フォーム -->
    <InvoiceSearchForm
      :model-value="searchForm"
      @update:model-value="handleSearchFormUpdate"
      @search="handleSearch"
      @clear="handleClear"
      @create="goToCreate"
    />

    <!-- 一覧テーブル -->
    <InvoiceTable :invoices="invoices" :loading="isLoading" @detail="goToDetail" />
  </section>
</template>

<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import ErrorMessage from '@/components/common/ErrorMessage.vue'
import InvoiceSearchForm from '@/components/invoice/InvoiceSearchForm.vue'
import InvoiceTable from '@/components/invoice/InvoiceTable.vue'
import { useInvoiceStore } from '@/stores/invoice'
import type { InvoiceSearchParams } from '@/types/invoice'

/**
 * ルーターを取得
 * - 新規登録画面、詳細画面への遷移に使用する
 */
const router = useRouter()

/**
 * 請求Storeを取得
 */
const invoiceStore = useInvoiceStore()

/**
 * Storeのstateをリアクティブ参照として取得
 */
const { invoices, isLoading, errorMessage } = storeToRefs(invoiceStore)

/**
 * 検索フォームのローカル状態
 * フォーム入力は local state とする設計方針
 */
const searchForm = reactive<InvoiceSearchParams>({
  customerId: '',
  status: '',
  dueDateFrom: '',
  dueDateTo: '',
})

/**
 * 子コンポーネントから受け取った検索条件で親の状態を更新する
 */
const handleSearchFormUpdate = (value: InvoiceSearchParams) => {
  searchForm.customerId = value.customerId ?? ''
  searchForm.status = value.status ?? ''
  searchForm.dueDateFrom = value.dueDateFrom ?? ''
  searchForm.dueDateTo = value.dueDateTo ?? ''
}

/**
 * 画面初期表示時に請求一覧を取得する
 */
onMounted(async () => {
  await invoiceStore.loadInvoices({})
})

/**
 * 検索ボタン押下時の処理
 * 現在のフォーム入力値で請求一覧を再取得する
 */
const handleSearch = async () => {
  await invoiceStore.loadInvoices({
    customerId: searchForm.customerId || '',
    status: searchForm.status || '',
    dueDateFrom: searchForm.dueDateFrom || '',
    dueDateTo: searchForm.dueDateTo || '',
  })
}

/**
 * クリアボタン押下時の処理
 * フォームを初期化し、全件検索を再実行する
 */
const handleClear = async () => {
  searchForm.customerId = ''
  searchForm.status = ''
  searchForm.dueDateFrom = ''
  searchForm.dueDateTo = ''

  await invoiceStore.loadInvoices({})
}

/**
 * 新規請求登録画面へ遷移する
 */
const goToCreate = () => {
  router.push('/invoices/new')
}

/**
 * 請求詳細画面へ遷移する
 */
const goToDetail = (invoiceId: string) => {
  router.push(`/invoices/${invoiceId}`)
}
</script>
