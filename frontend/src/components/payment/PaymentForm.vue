<template>
  <BaseCard title="支払登録">
    <div class="grid grid-cols-1 gap-4 md:grid-cols-2">
      <!-- 請求ID -->
      <BaseInput :model-value="localForm.invoiceId" label="請求ID" :readonly="true" />

      <!-- 支払金額 -->
      <BaseInput
        v-model="localForm.paymentAmount"
        label="支払金額"
        type="number"
        placeholder="例: 3000"
        :required="true"
        :error-message="errors.paymentAmount"
      />

      <!-- 支払日時 -->
      <BaseInput
        v-model="localForm.paymentDateTime"
        label="支払日時"
        type="datetime-local"
        :required="true"
        :error-message="errors.paymentDateTime"
      />

      <!-- 支払方法 -->
      <BaseSelect
        v-model="localForm.paymentMethod"
        label="支払方法"
        placeholder="選択してください"
        :required="true"
        :options="paymentMethodOptions"
        :error-message="errors.paymentMethod"
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
  paymentAmount: string
  paymentDateTime: string
  paymentMethod: PaymentMethod | ''
  note: string
}

/**
 * 項目別エラーメッセージ型
 */
interface PaymentFormErrors {
  paymentAmount: string
  paymentDateTime: string
  paymentMethod: string
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
  paymentAmount: props.modelValue.paymentAmount ?? '',
  paymentDateTime: props.modelValue.paymentDateTime ?? '',
  paymentMethod: props.modelValue.paymentMethod ?? '',
  note: props.modelValue.note ?? '',
})

/**
 * 項目別バリデーションエラーを管理する
 */
const errors = reactive<PaymentFormErrors>({
  paymentAmount: '',
  paymentDateTime: '',
  paymentMethod: '',
  note: '',
})

/**
 * 親の modelValue 更新時にローカルフォームへ反映する
 */
watch(
  () => props.modelValue,
  (newValue) => {
    localForm.invoiceId = newValue.invoiceId ?? ''
    localForm.paymentAmount = newValue.paymentAmount ?? ''
    localForm.paymentDateTime = newValue.paymentDateTime ?? ''
    localForm.paymentMethod = newValue.paymentMethod ?? ''
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
      paymentAmount: newValue.paymentAmount,
      paymentDateTime: newValue.paymentDateTime,
      paymentMethod: newValue.paymentMethod,
      note: newValue.note,
    })
  },
  { deep: true },
)

/**
 * 項目別エラーを初期化する
 */
const clearErrors = () => {
  errors.paymentAmount = ''
  errors.paymentDateTime = ''
  errors.paymentMethod = ''
  errors.note = ''
}

/**
 * フロント側の簡易バリデーションを行う
 */
const validateForm = (): boolean => {
  clearErrors()

  let isValid = true

  if (!localForm.paymentAmount.trim()) {
    errors.paymentAmount = '支払金額は必須です。'
    isValid = false
  } else if (Number.isNaN(Number(localForm.paymentAmount))) {
    errors.paymentAmount = '支払金額は数値で入力してください。'
    isValid = false
  } else if (Number(localForm.paymentAmount) <= 0) {
    errors.paymentAmount = '支払金額は0より大きい値を入力してください。'
    isValid = false
  }

  if (!localForm.paymentDateTime.trim()) {
    errors.paymentDateTime = '支払日時は必須です。'
    isValid = false
  }

  if (!localForm.paymentMethod) {
    errors.paymentMethod = '支払方法は必須です。'
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
