package com.dev.k.invoice.payment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.k.invoice.payment.entity.InvoicePayment;

/**
 * 支払リポジトリ（InvoicePaymentRepository）
 *
 * 【役割】
 * - 支払エンティティ（InvoicePayment）のDBアクセスを担当
 * - CRUD操作およびカスタムクエリを提供する
 *
 * 【継承】
 * - JpaRepository<InvoicePayment, UUID>
 *   → 基本的なCRUD操作（save, findById, findAll など）を自動提供
 *
 * --------------------------------------------------
 * 【提供メソッド】
 *
 * ■ findByInvoiceIdOrderByPaidAtAsc
 * - 指定した請求IDに紐づく支払一覧を取得
 * - 支払日時の昇順で並び替え
 *
 * ■ sumPaidAmountByInvoiceId
 * - 指定した請求IDの支払合計金額を取得
 * - null対策としてcoalesceで0を返却
 *
 * --------------------------------------------------
 * 【業務上の利用例】
 * - 支払履歴表示（一覧）
 * - 請求の残額計算
 *   → remainingAmount = 請求金額 - 支払合計
 * --------------------------------------------------
 * 【補足】
 * - sumPaidAmountByInvoiceId は重要な集計処理
 *   → Service層で頻繁に利用される
 *
 * - coalesceを使用する理由
 *   → 支払データが存在しない場合でも null ではなく 0 を返すため
 */
public interface InvoicePaymentRepository extends JpaRepository<InvoicePayment, UUID> {

	/**
     * 請求IDで支払一覧を取得（支払日時昇順）
     *
     * @param invoiceId 請求ID
     * @return 支払一覧
     */
    List<InvoicePayment> findByInvoiceIdOrderByPaidAtAsc(UUID invoiceId);

    /**
     * 請求IDごとの支払合計金額を取得
     *
     * @param invoiceId 請求ID
     * @return 支払合計金額（該当なしの場合は0）
     */
    @Query("""
            select coalesce(sum(ip.paidAmount), 0)
            from InvoicePayment ip
            where ip.invoiceId = :invoiceId
            """)
    Integer sumPaidAmountByInvoiceId(UUID invoiceId);
}