<template>
  <div class="min-h-screen bg-slate-50">
    <!-- 全画面共通ヘッダー -->
    <AppHeader />

    <!-- 各画面の表示領域 -->
    <main class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
      <router-view />
    </main>
  </div>
</template>
<script setup lang="ts">
import AppHeader from './components/common/AppHeader.vue'
// アプリ全体の共通レイアウト
// Header と RouterView を配置する
</script>
<style scoped></style>
