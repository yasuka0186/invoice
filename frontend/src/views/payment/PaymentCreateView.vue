<template>
  <section class="space-y-6">
    <!-- 画面タイトル -->
    <div>
      <h1 class="text-2xl font-bold text-slate-800">支払登録</h1>
      <p class="text-sm text-slate-500">請求に対する支払を登録する画面です。</p>
    </div>

    <!-- 戻るリンク -->
    <div>
      <button
        type="button"
        class="text-sm font-medium text-blue-600 hover:text-blue-800"
        @click="goBackToDetail"
      >
        ← 請求詳細へ戻る
      </button>
    </div>

    <!-- 支払登録フォーム -->
    <PaymentForm
      :model-value="form"
      :loading="isLoading"
      :form-error-message="errorMessage"
      @update:model-value="handleFormUpdate"
      @submit="handleSubmit"
      @cancel="goBackToDetail"
    />
  </section>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'
import PaymentForm from '@/components/payment/PaymentForm.vue'
import { usePaymentStore } from '@/stores/payment'
import type { CreatePaymentRequest } from '@/types/payment'
import type { PaymentFormInput } from '@/components/payment/PaymentForm.vue'

/**
 * ルーターとルート情報を取得
 */
const router = useRouter()
const route = useRoute()

/**
 * 支払Storeを取得
 */
const paymentStore = usePaymentStore()

/**
 * Store の状態をリアクティブ参照として取得する
 */
const { isLoading, errorMessage } = storeToRefs(paymentStore)

/**
 * クエリパラメータから invoiceId を取得する
 */
const getInvoiceIdFromQuery = (): string => {
  const queryValue = route.query.invoiceId
  return typeof queryValue === 'string' ? queryValue : ''
}

/**
 * 支払登録フォームのローカル状態
 */
const form = reactive<PaymentFormInput>({
  invoiceId: getInvoiceIdFromQuery(),
  paymentAmount: '',
  paymentDateTime: '',
  paymentMethod: '',
  note: '',
})

/**
 * クエリ変更時にも invoiceId を同期する
 */
watch(
  () => route.query.invoiceId,
  () => {
    form.invoiceId = getInvoiceIdFromQuery()
  },
)

/**
 * 子コンポーネントから受け取った入力値で親のフォーム状態を更新する
 */
const handleFormUpdate = (value: PaymentFormInput) => {
  form.invoiceId = value.invoiceId
  form.paymentAmount = value.paymentAmount
  form.paymentDateTime = value.paymentDateTime
  form.paymentMethod = value.paymentMethod
  form.note = value.note
}

/**
 * 請求詳細画面へ戻る
 */
const goBackToDetail = () => {
  if (!form.invoiceId) {
    router.push('/invoices')
    return
  }

  router.push(`/invoices/${form.invoiceId}`)
}

/**
 * datetime-local の値をAPI送信用に整形する
 * 例: 2026-03-15T20:30 → 2026-03-15T20:30:00
 */
const normalizePaymentDateTime = (value: string): string => {
  if (!value) {
    return value
  }

  return value.length === 16 ? `${value}:00` : value
}

/**
 * 登録処理
 * - フォームの文字列値をAPI送信用の型へ変換
 * - 登録成功後は請求詳細画面へ戻る
 */
const handleSubmit = async () => {
  try {
    const request: CreatePaymentRequest = {
      invoiceId: form.invoiceId,
      paymentAmount: Number(form.paymentAmount),
      paymentDateTime: normalizePaymentDateTime(form.paymentDateTime),
      paymentMethod: form.paymentMethod,
      note: form.note.trim(),
    }

    await paymentStore.registerPayment(request)

    router.push(`/invoices/${form.invoiceId}`)
  } catch (error) {
    console.error('支払登録画面で登録に失敗しました:', error)
  }
}
</script>
