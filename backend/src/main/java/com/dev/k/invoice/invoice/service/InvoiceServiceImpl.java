package com.dev.k.invoice.invoice.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.common.constant.ErrorCode;
import com.dev.k.invoice.common.exception.BusinessException;
import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.customer.repository.CustomerRepository;
import com.dev.k.invoice.invoice.constant.InvoiceStatus;
import com.dev.k.invoice.invoice.dto.InvoiceCreateRequest;
import com.dev.k.invoice.invoice.dto.InvoiceResponse;
import com.dev.k.invoice.invoice.dto.InvoiceUpdateRequest;
import com.dev.k.invoice.invoice.entity.Invoice;
import com.dev.k.invoice.invoice.repository.InvoiceRepository;
import com.dev.k.invoice.payment.repository.InvoicePaymentRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 請求サービス実装クラス（InvoiceServiceImpl）
 *
 * 【役割】
 * - 請求に関する業務ロジック（登録・更新・取得）を実装するクラス
 * - ControllerとRepositoryの橋渡しを行う
 *
 * 【アノテーション】
 * - @Service：Spring管理のサービスクラス
 * - @Transactional：トランザクション制御（更新系はデフォルト、参照系はreadOnly）
 *
 * 【依存コンポーネント】
 * - InvoiceRepository：請求データの永続化
 * - CustomerRepository：顧客存在チェック
 * - InvoicePaymentRepository：支払金額集計
 *
 * --------------------------------------------------
 * 【処理概要】
 *
 * ■ create（請求登録）
 * - 顧客存在チェック
 * - 請求番号の重複チェック
 * - 初期値設定（ステータス・発行日時など）
 * - DB保存
 *
 * ■ update（請求更新）
 * - 請求存在チェック
 * - ステータス制御（PAID / CANCELLEDは更新不可）
 * - 指定項目のみ更新
 *
 * ■ findById（詳細取得）
 * - 請求存在チェック
 * - DTOへ変換して返却
 *
 * --------------------------------------------------
 * 【業務ロジック】
 *
 * ■ バリデーション
 * - 顧客が存在しない場合 → ResourceNotFoundException
 * - 請求番号重複 → BusinessException
 *
 * ■ ステータス制御
 * - PAID / CANCELLED の請求は更新不可
 *
 * ■ 残額計算
 * - remainingAmount = 請求金額 - 支払済金額
 * - マイナス防止で Math.max を使用
 *
 * --------------------------------------------------
 * 【利用フロー】
 * Controller → Service → Repository → Entity操作 → DTO変換 → Controller → フロント
 */
@Slf4j
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            CustomerRepository customerRepository,
            InvoicePaymentRepository invoicePaymentRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    /**
     * 請求新規作成
     */
    @Override
    public InvoiceResponse create(InvoiceCreateRequest request) {
        log.info("Service start: create invoice. invoiceNo={}, customerId={}", request.getInvoiceNo(), request.getCustomerId());

        // 顧客存在チェック
        if (!customerRepository.existsById(request.getCustomerId())) {
            log.warn("Resource not found: customer. customerId={}", request.getCustomerId());
            throw new ResourceNotFoundException("Customer not found.");
        }

        // 請求番号重複チェック
        if (invoiceRepository.existsByInvoiceNo(request.getInvoiceNo())) {
            log.warn("Validation error: invoice number already exists. invoiceNo={}", request.getInvoiceNo());
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Invoice number already exists.");
        }

        // エンティティ生成
        Invoice invoice = new Invoice();
        
        // 初期状態設定
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setInvoiceNo(request.getInvoiceNo());
        invoice.setCustomerId(request.getCustomerId());
        invoice.setTitle(request.getTitle());
        invoice.setAmount(request.getAmount());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssuedAt(OffsetDateTime.now());
        invoice.setPaidAt(null);

        // 保存
        Invoice savedInvoice = invoiceRepository.save(invoice);

        log.info("Service success: create invoice. invoiceId={}, invoiceNo={}", savedInvoice.getInvoiceId(), savedInvoice.getInvoiceNo());
        return toResponse(savedInvoice);
    }

    /**
     * 請求更新
     */
    @Override
    public InvoiceResponse update(UUID invoiceId, InvoiceUpdateRequest request) {
        log.info("Service start: update invoice. invoiceId={}", invoiceId);

        // 存在チェック
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        // ステータス制御
        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            log.warn("Invalid status for invoice update. invoiceId={}, status={}", invoiceId, invoice.getStatus());
            throw new BusinessException(ErrorCode.INVALID_STATUS, "Cannot update paid or cancelled invoice.");
        }

        // 更新（部分更新）
        invoice.setTitle(request.getTitle());
        invoice.setAmount(request.getAmount());
        invoice.setDueDate(request.getDueDate());

        Invoice updatedInvoice = invoiceRepository.save(invoice);

        log.info("Service success: update invoice. invoiceId={}", updatedInvoice.getInvoiceId());
        return toResponse(updatedInvoice);
    }

    /**
     * 請求詳細取得
     */
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse findById(UUID invoiceId) {
        log.info("Service start: find invoice by id. invoiceId={}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: invoice. invoiceId={}", invoiceId);
                    return new ResourceNotFoundException("Invoice not found.");
                });

        log.info("Service success: find invoice by id. invoiceId={}", invoice.getInvoiceId());
        return toResponse(invoice);
    }

    /**
     * Entity → Response DTO 変換
     */
    private InvoiceResponse toResponse(Invoice invoice) {
        Integer totalPaidAmount = invoicePaymentRepository.sumPaidAmountByInvoiceId(invoice.getInvoiceId());
        int remainingAmount = invoice.getAmount() - totalPaidAmount;

        InvoiceResponse response = new InvoiceResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setInvoiceNo(invoice.getInvoiceNo());
        response.setCustomerId(invoice.getCustomerId());
        response.setTitle(invoice.getTitle());
        response.setAmount(invoice.getAmount());
        response.setTotalPaidAmount(totalPaidAmount);
        response.setRemainingAmount(Math.max(remainingAmount, 0));
        response.setDueDate(invoice.getDueDate());
        response.setStatus(invoice.getStatus());
        response.setIssuedAt(invoice.getIssuedAt());
        response.setPaidAt(invoice.getPaidAt());
        return response;
    }
}