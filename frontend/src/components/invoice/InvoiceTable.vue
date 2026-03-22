<template>
  <BaseCard title="請求一覧">
    <!-- ローディング表示 -->
    <div v-if="loading" class="py-6 text-sm text-slate-500">請求一覧を読み込み中です...</div>

    <!-- データなし表示 -->
    <div v-else-if="invoices.length === 0" class="py-6 text-sm text-slate-500">
      該当する請求データがありません。
    </div>

    <!-- 一覧テーブル -->
    <div v-else class="overflow-x-auto">
      <table class="min-w-full border-collapse">
        <thead>
          <tr class="border-b border-slate-200 bg-slate-50">
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">No</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">請求番号</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">件名</th>
            <th class="px-4 py-3 text-right text-sm font-semibold text-slate-700">金額</th>
            <th class="px-4 py-3 text-right text-sm font-semibold text-slate-700">残額</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">状態</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">支払期限</th>
            <th class="px-4 py-3 text-center text-sm font-semibold text-slate-700">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(invoice, index) in invoices"
            :key="invoice.invoiceId"
            class="border-b border-slate-100 hover:bg-slate-50"
          >
            <td class="px-4 py-3 text-sm text-slate-700">{{ index + 1 }}</td>
            <td class="px-4 py-3 text-sm text-slate-700">{{ invoice.invoiceNumber }}</td>
            <td class="px-4 py-3 text-sm text-slate-700">{{ invoice.title }}</td>
            <td class="px-4 py-3 text-right text-sm text-slate-700">
              {{ formatMoney(invoice.amount) }}
            </td>
            <td class="px-4 py-3 text-right text-sm text-slate-700">
              {{ formatMoney(invoice.balance) }}
            </td>
            <td class="px-4 py-3 text-sm text-slate-700">
              <span class="rounded-full bg-slate-100 px-2 py-1 text-xs font-medium">
                {{ invoice.status }}
              </span>
            </td>
            <td class="px-4 py-3 text-sm text-slate-700">
              {{ invoice.dueDate }}
            </td>
            <td class="px-4 py-3 text-center">
              <BaseButton
                label="詳細"
                variant="secondary"
                @click="handleDetail(invoice.invoiceId)"
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </BaseCard>
</template>

<script setup lang="ts">
import BaseButton from '@/components/common/BaseButton.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import type { Invoice } from '@/types/invoice'

interface Props {
  invoices: Invoice[]
  loading?: boolean
}

/**
 * 一覧テーブルの props 定義
 */
withDefaults(defineProps<Props>(), {
  loading: false,
})

/**
 * 詳細画面遷移イベントを親へ通知する
 */
const emit = defineEmits<{
  (e: 'detail', invoiceId: string): void
}>()

/**
 * 金額表示用フォーマット
 * undefined や null が来ても画面が落ちないようにする
 */
const formatMoney = (value?: number | null): string => {
  if (value == null) {
    return '-'
  }
  return value.toLocaleString()
}

/**
 * 詳細ボタン押下時の処理
 */
const handleDetail = (invoiceId: string) => {
  emit('detail', invoiceId)
}
</script>
