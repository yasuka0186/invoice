<template>
  <section class="space-y-6">
    <!-- 画面タイトル -->
    <div>
      <h1 class="text-2xl font-bold text-slate-800">請求詳細</h1>
      <p class="text-sm text-slate-500">請求詳細と支払履歴を確認する画面です。</p>
    </div>

    <!-- 戻るリンク -->
    <div>
      <button
        type="button"
        class="text-sm font-medium text-blue-600 hover:text-blue-800"
        @click="goBackToList"
      >
        ← 請求一覧へ戻る
      </button>
    </div>

    <!-- エラーメッセージ -->
    <ErrorMessage :message="displayErrorMessage" />

    <!-- 請求詳細カード -->
    <InvoiceDetailCard
      :invoice="selectedInvoice"
      :loading="invoiceLoading"
      @create-payment="goToPaymentCreate"
    />

    <!-- 支払履歴テーブル -->
    <PaymentHistoryTable :payments="payments" :loading="paymentLoading" />
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import ErrorMessage from '@/components/common/ErrorMessage.vue'
import InvoiceDetailCard from '@/components/invoice/InvoiceDetailCard.vue'
import PaymentHistoryTable from '@/components/invoice/PaymentHistoryTable.vue'
import { useInvoiceStore } from '@/stores/invoice'
import { usePaymentStore } from '@/stores/payment'

interface Props {
  invoiceId: string
}

/**
 * ルートパラメータから請求IDを受け取る
 */
const props = defineProps<Props>()

/**
 * ルーターを取得
 */
const router = useRouter()

/**
 * Store を取得
 */
const invoiceStore = useInvoiceStore()
const paymentStore = usePaymentStore()

/**
 * Store の state をリアクティブ参照として取得
 */
const {
  selectedInvoice,
  isLoading: invoiceLoading,
  errorMessage: invoiceErrorMessage,
} = storeToRefs(invoiceStore)

const {
  payments,
  isLoading: paymentLoading,
  errorMessage: paymentErrorMessage,
} = storeToRefs(paymentStore)

/**
 * 請求詳細取得エラーと支払履歴取得エラーをまとめて表示する
 */
const displayErrorMessage = computed(() => {
  return invoiceErrorMessage.value || paymentErrorMessage.value || ''
})

/**
 * 画面初期表示時に請求詳細と支払履歴を取得する
 */
onMounted(async () => {
  await Promise.all([
    invoiceStore.loadInvoiceById(props.invoiceId),
    paymentStore.loadPaymentsByInvoiceId(props.invoiceId),
  ])
})

/**
 * 画面離脱時に前画面の情報が残らないよう state を初期化する
 */
onUnmounted(() => {
  invoiceStore.clearSelectedInvoice()
  paymentStore.clearPayments()
  invoiceStore.clearError()
  paymentStore.clearError()
})

/**
 * 請求一覧画面へ戻る
 */
const goBackToList = () => {
  router.push('/invoices')
}

/**
 * 支払登録画面へ遷移する
 * クエリパラメータに invoiceId を付与する
 */
const goToPaymentCreate = () => {
  router.push(`/payments/new?invoiceId=${props.invoiceId}`)
}
</script>
