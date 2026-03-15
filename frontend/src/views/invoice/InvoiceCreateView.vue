<template>
  <section class="space-y-6">
    <!-- 画面タイトル -->
    <div>
      <h1 class="text-2xl font-bold text-slate-800">請求登録</h1>
      <p class="text-sm text-slate-500">新しい請求を登録する画面です。</p>
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

    <!-- 請求登録フォーム -->
    <InvoiceForm
      v-model="form"
      :loading="isLoading"
      :form-error-message="errorMessage"
      @submit="handleSubmit"
      @cancel="goBackToList"
    />
  </section>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import InvoiceForm from '@/components/invoice/InvoiceForm.vue'
import { useInvoiceStore } from '@/stores/invoice'
import type { CreateInvoiceRequest } from '@/types/invoice'
import type { InvoiceFormInput } from '@/components/invoice/InvoiceForm.vue'

/**
 * ルーターを取得
 * - 請求一覧画面、請求詳細画面への遷移に使用する
 */
const router = useRouter()

/**
 * 請求Storeを取得
 */
const invoiceStore = useInvoiceStore()

/**
 * Store の状態をリアクティブ参照として取得する
 */
const { isLoading, errorMessage } = storeToRefs(invoiceStore)

/**
 * 請求登録フォームのローカル状態
 */
const form = reactive<InvoiceFormInput>({
  invoiceNumber: '',
  customerId: '',
  title: '',
  amount: '',
  dueDate: '',
})

/**
 * 請求一覧画面へ戻る
 */
const goBackToList = () => {
  router.push('/invoices')
}

/**
 * 登録処理
 * - フォームの文字列値をAPI送信用の型へ変換
 * - 登録成功後は請求詳細画面へ遷移
 */
const handleSubmit = async () => {
  try {
    const request: CreateInvoiceRequest = {
      invoiceNumber: form.invoiceNumber.trim(),
      customerId: form.customerId.trim(),
      title: form.title.trim(),
      amount: Number(form.amount),
      dueDate: form.dueDate,
    }

    const createdInvoice = await invoiceStore.registerInvoice(request)

    router.push(`/invoices/${createdInvoice.invoiceId}`)
  } catch (error) {
    console.error('請求登録画面で登録に失敗しました:', error)
  }
}
</script>
