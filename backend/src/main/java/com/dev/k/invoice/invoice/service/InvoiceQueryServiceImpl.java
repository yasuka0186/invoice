package com.dev.k.invoice.invoice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.invoice.dto.InvoiceSearchRequest;
import com.dev.k.invoice.invoice.dto.InvoiceSummaryResponse;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;
import com.dev.k.invoice.payment.repository.InvoicePaymentRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 請求検索用クエリサービス実装クラス
 *
 * 【役割】
 * - 請求の検索処理（参照系ロジック）を実装するクラス
 * - 複数条件によるフィルタリングおよびDTO変換を担当
 *
 * 【アノテーション】
 * - @Service：サービスクラスとしてSpring管理対象
 * - @Transactional(readOnly = true)：参照専用トランザクション（パフォーマンス最適化）
 *
 * 【依存コンポーネント】
 * - InvoiceRepository：請求データ取得
 * - InvoicePaymentRepository：支払金額の集計
 *
 * 【処理概要（search）】
 * 1. 全件取得（findAll）
 * 2. Streamで条件フィルタリング
 *    - 顧客ID
 *    - ステータス
 *    - 支払期限（From / To）
 * 3. 各Invoiceを一覧用DTOに変換
 * 4. 結果を返却
 * 【注意点（重要）】
 * - 現在は findAll → メモリフィルタのためデータ量増加に弱い
 *   → 本番では以下へ改善推奨
 *     ・Specification
 *     ・QueryDSL
 *     ・カスタムクエリ（DB側で絞り込み）
 *
 * 【残額計算ロジック】
 * - totalPaidAmount = 支払テーブルから合計取得
 * - remainingAmount = 請求金額 - 支払済金額
 * - マイナス防止のため Math.max(remainingAmount, 0)
 *
 * 【利用フロー】
 * Controller → 本Service → Repository → DTO変換 → Controller → フロント
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class InvoiceQueryServiceImpl implements InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;

    public InvoiceQueryServiceImpl(
            InvoiceRepository invoiceRepository,
            InvoicePaymentRepository invoicePaymentRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    /**
     * 請求検索処理
     *
     * @param request 検索条件
     * @return 請求一覧（サマリーDTO）
     */
    @Override
    public List<InvoiceSummaryResponse> search(InvoiceSearchRequest request) {
        log.info(
                "Service start: search invoices. customerId={}, status={}, dueDateFrom={}, dueDateTo={}",
                request.getCustomerId(),
                request.getStatus(),
                request.getDueDateFrom(),
                request.getDueDateTo()
        );

        List<InvoiceSummaryResponse> response = invoiceRepository.findAll()
                .stream()
                // 顧客IDフィルタ
                .filter(invoice -> request.getCustomerId() == null
                        || invoice.getCustomerId().equals(request.getCustomerId()))
                // ステータスフィルタ
                .filter(invoice -> request.getStatus() == null
                        || invoice.getStatus() == request.getStatus())
                // 期限（From）フィルタ
                .filter(invoice -> request.getDueDateFrom() == null
                        || !invoice.getDueDate().isBefore(request.getDueDateFrom()))
                // 期限（To）フィルタ
                .filter(invoice -> request.getDueDateTo() == null
                        || !invoice.getDueDate().isAfter(request.getDueDateTo()))
                // DTO変換
                .map(this::toSummaryResponse)
                .toList();

        log.info("Service success: search invoices. count={}", response.size());
        return response;
    }

    /**
     * Invoice → InvoiceSummaryResponse 変換
     *
     * @param invoice エンティティ
     * @return サマリーDTO
     */
    private InvoiceSummaryResponse toSummaryResponse(Invoice invoice) {
        
    	// 支払済金額の合計取得
    	Integer totalPaidAmount = invoicePaymentRepository.sumPaidAmountByInvoiceId(invoice.getInvoiceId());
        
    	// 残額計算
    	int remainingAmount = invoice.getAmount() - totalPaidAmount;

        InvoiceSummaryResponse response = new InvoiceSummaryResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setInvoiceNo(invoice.getInvoiceNo());
        response.setTitle(invoice.getTitle());
        response.setAmount(invoice.getAmount());
        response.setRemainingAmount(Math.max(remainingAmount, 0));
        response.setDueDate(invoice.getDueDate());
        response.setStatus(invoice.getStatus());
        return response;
    }
}