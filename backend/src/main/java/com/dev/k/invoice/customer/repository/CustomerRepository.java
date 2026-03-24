package com.dev.k.invoice.customer.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.k.invoice.customer.entity.Customer;

/**
 * 顧客リポジトリ
 *
 * 【役割】
 * - Customerエンティティに対するDBアクセスを担当するインターフェース
 * - Spring Data JPAにより基本的なCRUD操作を自動生成
 *
 * 【継承】
 * - JpaRepository<Customer, UUID>
 *   → 主キーがUUIDのCustomerエンティティを扱う
 *
 * 【提供機能】
 * ■ 標準機能（JpaRepository由来）
 * - save()         ：登録・更新
 * - findById()     ：主キー検索
 * - findAll()      ：全件取得
 * - delete()       ：削除
 *
 * ■ カスタムメソッド
 * - findByCustomerCode()   ：顧客コードで検索
 * - existsByCustomerCode()：顧客コードの存在チェック
 *
 * 【利用シーン】
 * - Service層から呼び出してDB操作を実行
 * - 顧客登録時の重複チェック
 * - 顧客検索（業務キー：customerCode）
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	/**
     * 顧客コードで顧客を検索する
     *
     * @param customerCode 顧客コード
     * @return 顧客情報（存在しない場合はOptional.empty）
     */
    Optional<Customer> findByCustomerCode(String customerCode);

    /**
     * 指定した顧客コードが存在するか判定する
     *
     * @param customerCode 顧客コード
     * @return 存在する場合 true、存在しない場合 false
     */
    boolean existsByCustomerCode(String customerCode);
}