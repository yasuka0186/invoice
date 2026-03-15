<template>
  <section class="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
    <!-- カードタイトル -->
    <div v-if="title || $slots.header" class="mb-4">
      <slot name="header">
        <h2 class="text-lg font-semibold text-slate-800">
          {{ title }}
        </h2>
      </slot>
    </div>

    <!-- カード本文 -->
    <div>
      <slot />
    </div>
  </section>
</template>

<script setup lang="ts">
interface Props {
  title?: string
}

/**
 * 共通カードコンポーネント
 * - 白背景
 * - 枠線
 * - 影付き
 * のシンプルなコンテナとして使用する
 */
withDefaults(defineProps<Props>(), {
  title: '',
})
</script>
