<template>
  <section class="space-y-4">
    <div>
      <h1 class="text-2xl font-bold text-slate-800">支払登録</h1>
      <p class="text-sm text-slate-500">請求に対する支払を登録する画面です。</p>
    </div>

    <div class="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
      <p class="mb-2 text-slate-600">
        対象請求ID（クエリ想定）: <span class="font-medium">{{ invoiceId }}</span>
      </p>
      <p class="text-slate-600">ここに支払登録フォームを実装します。</p>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

/**
 * 現在のルート情報を取得
 */
const route = useRoute()

/**
 * クエリパラメータから invoiceId を取得
 * 例: /payments/new?invoiceId=xxx
 */
const invoiceId = computed(() => {
  const queryValue = route.query.invoiceId
  return typeof queryValue === 'string' ? queryValue : ''
})
</script>
