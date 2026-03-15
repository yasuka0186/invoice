<template>
  <section class="space-y-4">
    <div>
      <h1 class="text-2xl font-bold text-slate-800">請求一覧</h1>
      <p class="text-sm text-slate-500">請求の検索・一覧表示を行う画面です。</p>
    </div>

    <div class="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
      <p class="text-slate-600">ここに請求検索フォームと請求一覧テーブルを実装します。</p>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { fetchInvoices } from '@/api/invoiceApi'

/**
 * 画面表示時に請求一覧取得APIを呼び出して
 * コンソールに結果を出力する動作確認用コード
 */
onMounted(async () => {
  try {
    const invoices = await fetchInvoices()
    console.log('請求一覧取得結果:', invoices)
  } catch (error) {
    console.error('請求一覧取得失敗:', error)
  }
})
</script>
