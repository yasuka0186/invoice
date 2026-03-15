<template>
  <div class="space-y-1">
    <!-- セレクト項目ラベル -->
    <label v-if="label" :for="id" class="block text-sm font-medium text-slate-700">
      {{ label }}
      <span v-if="required" class="ml-1 text-red-500">*</span>
    </label>

    <!-- 共通セレクトボックス -->
    <select
      :id="id"
      :value="modelValue"
      :disabled="disabled"
      :class="selectClass"
      class="block w-full rounded-md border px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:ring-2 disabled:bg-slate-100 disabled:text-slate-500"
      @change="handleChange"
    >
      <option v-if="placeholder" value="">
        {{ placeholder }}
      </option>
      <option v-for="option in options" :key="option.value" :value="option.value">
        {{ option.label }}
      </option>
    </select>

    <!-- 補助説明文 -->
    <p v-if="hint" class="text-xs text-slate-500">
      {{ hint }}
    </p>

    <!-- エラーメッセージ -->
    <p v-if="errorMessage" class="text-xs text-red-600">
      {{ errorMessage }}
    </p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

/**
 * セレクト項目の型
 */
export interface SelectOption {
  label: string
  value: string
}

interface Props {
  modelValue: string
  id?: string
  label?: string
  options: SelectOption[]
  placeholder?: string
  disabled?: boolean
  required?: boolean
  hint?: string
  errorMessage?: string
}

/**
 * 共通セレクトの props 定義
 */
const props = withDefaults(defineProps<Props>(), {
  id: '',
  label: '',
  placeholder: '',
  disabled: false,
  required: false,
  hint: '',
  errorMessage: '',
})

/**
 * v-model 更新イベント
 */
const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

/**
 * エラー有無でセレクトの見た目を切り替える
 */
const selectClass = computed(() => {
  return props.errorMessage
    ? 'border-red-300 focus:border-red-500 focus:ring-red-200'
    : 'border-slate-300 focus:border-blue-500 focus:ring-blue-200'
})

/**
 * change イベント発火時に親へ値を返す
 */
const handleChange = (event: Event) => {
  const target = event.target as HTMLSelectElement
  emit('update:modelValue', target.value)
}
</script>
