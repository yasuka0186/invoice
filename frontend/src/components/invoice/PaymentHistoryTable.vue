<template>
  <BaseCard title="支払履歴">
    <!-- データ読み込み中 -->
    <div v-if="loading" class="py-6 text-sm text-slate-500">支払履歴を読み込み中です...</div>

    <!-- データなし -->
    <div v-else-if="payments.length === 0" class="py-6 text-sm text-slate-500">
      支払履歴はありません。
    </div>

    <!-- 一覧テーブル -->
    <div v-else class="overflow-x-auto">
      <table class="min-w-full border-collapse">
        <thead>
          <tr class="border-b border-slate-200 bg-slate-50">
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">No</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">支払日時</th>
            <th class="px-4 py-3 text-right text-sm font-semibold text-slate-700">金額</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">方法</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">備考</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(payment, index) in payments"
            :key="payment.paymentId"
            class="border-b border-slate-100 hover:bg-slate-50"
          >
            <td class="px-4 py-3 text-sm text-slate-700">{{ index + 1 }}</td>
            <td class="px-4 py-3 text-sm text-slate-700">{{ payment.paymentDateTime }}</td>
            <td class="px-4 py-3 text-right text-sm text-slate-700">
              {{ formatMoney(payment.paymentAmount) }}
            </td>
            <td class="px-4 py-3 text-sm text-slate-700">{{ payment.paymentMethod }}</td>
            <td class="px-4 py-3 text-sm text-slate-700">{{ payment.note || '-' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </BaseCard>
</template>

<script setup lang="ts">
import BaseCard from '@/components/common/BaseCard.vue'
import type { Payment } from '@/types/payment'

interface Props {
  payments: Payment[]
  loading?: boolean
}

/**
 * 支払履歴一覧表示用コンポーネント
 * - 支払履歴のテーブル表示を担当する
 */
withDefaults(defineProps<Props>(), {
  loading: false,
})

/**
 * 金額を3桁カンマ付きで表示する
 */
const formatMoney = (value: number): string => {
  return value.toLocaleString('ja-JP')
}
</script>
