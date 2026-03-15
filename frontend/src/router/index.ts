import { createRouter, createWebHistory } from 'vue-router'
import CustomerListView from '@/views/customer/CustomerListView.vue'
import InvoiceListView from '@/views/invoice/Invoice/InvoiceListView.vue'
import InvoiceCreateView from '@/views/invoice/InvoiceDetailView.vue'
import InvoiceDetailView from '@/views/invoice/PaymentCreateView.vue'
import PaymentCreateView from '@/views/payment/PaymentCreateView.vue'

/**
 * アプリ全体のルーティング定義
 * - トップページは /invoices にリダイレクト
 * - 請求、支払、顧客の各画面へ遷移可能
 */
const routes = [
  {
    path: '/',
    redirect: '/invoices',
  },
  {
    path: '/customers',
    name: 'CustomerList',
    component: CustomerListView,
  },
  {
    path: '/invoices',
    name: 'InvoiceList',
    component: InvoiceListView,
  },
  {
    path: '/invoices/new',
    name: 'InvoiceCreate',
    component: InvoiceCreateView,
  },
  {
    path: '/invoices/:invoicedId',
    name: 'InvoiceDetailView',
    component: InvoiceDetailView,
    props: true,
  },
  {
    path: '/payments/new',
    name: 'PaymentCreate',
    component: PaymentCreateView,
  },
]

/**
 * Vue Router インスタンス作成
 * HTML5 History モードを使用
 */
export const router = createRouter({
  history: createWebHistory(),
  routes: [],
})

export default router
