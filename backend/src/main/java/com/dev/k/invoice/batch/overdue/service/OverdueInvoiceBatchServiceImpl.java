package com.dev.k.invoice.batch.overdue.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.batch.overdue.dto.BatchResult;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 期限切れ請求バッチサービスの実装クラス
 *
 * 【役割】
 * - 支払期限を過ぎた請求（ISSUED状態）を抽出し、OVERDUE状態へ更新する
 *
 * 【処理フロー】
 * 1. 現在日付より前の支払期限（dueDate）を持つ請求を取得
 * 2. ステータスが ISSUED（未払い）のもののみ対象
 * 3. 対象請求を OVERDUE（期限切れ）へ更新
 * 4. 更新件数をカウントし、結果を返却
 *
 * 【トランザクション】
 * - クラス全体に @Transactional を付与
 * - バッチ処理全体を1トランザクションとして扱う
 *
 * 【ログ設計】
 * - 開始・対象件数・更新処理・終了をログ出力
 * - 運用時のトラブルシュート・監視に利用可能
 *
 * 【注意点】
 * - 現状は1件ずつ save() を実行（シンプル実装）
 * - 大量データの場合はバルク更新（UPDATE文）への改善余地あり
 */
@Slf4j
@Service
@Transactional
public class OverdueInvoiceBatchServiceImpl implements OverdueInvoiceBatchService {

	/**
     * 請求リポジトリ
     * - DBアクセス（検索・更新）を担当
     */
    private final InvoiceRepository invoiceRepository;

    public OverdueInvoiceBatchServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * 期限切れ請求のステータス更新処理を実行する
     *
     * @return BatchResult
     *         - targetCount：対象となった請求件数
     *         - updatedCount：実際に更新した件数
     */
    @Override
    public BatchResult execute() {
        log.info("Batch start: overdue invoice update.");

        /**
         * 対象請求を取得
         * - 支払期限が現在日より前
         * - ステータスが ISSUED（未払い）
         */
        List<Invoice> targetInvoices = invoiceRepository.findByDueDateBeforeAndStatus(
                LocalDate.now(),
                InvoiceStatus.ISSUED
        );

        int targetCount = targetInvoices.size();
        int updatedCount = 0;

        log.info("Batch target invoices found. targetCount={}", targetCount);

        /**
         * 各請求を期限切れへ更新
         */
        for (Invoice invoice : targetInvoices) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
            
       　　　// 永続化（UPDATE）
            invoiceRepository.save(invoice);
            updatedCount++;
            log.info("Batch updated invoice to overdue. invoiceId={}", invoice.getInvoiceId());
        }

        log.info("Batch finished: overdue invoice update. targetCount={}, updatedCount={}", targetCount, updatedCount);
        return new BatchResult(targetCount, updatedCount);
    }
}