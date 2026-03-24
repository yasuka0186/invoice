package com.dev.k.invoice.customer.controller;

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
import com.dev.k.invoice.customer.dto.CustomerCreateRequest;
import com.dev.k.invoice.customer.dto.CustomerResponse;
import com.dev.k.invoice.customer.dto.CustomerUpdateRequest;
import com.dev.k.invoice.customer.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

/**
 * 顧客APIコントローラ
 *
 * 【役割】
 * - 顧客に関するHTTPリクエストを受け付けるエントリーポイント
 * - リクエストのバリデーション・ログ出力・レスポンス整形を担当
 *
 * 【提供API】
 * - POST   /api/customers        ：顧客登録
 * - PUT    /api/customers/{id}   ：顧客更新
 * - GET    /api/customers/{id}   ：顧客詳細取得
 * - GET    /api/customers        ：顧客一覧取得
 */
@Slf4j
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	/**
     * 顧客サービス
     * - 顧客に関するビジネスロジックを担当
     */
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 顧客登録API
     *
     * @param request 顧客作成リクエスト（バリデーション対象）
     * @return 作成された顧客情報
     */
    @PostMapping
    public ApiResponse<CustomerResponse> create(@Valid @RequestBody CustomerCreateRequest request) {
    	log.info("API start: create customer. customerCode={}", request.getCustomerCode());
    	CustomerResponse response = customerService.create(request);
    	log.info("API success: create customer. customerId={}", response.getCustomerId());
    	
    	return ApiResponse.success(response);
    }

    /**
     * 顧客更新API
     *
     * @param customerId 更新対象の顧客ID
     * @param request    更新内容（バリデーション対象）
     * @return 更新後の顧客情報
     */
    @PutMapping("/{customerId}")
    public ApiResponse<CustomerResponse> update(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
    	log.info("API start: update customer. customerId={}", customerId);
        CustomerResponse response = customerService.update(customerId, request);
        log.info("API success: update customer. customerId={}", response.getCustomerId());
        
        return ApiResponse.success(response);
    }

    /**
     * 顧客詳細取得API
     *
     * @param customerId 取得対象の顧客ID
     * @return 顧客詳細情報
     */
    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponse> findById(@PathVariable UUID customerId) {
    	log.info("API start: find customer by id. customerId={}", customerId);
        CustomerResponse response = customerService.findById(customerId);
        log.info("API success: find customer by id. customerId={}", response.getCustomerId());
        
        return ApiResponse.success(response);
    }

    /**
     * 顧客一覧取得API
     *
     * @return 顧客一覧
     */
    @GetMapping
    public ApiResponse<List<CustomerResponse>> findAll() {
    	log.info("API start: find all customers.");
        List<CustomerResponse> response = customerService.findAll();
        log.info("API success: find all customers. count={}", response.size());
        
        return ApiResponse.success(response);
    }
}