<template>
  <section class="space-y-6">
    <!-- 画面タイトル -->
    <div>
      <h1 class="text-2xl font-bold text-slate-800">顧客一覧</h1>
      <p class="text-sm text-slate-500">顧客情報の一覧を表示する画面です。</p>
    </div>

    <!-- エラーメッセージ表示 -->
    <ErrorMessage :message="errorMessage" />

    <!-- 顧客一覧テーブル -->
    <CustomerTable :customers="customers" :loading="isLoading" />
  </section>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { storeToRefs } from 'pinia'
import ErrorMessage from '@/components/common/ErrorMessage.vue'
import CustomerTable from '@/components/customer/CustomerTable.vue'
import { useCustomerStore } from '@/stores/customer'

/**
 * 顧客Storeを取得
 */
const customerStore = useCustomerStore()

/**
 * Store の state をリアクティブ参照として取得する
 */
const { customers, isLoading, errorMessage } = storeToRefs(customerStore)

/**
 * 画面初期表示時に顧客一覧を取得する
 */
onMounted(async () => {
  await customerStore.loadCustomers()
})

/**
 * 画面離脱時に不要な状態をクリアする
 */
onUnmounted(() => {
  customerStore.clearError()
})
</script>
