package com.dev.k.invoice.invoice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.entity.Invoice;

/**
 * 請求リポジトリ（InvoiceRepository）
 *
 * 【役割】
 * - invoicesテーブルに対するデータアクセス（CRUD）を担当するインターフェース
 * - Spring Data JPAにより実装は自動生成される
 *
 * 【継承】
 * - JpaRepository<Invoice, UUID>
 *   → 基本的なCRUD操作（save, findById, findAll, deleteなど）を提供
 *
 * 【設計ポイント】
 * - メソッド名ベースのクエリ（Query Method）を活用
 * - 業務でよく使う検索条件をRepositoryに定義
 * - Service層から呼び出して利用
 *
 * 【提供メソッド】
 *
 * ■ findByInvoiceNo
 * - 請求番号で請求を1件取得
 * - ユニーク制約前提のためOptionalで返却
 *
 * ■ existsByInvoiceNo
 * - 請求番号の重複チェックに使用
 * - 登録時のバリデーションで利用
 *
 * ■ findByCustomerId
 * - 指定顧客に紐づく請求一覧を取得
 *
 * ■ findByStatus
 * - ステータスで請求一覧を取得
 *
 * ■ findByDueDateBeforeAndStatus
 * - 指定日より前の期限かつ特定ステータスの請求を取得
 * - 主にバッチ処理（期限切れ判定）で使用
 *
 * 【利用フロー】
 * Controller → Service → Repository → DB
 *
 * 【補足】
 * - 複雑な検索はQueryServiceやSpecificationで拡張可能
 * - JPQL / Native Queryを使う場合は @Query を利用
 */
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

	/**
     * 請求番号で請求を取得
     */
    Optional<Invoice> findByInvoiceNo(String invoiceNo);

    /**
     * 請求番号の存在チェック（重複確認）
     */
    boolean existsByInvoiceNo(String invoiceNo);

    /**
     * 顧客IDで請求一覧を取得
     */
    List<Invoice> findByCustomerId(UUID customerId);

    /**
     * ステータスで請求一覧を取得
     */
    List<Invoice> findByStatus(InvoiceStatus status);

    /**
     * 指定日より前の期限かつ指定ステータスの請求を取得
     * （例：期限切れバッチ処理）
     */
    List<Invoice> findByDueDateBeforeAndStatus(LocalDate dueDate, InvoiceStatus status);
}