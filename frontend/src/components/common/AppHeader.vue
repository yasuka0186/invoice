<template>
  <header class="border-b border-slate-200 bg-white shadow-sm">
    <div class="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6 lg:px-8">
      <!-- アプリタイトル -->
      <div>
        <router-link to="/invoices" class="text-xl font-bold text-slate-800 hover:text-blue-600">
          請求・支払管理システム
        </router-link>
      </div>

      <!-- グローバルナビゲーション -->
      <nav class="flex items-center gap-2">
        <router-link
          to="/invoices"
          class="rounded-md px-3 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-100 hover:text-blue-600"
          active-class="bg-blue-50 text-blue-700"
        >
          請求一覧
        </router-link>

        <router-link
          to="/customers"
          class="rounded-md px-3 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-100 hover:text-blue-600"
          active-class="bg-blue-50 text-blue-700"
        >
          顧客一覧
        </router-link>
      </nav>
    </div>
  </header>
</template>

<script setup lang="ts">
// 共通ヘッダーコンポーネント
// アプリタイトルと主要画面へのナビゲーションを表示する
</script>
