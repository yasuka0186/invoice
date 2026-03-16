<template>
  <BaseCard title="顧客一覧">
    <!-- データ読み込み中 -->
    <div v-if="loading" class="py-6 text-sm text-slate-500">顧客一覧を読み込み中です...</div>

    <!-- データなし -->
    <div v-else-if="customers.length === 0" class="py-6 text-sm text-slate-500">
      顧客データがありません。
    </div>

    <!-- 一覧テーブル -->
    <div v-else class="overflow-x-auto">
      <table class="min-w-full border-collapse">
        <thead>
          <tr class="border-b border-slate-200 bg-slate-50">
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">No</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">顧客コード</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">顧客名</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">メールアドレス</th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">有効状態</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(customer, index) in customers"
            :key="customer.customerId"
            class="border-b border-slate-100 hover:bg-slate-50"
          >
            <td class="px-4 py-3 text-sm text-slate-700">
              {{ index + 1 }}
            </td>

            <td class="px-4 py-3 text-sm text-slate-700">
              {{ customer.customerCode }}
            </td>

            <td class="px-4 py-3 text-sm text-slate-700">
              {{ customer.customerName }}
            </td>

            <td class="px-4 py-3 text-sm text-slate-700">
              {{ customer.email || '-' }}
            </td>

            <td class="px-4 py-3 text-sm text-slate-700">
              <span
                class="rounded-full px-2 py-1 text-xs font-medium"
                :class="
                  customer.active
                    ? 'bg-emerald-100 text-emerald-700'
                    : 'bg-slate-100 text-slate-600'
                "
              >
                {{ customer.active ? '有効' : '無効' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </BaseCard>
</template>

<script setup lang="ts">
import BaseCard from '@/components/common/BaseCard.vue'
import type { Customer } from '@/types/customer'

interface Props {
  customers: Customer[]
  loading?: boolean
}

/**
 * 顧客一覧テーブル表示用コンポーネント
 * - 顧客データの一覧表示
 * - ローディング表示
 * - 0件表示
 * を担当する
 */
withDefaults(defineProps<Props>(), {
  loading: false,
})
</script>
