<template>
  <div class="space-y-1">
    <!-- 入力項目ラベル -->
    <label v-if="label" :for="id" class="block text-sm font-medium text-slate-700">
      {{ label }}
      <span v-if="required" class="ml-1 text-red-500">*</span>
    </label>

    <!-- 共通入力欄 -->
    <input
      :id="id"
      :value="modelValue"
      :type="type"
      :placeholder="placeholder"
      :disabled="disabled"
      :readonly="readonly"
      :class="inputClass"
      class="block w-full rounded-md border px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition placeholder:text-slate-400 focus:ring-2 disabled:bg-slate-100 disabled:text-slate-500"
      @input="handleInput"
    />

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

interface Props {
  // undefinedの追加は一時的な処置
  modelValue: string | number | undefined
  id?: string
  label?: string
  type?: string
  placeholder?: string
  disabled?: boolean
  readonly?: boolean
  required?: boolean
  hint?: string
  errorMessage?: string
}

/**
 * 共通入力欄の props 定義
 */
const props = withDefaults(defineProps<Props>(), {
  id: '',
  label: '',
  type: 'text',
  placeholder: '',
  disabled: false,
  readonly: false,
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
 * エラー有無で入力欄の見た目を切り替える
 */
const inputClass = computed(() => {
  return props.errorMessage
    ? 'border-red-300 focus:border-red-500 focus:ring-red-200'
    : 'border-slate-300 focus:border-blue-500 focus:ring-blue-200'
})

/**
 * input イベント発火時に親へ値を返す
 */
const handleInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  emit('update:modelValue', target.value)
}
</script>
