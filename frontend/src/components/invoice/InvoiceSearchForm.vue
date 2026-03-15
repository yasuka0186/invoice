<template>
  <BaseCard title="請求検索">
    <div class="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
      <!-- 顧客ID検索 -->
      <BaseInput v-model="localForm.customerId" label="顧客ID" placeholder="顧客IDを入力" />

      <!-- ステータス検索 -->
      <BaseSelect
        v-model="localForm.status"
        label="ステータス"
        placeholder="選択してください"
        :options="statusOptions"
      />

      <!-- 支払期限From -->
      <BaseInput v-model="localForm.dueDateFrom" label="支払期限From" type="date" />

      <!-- 支払期限To -->
      <BaseInput v-model="localForm.dueDateTo" label="支払期限To" type="date" />
    </div>

    <!-- 操作ボタン -->
    <div class="mt-6 flex flex-wrap items-center gap-3">
      <BaseButton label="検索" variant="primary" @click="handleSearch" />
      <BaseButton label="クリア" variant="secondary" @click="handleClear" />
      <BaseButton label="新規請求登録" variant="primary" @click="handleCreate" />
    </div>
  </BaseCard>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import BaseInput from '@/components/common/BaseInput.vue'
import BaseSelect from '@/components/common/BaseSelect.vue'
import type { InvoiceSearchParams, InvoiceStatus } from '@/types/invoice'

interface Props {
  modelValue: InvoiceSearchParams
}

/**
 * 請求検索フォームから親へ通知するイベント定義
 * - update:modelValue: 親との双方向バインディング用
 * - search: 検索実行
 * - clear: クリア実行
 * - create: 新規登録画面遷移
 */
const props = defineProps<Props>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: InvoiceSearchParams): void
  (e: 'search'): void
  (e: 'clear'): void
  (e: 'create'): void
}>()

/**
 * ステータス選択肢
 */
const statusOptions: Array<{ label: string; value: InvoiceStatus }> = [
  { label: 'ISSUED', value: 'ISSUED' },
  { label: 'PARTIALLY_PAID', value: 'PARTIALLY_PAID' },
  { label: 'PAID', value: 'PAID' },
  { label: 'OVERDUE', value: 'OVERDUE' },
]

/**
 * フォームのローカル状態
 * 親から受け取った modelValue を初期値として保持する
 */
const localForm = reactive<InvoiceSearchParams>({
  customerId: props.modelValue.customerId ?? '',
  status: props.modelValue.status ?? '',
  dueDateFrom: props.modelValue.dueDateFrom ?? '',
  dueDateTo: props.modelValue.dueDateTo ?? '',
})

/**
 * 親から modelValue が更新された場合にローカルフォームへ反映する
 * クリア処理や初期化との整合性を保つために使用する
 */
watch(
  () => props.modelValue,
  (newValue) => {
    localForm.customerId = newValue.customerId ?? ''
    localForm.status = newValue.status ?? ''
    localForm.dueDateFrom = newValue.dueDateFrom ?? ''
    localForm.dueDateTo = newValue.dueDateTo ?? ''
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
      customerId: newValue.customerId ?? '',
      status: newValue.status ?? '',
      dueDateFrom: newValue.dueDateFrom ?? '',
      dueDateTo: newValue.dueDateTo ?? '',
    })
  },
  { deep: true },
)

/**
 * 検索ボタン押下時の処理
 */
const handleSearch = () => {
  emit('search')
}

/**
 * クリアボタン押下時の処理
 * ローカルフォームを初期値へ戻した上で親へ通知する
 */
const handleClear = () => {
  localForm.customerId = ''
  localForm.status = ''
  localForm.dueDateFrom = ''
  localForm.dueDateTo = ''

  emit('update:modelValue', {
    customerId: '',
    status: '',
    dueDateFrom: '',
    dueDateTo: '',
  })

  emit('clear')
}

/**
 * 新規請求登録ボタン押下時の処理
 */
const handleCreate = () => {
  emit('create')
}
</script>
