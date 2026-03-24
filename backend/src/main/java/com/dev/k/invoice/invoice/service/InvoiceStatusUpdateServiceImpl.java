package com.dev.k.invoice.invoice.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 請求ステータス更新サービス実装クラス
 *
 * 【役割】
 * - 請求のステータス変更（状態遷移）に関する業務ロジックを実装する
 * - 単純な更新ではなく、業務ルールに基づいた安全な状態変更を行う
 *
 * 【アノテーション】
 * - @Service：Spring管理のサービスクラス
 * - @Transactional：トランザクション制御（更新処理）
 *
 * 【依存コンポーネント】
 * - InvoiceRepository：請求データの取得・更新
 *
 * --------------------------------------------------
 * 【提供機能】
 *
 * ■ markAsPaid（支払済に更新）
 * - 指定した請求を PAID に変更
 * - 支払日時（paidAt）を現在時刻で設定
 *
 * ■ markAsOverdue（期限超過に更新）
 * - 支払済・取消済の請求は対象外（更新スキップ）
 * - 支払期限を過ぎている場合のみ OVERDUE に変更
 *
 * --------------------------------------------------
 * 【業務ルール】
 *
 * ■ 共通
 * - 請求が存在しない場合は ResourceNotFoundException をスロー
 *
 * ■ 支払済更新（markAsPaid）
 * - 強制的に PAID に更新
 * - 支払日時をセット
 *
 * ■ 期限超過更新（markAsOverdue）
 * - PAID / CANCELLED の場合は更新しない
 * - dueDate < 今日 の場合のみ OVERDUE に更新
 *
 * 【利用シーン】
 * - 支払登録後のステータス更新
 * - バッチ処理による期限超過更新
 *
 * 【利用フロー】
 * Controller / Batch → 本Service → Repository → Entity更新
 */

@Slf4j
@Service
@Transactional
public class InvoiceStatusUpdateServiceImpl implements InvoiceStatusUpdateService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceStatusUpdateServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * 請求を支払済（PAID）に更新する
     *
     * @param invoiceId 請求ID
     */
    @Override
    public void markAsPaid(UUID invoiceId) {
        log.info("Service start: mark invoice as paid. invoiceId={}", invoiceId);

        // 存在チェック
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        // ステータス更新
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(OffsetDateTime.now());
        invoiceRepository.save(invoice);

        log.info("Service success: mark invoice as paid. invoiceId={}", invoiceId);
    }

    /**
     * 請求を期限超過（OVERDUE）に更新する
     *
     * @param invoiceId 請求ID
     */
    @Override
    public void markAsOverdue(UUID invoiceId) {
        log.info("Service start: mark invoice as overdue. invoiceId={}", invoiceId);

        // 存在チェック
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        // 更新対象外チェック
        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            log.info("Skip overdue update. invoiceId={}, status={}", invoiceId, invoice.getStatus());
            return;
        }

        // 期限超過判定
        if (invoice.getDueDate().isBefore(LocalDate.now())) {
            invoice.setStatus(InvoiceStatus.OVERDUE);
            invoiceRepository.save(invoice);
            log.info("Service success: mark invoice as overdue. invoiceId={}", invoiceId);
            return;
        }

        // 条件未達のため更新スキップ
        log.info("Skip overdue update. invoiceId={}, dueDate={}", invoiceId, invoice.getDueDate());
    }
}