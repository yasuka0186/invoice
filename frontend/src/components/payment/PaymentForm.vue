<template>
  <BaseCard title="支払登録">
    <div class="grid grid-cols-1 gap-4 md:grid-cols-2">
      <!-- 請求ID -->
      <BaseInput :model-value="localForm.invoiceId" label="請求ID" :readonly="true" />

      <!-- 支払金額 -->
      <BaseInput
        v-model="localForm.paidAmount"
        label="支払金額"
        type="number"
        placeholder="例: 3000"
        :required="true"
        :error-message="errors.paidAmount"
      />

      <!-- 支払日時 -->
      <BaseInput
        v-model="localForm.paidAt"
        label="支払日時"
        type="datetime-local"
        :required="true"
        :error-message="errors.paidAt"
      />

      <!-- 支払方法 -->
      <BaseSelect
        v-model="localForm.method"
        label="支払方法"
        placeholder="選択してください"
        :required="true"
        :options="paymentMethodOptions"
        :error-message="errors.method"
      />

      <!-- 備考 -->
      <div class="md:col-span-2">
        <BaseInput
          v-model="localForm.note"
          label="備考"
          placeholder="備考を入力"
          :error-message="errors.note"
        />
      </div>
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
import BaseSelect from '@/components/common/BaseSelect.vue'
import ErrorMessage from '@/components/common/ErrorMessage.vue'
import type { PaymentMethod } from '@/types/payment'

/**
 * 支払登録フォームの入力値型
 * - paymentAmount は入力中は文字列で保持する
 */
export interface PaymentFormInput {
  invoiceId: string
  paidAmount: string
  paidAt: string
  method: PaymentMethod | ''
  note: string
}

/**
 * 項目別エラーメッセージ型
 */
interface PaymentFormErrors {
  paidAmount: string
  paidAt: string
  method: string
  note: string
}

interface Props {
  modelValue: PaymentFormInput
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
  (e: 'update:modelValue', value: PaymentFormInput): void
  (e: 'submit'): void
  (e: 'cancel'): void
}>()

/**
 * 支払方法選択肢
 */
const paymentMethodOptions: Array<{ label: string; value: PaymentMethod }> = [
  { label: 'BANK_TRANSFER', value: 'BANK_TRANSFER' },
  { label: 'CREDIT_CARD', value: 'CREDIT_CARD' },
  { label: 'CASH', value: 'CASH' },
]

/**
 * フォームのローカル状態
 */
const localForm = reactive<PaymentFormInput>({
  invoiceId: props.modelValue.invoiceId ?? '',
  paidAmount: props.modelValue.paidAmount ?? '',
  paidAt: props.modelValue.paidAt ?? '',
  method: props.modelValue.method ?? '',
  note: props.modelValue.note ?? '',
})

/**
 * 項目別バリデーションエラーを管理する
 */
const errors = reactive<PaymentFormErrors>({
  paidAmount: '',
  paidAt: '',
  method: '',
  note: '',
})

/**
 * 親の modelValue 更新時にローカルフォームへ反映する
 */
watch(
  () => props.modelValue,
  (newValue) => {
    localForm.invoiceId = newValue.invoiceId ?? ''
    localForm.paidAmount = newValue.paidAmount ?? ''
    localForm.paidAt = newValue.paidAt ?? ''
    localForm.method = newValue.method ?? ''
    localForm.note = newValue.note ?? ''
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
      invoiceId: newValue.invoiceId,
      paidAmount: newValue.paidAmount,
      paidAt: newValue.paidAt,
      method: newValue.method,
      note: newValue.note,
    })
  },
  { deep: true },
)

/**
 * 項目別エラーを初期化する
 */
const clearErrors = () => {
  errors.paidAmount = ''
  errors.paidAt = ''
  errors.method = ''
  errors.note = ''
}

/**
 * フロント側の簡易バリデーションを行う
 */
const validateForm = (): boolean => {
  clearErrors()

  let isValid = true

  if (!localForm.paidAmount.trim()) {
    errors.paidAmount = '支払金額は必須です。'
    isValid = false
  } else if (Number.isNaN(Number(localForm.paidAmount))) {
    errors.paidAmount = '支払金額は数値で入力してください。'
    isValid = false
  } else if (Number(localForm.paidAmount) <= 0) {
    errors.paidAmount = '支払金額は0より大きい値を入力してください。'
    isValid = false
  }

  if (!localForm.paidAt.trim()) {
    errors.paidAt = '支払日時は必須です。'
    isValid = false
  }

  if (!localForm.method) {
    errors.method = '支払方法は必須です。'
    isValid = false
  }

  return isValid
}

/**
 * 登録ボタン押下時の処理
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
