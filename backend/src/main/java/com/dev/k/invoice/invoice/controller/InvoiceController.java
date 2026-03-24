package com.dev.k.invoice.invoice.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.k.invoice.common.dto.ApiResponse;
import com.dev.k.invoice.invoice.dto.InvoiceCreateRequest;
import com.dev.k.invoice.invoice.dto.InvoiceResponse;
import com.dev.k.invoice.invoice.dto.InvoiceSearchRequest;
import com.dev.k.invoice.invoice.dto.InvoiceSummaryResponse;
import com.dev.k.invoice.invoice.dto.InvoiceUpdateRequest;
import com.dev.k.invoice.invoice.service.InvoiceQueryService;
import com.dev.k.invoice.invoice.service.InvoiceService;

import lombok.extern.slf4j.Slf4j;

/**
 * 請求APIコントローラ
 *
 * 【役割】
 * - 請求（Invoice）に関するHTTPリクエストを受け付けるエントリーポイント
 * - リクエストのバリデーション、ログ出力、レスポンス整形を担当
 *
 * 【提供API】
 * - POST   /api/invoices              ：請求登録
 * - PUT    /api/invoices/{invoiceId}  ：請求更新
 * - GET    /api/invoices/{invoiceId}  ：請求詳細取得
 * - POST   /api/invoices/search       ：請求検索（条件指定）
 */
@Slf4j
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

	/**
     * 請求操作サービス（登録・更新・取得）
     */
    private final InvoiceService invoiceService;
    
    /**
     * 請求検索サービス（検索専用）
     */
    private final InvoiceQueryService invoiceQueryService;

    public InvoiceController(
            InvoiceService invoiceService,
            InvoiceQueryService invoiceQueryService
    ) {
        this.invoiceService = invoiceService;
        this.invoiceQueryService = invoiceQueryService;
    }

    /**
     * 請求登録API
     *
     * @param request 請求作成リクエスト（バリデーション対象）
     * @return 作成された請求情報
     */
    @PostMapping
    public ApiResponse<InvoiceResponse> create(@Valid @RequestBody InvoiceCreateRequest request) {
        log.info("API start: create invoice. invoiceNo={}, customerId={}", request.getInvoiceNo(), request.getCustomerId());

        InvoiceResponse response = invoiceService.create(request);

        log.info("API success: create invoice. invoiceId={}, invoiceNo={}", response.getInvoiceId(), response.getInvoiceNo());
        return ApiResponse.success(response);
    }

    /**
     * 請求更新API
     *
     * @param invoiceId 更新対象の請求ID
     * @param request   更新内容（バリデーション対象）
     * @return 更新後の請求情報
     */
    @PutMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> update(
            @PathVariable UUID invoiceId,
            @Valid @RequestBody InvoiceUpdateRequest request
    ) {
        log.info("API start: update invoice. invoiceId={}", invoiceId);

        InvoiceResponse response = invoiceService.update(invoiceId, request);

        log.info("API success: update invoice. invoiceId={}", response.getInvoiceId());
        return ApiResponse.success(response);
    }

    /**
     * 請求詳細取得API
     *
     * @param invoiceId 請求ID
     * @return 請求詳細情報
     */
    @GetMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> findById(@PathVariable UUID invoiceId) {
        log.info("API start: find invoice by id. invoiceId={}", invoiceId);

        InvoiceResponse response = invoiceService.findById(invoiceId);

        log.info("API success: find invoice by id. invoiceId={}, status={}", response.getInvoiceId(), response.getStatus());
        return ApiResponse.success(response);
    }

    /**
     * 請求検索API
     *
     * 【特徴】
     * - 複数条件（顧客ID、ステータス、支払期限）で検索可能
     * - POSTで検索条件をリクエストボディとして受け取る
     *
     * @param request 検索条件
     * @return 請求一覧（サマリー）
     */
    @PostMapping("/search")
    public ApiResponse<List<InvoiceSummaryResponse>> search(@RequestBody InvoiceSearchRequest request) {
        log.info(
                "API start: search invoices. customerId={}, status={}, dueDateFrom={}, dueDateTo={}",
                request.getCustomerId(),
                request.getStatus(),
                request.getDueDateFrom(),
                request.getDueDateTo()
        );

        List<InvoiceSummaryResponse> response = invoiceQueryService.search(request);

        log.info("API success: search invoices. count={}", response.size());
        return ApiResponse.success(response);
    }
}