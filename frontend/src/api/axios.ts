import axios from 'axios'

/**
 * 共通Axiosインスタンス
 * - baseURL は Vite の環境変数から取得
 * - 未設定時は localhost を利用
 * - timeout を設定して無限待機を防止
 */
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

/**
 * リクエスト送信前の共通処理
 * 今後、認証トークン付与などを追加しやすいように interceptor を定義
 */
axiosInstance.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

/**
 * レスポンス受信後の共通処理
 * 今後、共通エラーハンドリングを追加しやすいように interceptor を定義
 */
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error)
  },
)

export default axiosInstance
