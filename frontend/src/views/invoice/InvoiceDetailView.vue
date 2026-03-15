<template>
  <section class="space-y-4">
    <div>
      <h1 class="text-2xl font-bold text-slate-800">請求詳細</h1>
      <p class="text-sm text-slate-500">請求詳細と支払履歴を確認する画面です。</p>
    </div>

    <div class="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
      <p class="mb-2 text-slate-600">
        対象請求ID: <span class="font-medium">{{ invoiceId }}</span>
      </p>
      <p class="text-slate-600">ここに請求詳細カードと支払履歴テーブルを実装します。</p>
    </div>
  </section>
</template>

<script setup lang="ts">
/**
 * ルートパラメータから請求IDを受け取るためのprops定義
 */
interface Props {
  invoiceId: string
}

defineProps<Props>()
</script>
