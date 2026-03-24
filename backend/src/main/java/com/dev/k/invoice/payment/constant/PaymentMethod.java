package com.dev.k.invoice.payment.constant;

/**
 * 支払方法を表す列挙型（PaymentMethod）
 *
 * 【役割】
 * - 請求の支払手段を定義する
 * - 支払データにおける method カラムと対応する
 *
 * 【利用箇所】
 * - InvoicePayment エンティティ（@Enumerated(EnumType.STRING)）
 * - 支払登録API
 * - 支払一覧・詳細表示
 *
 * 【定義値】
 * ■ BANK_TRANSFER
 * - 銀行振込による支払
 *
 * ■ CREDIT_CARD
 * - クレジットカードによる支払
 *
 * ■ CASH
 * - 現金による支払
 *
 * 【拡張性】
 * - 将来的に以下のような支払方法も追加可能
 *   ・QRコード決済
 *   ・電子マネー
 *   ・コンビニ支払 など
 */
public enum PaymentMethod {

    BANK_TRANSFER,  //
    CREDIT_CARD,
    CASH

}