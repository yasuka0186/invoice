<template>
  <section class="space-y-4">
    <div>
      <h1 class="text-2xl font-bold text-slate-800">顧客一覧</h1>
      <p class="text-sm text-slate-500">顧客情報の一覧を表示する画面です。</p>
    </div>

    <div class="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
      <p class="text-slate-600">ここに顧客一覧テーブルを実装します。</p>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useCustomerStore } from '@/stores/customer'

/**
 * 顧客Storeを取得
 */
const customerStore = useCustomerStore()

/**
 * store の state をリアクティブ参照として取り出す
 */
const { customers, isLoading, errorMessage } = storeToRefs(customerStore)

/**
 * 画面表示時に顧客一覧を取得する
 */
onMounted(async () => {
  await customerStore.loadCustomers()
  console.log('customers:', customers.value)
  console.log('isLoading:', isLoading.value)
  console.log('errorMessage:', errorMessage.value)
})
</script>
