<template>
  <BaseCard title="請求詳細">
    <!-- データ読み込み中 -->
    <div v-if="loading" class="py-6 text-sm text-slate-500">請求詳細を読み込み中です...</div>

    <!-- データなし -->
    <div v-else-if="!invoice" class="py-6 text-sm text-slate-500">請求データが見つかりません。</div>

    <!-- 請求詳細表示 -->
    <div v-else class="space-y-4">
      <div class="grid grid-cols-1 gap-4 md:grid-cols-2">
        <div>
          <p class="text-xs text-slate-500">請求番号</p>
          <p class="text-sm font-medium text-slate-800">{{ invoice.invoiceNo }}</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">顧客ID</p>
          <p class="break-all text-sm font-medium text-slate-800">{{ invoice.customerId }}</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">件名</p>
          <p class="text-sm font-medium text-slate-800">{{ invoice.title }}</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">状態</p>
          <p class="text-sm font-medium text-slate-800">{{ invoice.status }}</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">金額</p>
          <p class="text-sm font-medium text-slate-800">{{ formatMoney(invoice.amount) }}円</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">支払合計</p>
          <p class="text-sm font-medium text-slate-800">{{ formatMoney(invoice.paidAmount) }}円</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">残額</p>
          <p class="text-sm font-medium text-slate-800">{{ formatMoney(invoice.balance) }}円</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">支払期限</p>
          <p class="text-sm font-medium text-slate-800">{{ invoice.dueDate }}</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">発行日時</p>
          <p class="text-sm font-medium text-slate-800">{{ invoice.issuedAt || '-' }}</p>
        </div>

        <div>
          <p class="text-xs text-slate-500">完済日時</p>
          <p class="text-sm font-medium text-slate-800">{{ invoice.paidAt || '-' }}</p>
        </div>
      </div>

      <!-- 操作ボタン -->
      <div class="flex justify-end">
        <BaseButton label="支払登録" variant="primary" @click="handleCreatePayment" />
      </div>
    </div>
  </BaseCard>
</template>

<script setup lang="ts">
import BaseButton from '@/components/common/BaseButton.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import type { Invoice } from '@/types/invoice'

interface Props {
  invoice: Invoice | null
  loading?: boolean
}

/**
 * 請求詳細表示用コンポーネント
 * - 請求情報の表示
 * - 支払登録画面への遷移イベント通知
 * を担当する
 */
withDefaults(defineProps<Props>(), {
  loading: false,
})

const emit = defineEmits<{
  (e: 'create-payment'): void
}>()

/**
 * 金額を3桁カンマ付きで表示する
 */
const formatMoney = (value?: number | null) => {
  if (value == null) return '-'
  return value.toLocaleString()
}

/**
 * 支払登録ボタン押下時の処理
 */
const handleCreatePayment = () => {
  emit('create-payment')
}
</script>
