<template>
  <BaseCard title="請求登録">
    <div class="grid grid-cols-1 gap-4 md:grid-cols-2">
      <!-- 請求番号 -->
      <BaseInput
        v-model="localForm.invoiceNo"
        label="請求番号"
        placeholder="例: INV-202603-0001"
        :required="true"
        :error-message="errors.invoiceNo"
      />

      <!-- 顧客ID -->
      <BaseInput
        v-model="localForm.customerId"
        label="顧客ID"
        placeholder="顧客IDを入力"
        :required="true"
        :error-message="errors.customerId"
      />

      <!-- 件名 -->
      <div class="md:col-span-2">
        <BaseInput
          v-model="localForm.title"
          label="件名"
          placeholder="例: 3月分利用料"
          :required="true"
          :error-message="errors.title"
        />
      </div>

      <!-- 金額 -->
      <BaseInput
        v-model="localForm.amount"
        label="金額"
        type="number"
        placeholder="例: 10000"
        :required="true"
        :error-message="errors.amount"
      />

      <!-- 支払期限 -->
      <BaseInput
        v-model="localForm.dueDate"
        label="支払期限"
        type="date"
        :required="true"
        :error-message="errors.dueDate"
      />
    </div>

    <!-- フォーム全体エラー -->
    <div class="mt-4">
      <ErrorMessage :message="formErrorMessage" />
    </div>

    <!-- 操作ボタン -->
    <div class="mt-6 flex flex-wrap items-center gap-3">
      <BaseButton
        label="登録"
        variant="primary"
        :loading="loading"
        loading-text="登録中..."
        @click="handleSubmit"
      />
      <BaseButton
        label="キャンセル"
        variant="secondary"
        :disabled="loading"
        @click="handleCancel"
      />
    </div>
  </BaseCard>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import BaseInput from '@/components/common/BaseInput.vue'
import ErrorMessage from '@/components/common/ErrorMessage.vue'

/**
 * 請求登録フォームの入力値型
 * - amount は入力中は文字列で保持し、送信時に number へ変換する
 */
export interface InvoiceFormInput {
  invoiceNo: string
  customerId: string
  title: string
  amount: string
  dueDate: string
}

/**
 * 項目別エラーメッセージ型
 */
interface InvoiceFormErrors {
  invoiceNo: string
  customerId: string
  title: string
  amount: string
  dueDate: string
}

interface Props {
  modelValue: InvoiceFormInput
  loading?: boolean
  formErrorMessage?: string | null
}

/**
 * 親画面との連携用 props
 */
const props = withDefaults(defineProps<Props>(), {
  loading: false,
  formErrorMessage: '',
})

/**
 * 親画面へ通知するイベント
 * - update:modelValue: 親との双方向バインディング
 * - submit: 登録実行
 * - cancel: キャンセル押下
 */
const emit = defineEmits<{
  (e: 'update:modelValue', value: InvoiceFormInput): void
  (e: 'submit'): void
  (e: 'cancel'): void
}>()

/**
 * フォームのローカル状態
 * 親の modelValue を初期値として扱う
 */
const localForm = reactive<InvoiceFormInput>({
  invoiceNo: props.modelValue.invoiceNo ?? '',
  customerId: props.modelValue.customerId ?? '',
  title: props.modelValue.title ?? '',
  amount: props.modelValue.amount ?? '',
  dueDate: props.modelValue.dueDate ?? '',
})

/**
 * 項目別バリデーションエラーを管理する
 */
const errors = reactive<InvoiceFormErrors>({
  invoiceNo: '',
  customerId: '',
  title: '',
  amount: '',
  dueDate: '',
})

/**
 * 親の modelValue 更新時にローカルフォームへ反映する
 */
watch(
  () => props.modelValue,
  (newValue) => {
    localForm.invoiceNo = newValue.invoiceNo ?? ''
    localForm.customerId = newValue.customerId ?? ''
    localForm.title = newValue.title ?? ''
    localForm.amount = newValue.amount ?? ''
    localForm.dueDate = newValue.dueDate ?? ''
  },
  { deep: true },
)

/**
 * ローカルフォーム変更時に親へ最新値を通知する
 */
watch(
  localForm,
  (newValue) => {
    emit('update:modelValue', {
      invoiceNo: newValue.invoiceNo,
      customerId: newValue.customerId,
      title: newValue.title,
      amount: newValue.amount,
      dueDate: newValue.dueDate,
    })
  },
  { deep: true },
)

/**
 * 項目別エラーを初期化する
 */
const clearErrors = () => {
  errors.invoiceNo = ''
  errors.customerId = ''
  errors.title = ''
  errors.amount = ''
  errors.dueDate = ''
}

/**
 * フロント側の簡易バリデーションを行う
 * - 必須チェック
 * - 金額の数値/0以下チェック
 */
const validateForm = (): boolean => {
  clearErrors()

  let isValid = true

  if (!localForm.invoiceNo.trim()) {
    errors.invoiceNo = '請求番号は必須です。'
    isValid = false
  }

  if (!localForm.customerId.trim()) {
    errors.customerId = '顧客IDは必須です。'
    isValid = false
  }

  if (!localForm.title.trim()) {
    errors.title = '件名は必須です。'
    isValid = false
  }

  if (!localForm.amount.trim()) {
    errors.amount = '金額は必須です。'
    isValid = false
  } else if (Number.isNaN(Number(localForm.amount))) {
    errors.amount = '金額は数値で入力してください。'
    isValid = false
  } else if (Number(localForm.amount) <= 0) {
    errors.amount = '金額は0より大きい値を入力してください。'
    isValid = false
  }

  if (!localForm.dueDate.trim()) {
    errors.dueDate = '支払期限は必須です。'
    isValid = false
  }

  return isValid
}

/**
 * 登録ボタン押下時の処理
 * バリデーション成功時のみ親へ submit を通知する
 */
const handleSubmit = () => {
  const isValid = validateForm()

  if (!isValid) {
    return
  }

  emit('submit')
}

/**
 * キャンセルボタン押下時の処理
 */
const handleCancel = () => {
  emit('cancel')
}
</script>
