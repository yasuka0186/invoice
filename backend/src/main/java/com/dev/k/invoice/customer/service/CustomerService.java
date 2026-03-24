package com.dev.k.invoice.customer.service;

import java.util.List;
import java.util.UUID;

import com.dev.k.invoice.customer.dto.CustomerCreateRequest;
import com.dev.k.invoice.customer.dto.CustomerResponse;
import com.dev.k.invoice.customer.dto.CustomerUpdateRequest;

/**
 * 顧客サービスインターフェース
 *
 * 【役割】
 * - 顧客に関するビジネスロジックを定義する層
 * - ControllerとRepositoryの橋渡しを行う
 *
 * 【設計方針】
 * - インターフェースと実装を分離（DI・テスト容易性向上）
 * - Controllerはこのインターフェースのみを参照する
 * - DB操作はRepositoryに委譲し、業務ロジックに専念する
 *
 * 【提供機能】
 * - 顧客登録
 * - 顧客更新
 * - 顧客詳細取得
 * - 顧客一覧取得
 *
 * 【注意点】
 * - DTO ↔ Entity の変換はService層で行う
 * - バリデーション後の業務チェック（重複・存在確認など）を実装する
 */
public interface CustomerService {

	/**
     * 顧客を新規登録する
     *
     * 【処理概要】
     * - 顧客コードの重複チェック
     * - 顧客情報の作成
     * - DBへ保存
     *
     * @param request 顧客作成リクエスト
     * @return 登録された顧客情報
     */
    CustomerResponse create(CustomerCreateRequest request);

    /**
     * 顧客情報を更新する
     *
     * 【処理概要】
     * - 顧客存在チェック
     * - 更新対象項目のみ反映（部分更新）
     * - DBへ保存
     *
     * @param customerId 更新対象の顧客ID
     * @param request    更新内容
     * @return 更新後の顧客情報
     */
    CustomerResponse update(UUID customerId, CustomerUpdateRequest request);

    /**
     * 顧客IDで顧客情報を取得する
     *
     * 【処理概要】
     * - 顧客存在チェック
     * - 該当データを返却
     *
     * @param customerId 顧客ID
     * @return 顧客情報
     */
    CustomerResponse findById(UUID customerId);

    /**
     * 全顧客一覧を取得する
     *
     * @return 顧客一覧
     */
    List<CustomerResponse> findAll();
}