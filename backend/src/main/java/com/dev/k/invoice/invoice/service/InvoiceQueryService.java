package com.dev.k.invoice.invoice.service;

import java.util.List;

import com.dev.k.invoice.invoice.dto.InvoiceSearchRequest;
import com.dev.k.invoice.invoice.dto.InvoiceSummaryResponse;

/**
 * 請求検索用クエリサービスインターフェース
 *
 * 【役割】
 * - 請求の「検索・参照系処理」に特化したサービス
 * - 複数条件による検索ロジックを集約する
 *
 * 【設計思想】
 * - Command（更新系）とQuery（参照系）を分離する設計（CQRSの簡易版）
 * - InvoiceService（更新系）とは責務を分ける
 *
 * 【提供機能】
 *
 * ■ search
 * - 検索条件（InvoiceSearchRequest）に基づいて請求一覧を取得
 * - 結果は一覧表示用DTO（InvoiceSummaryResponse）で返却
 *
 * 【検索条件例】
 * - 顧客ID
 * - ステータス
 * - 支払期限（From / To）
 *
 * 【利用フロー】
 * Controller → QueryService → Repository → DTO変換 → Controller → フロント
 *
 * 【補足】
 * - 動的検索（条件に応じたWHERE句生成）は実装クラスで対応
 * - 複雑な検索はSpecification / QueryDSL等で拡張可能
 */
public interface InvoiceQueryService {

	/**
     * 請求検索
     *
     * @param request 検索条件
     * @return 請求一覧（サマリー）
     */
    List<InvoiceSummaryResponse> search(InvoiceSearchRequest request);
}