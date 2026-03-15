<template>
  <button
    :type="type"
    :disabled="disabled || loading"
    :class="buttonClass"
    class="inline-flex items-center justify-center rounded-md px-4 py-2 text-sm font-medium transition focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
    @click="handleClick"
  >
    <!-- ローディング中は文言を切り替えて表示 -->
    <span v-if="loading">{{ loadingText }}</span>
    <span v-else>{{ label }}</span>
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

/**
 * ボタン種別
 * - primary: メイン操作
 * - secondary: 補助操作
 * - danger: 削除などの危険操作
 */
type ButtonVariant = 'primary' | 'secondary' | 'danger'

interface Props {
  label: string
  type?: 'button' | 'submit' | 'reset'
  variant?: ButtonVariant
  disabled?: boolean
  loading?: boolean
  loadingText?: string
}

/**
 * 共通ボタンの props 定義
 */
const props = withDefaults(defineProps<Props>(), {
  type: 'button',
  variant: 'primary',
  disabled: false,
  loading: false,
  loadingText: '処理中...',
})

/**
 * click イベントを親へ通知する
 */
const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
}>()

/**
 * ボタンの見た目を variant ごとに切り替える
 */
const buttonClass = computed(() => {
  switch (props.variant) {
    case 'secondary':
      return 'bg-slate-100 text-slate-700 hover:bg-slate-200 focus:ring-slate-400'
    case 'danger':
      return 'bg-red-600 text-white hover:bg-red-700 focus:ring-red-500'
    case 'primary':
    default:
      return 'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500'
  }
})

/**
 * disabled または loading のときは click を無効化する
 */
const handleClick = (event: MouseEvent) => {
  if (props.disabled || props.loading) {
    return
  }
  emit('click', event)
}
</script>
