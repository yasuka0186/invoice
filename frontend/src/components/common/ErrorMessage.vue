<template>
  <div
    v-if="message"
    class="whitespace-pre-line rounded-md border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
  >
    <!-- 画面共通のエラーメッセージ表示 -->
    {{ message }}
  </div>
</template>

<script setup lang="ts">
interface Props {
  message?: string | null
}

/**
 * 共通エラーメッセージ表示コンポーネント
 * - API失敗時
 * - バリデーションエラー時
 * などで使用する
 */
withDefaults(defineProps<Props>(), {
  message: '',
})
</script>
