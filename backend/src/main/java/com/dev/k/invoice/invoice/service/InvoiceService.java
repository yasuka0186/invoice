package com.dev.k.invoice.invoice.service;

import java.util.UUID;

import com.dev.k.invoice.invoice.dto.InvoiceCreateRequest;
import com.dev.k.invoice.invoice.dto.InvoiceResponse;
import com.dev.k.invoice.invoice.dto.InvoiceUpdateRequest;

/**
 * 請求サービスインターフェース（更新系 / 参照系）
 *
 * 【役割】
 * - 請求に関する「業務ロジック」を定義するサービス
 * - ControllerとRepositoryの中間層として機能する
 *
 * 【設計思想】
 * - Command（登録・更新）と一部参照処理を担当
 * - 検索（複雑な参照）は InvoiceQueryService に分離
 *
 * 【提供機能】
 *
 * ■ create
 * - 請求を新規作成する
 * - バリデーション（重複チェックなど）および初期値設定を行う
 *
 * ■ update
 * - 既存請求を更新する
 * - 指定された項目のみ変更（部分更新）
 *
 * ■ findById
 * - 請求IDで請求詳細を取得する
 * - 存在しない場合は例外をスロー
 *
 * 【利用フロー】
 * Controller → Service → Repository → Entity操作 → DTO変換 → Controller → フロント
 *
 * 【補足】
 * - ビジネスルール（例：ステータス遷移、重複チェック）はService層で管理
 * - DTO ↔ Entity の変換もService層で実施
 */
public interface InvoiceService {

	/**
     * 請求新規作成
     *
     * @param request 作成リクエスト
     * @return 作成された請求情報
     */
    InvoiceResponse create(InvoiceCreateRequest request);

    /**
     * 請求更新
     *
     * @param invoiceId 請求ID
     * @param request 更新リクエスト
     * @return 更新後の請求情報
     */
    InvoiceResponse update(UUID invoiceId, InvoiceUpdateRequest request);

    /**
     * 請求詳細取得
     *
     * @param invoiceId 請求ID
     * @return 請求詳細情報
     */
    InvoiceResponse findById(UUID invoiceId);
}