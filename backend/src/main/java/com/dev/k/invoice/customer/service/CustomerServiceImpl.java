package com.dev.k.invoice.customer.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.k.invoice.common.constant.ErrorCode;
import com.dev.k.invoice.common.exception.BusinessException;
import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.customer.dto.CustomerCreateRequest;
import com.dev.k.invoice.customer.dto.CustomerResponse;
import com.dev.k.invoice.customer.dto.CustomerUpdateRequest;
import com.dev.k.invoice.customer.entity.Customer;
import com.dev.k.invoice.customer.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 顧客サービス実装クラス
 *
 * 【役割】
 * - 顧客に関するビジネスロジックを実装する
 * - Controllerからのリクエストを受け、Repositoryを通じてDB操作を行う
 *
 * 【例外設計】
 * - 業務エラー：BusinessException
 * - 未存在：ResourceNotFoundException
 */
@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	/**
     * 顧客リポジトリ
     * - DBアクセスを担当
     */
    private final CustomerRepository customerRepository;

    
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * 顧客登録処理
     *
     * 【処理内容】
     * - 顧客コードの重複チェック
     * - UUID採番
     * - Entity生成・保存
     *
     * @param request 顧客作成リクエスト
     * @return 登録された顧客情報
     */
    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        log.info("Service start: create customer. customerCode={}", request.getCustomerCode());

        // 顧客コードの重複チェック
        if (customerRepository.existsByCustomerCode(request.getCustomerCode())) {
            log.warn("Validation error: customer code already exists. customerCode={}", request.getCustomerCode());
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Customer code already exists.");
        }

        // Entity生成
        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setCustomerCode(request.getCustomerCode());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setIsActive(true);

     // 保存
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Service success: create customer. customerId={}", savedCustomer.getCustomerId());
        return toResponse(savedCustomer);
    }

    /**
     * 顧客更新処理
     *
     * 【処理内容】
     * - 顧客存在チェック
     * - 更新対象項目の反映（部分更新）
     * - 保存
     *
     * @param customerId 顧客ID
     * @param request    更新内容
     * @return 更新後の顧客情報
     */
    @Override
    public CustomerResponse update(UUID customerId, CustomerUpdateRequest request) {
        log.info("Service start: update customer. customerId={}", customerId);

        // 存在チェック
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: customer. customerId={}", customerId);
                    return new ResourceNotFoundException("Customer not found.");
                });

        // 更新（部分更新）
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        }

        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Service success: update customer. customerId={}", updatedCustomer.getCustomerId());
        return toResponse(updatedCustomer);
    }

    /**
     * 顧客詳細取得処理
     *
     * @param customerId 顧客ID
     * @return 顧客情報
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID customerId) {
        log.info("Service start: find customer by id. customerId={}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: customer. customerId={}", customerId);
                    return new ResourceNotFoundException("Customer not found.");
                });

        log.info("Service success: find customer by id. customerId={}", customer.getCustomerId());
        return toResponse(customer);
    }

    /**
     * 顧客一覧取得処理
     *
     * @return 顧客一覧
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        log.info("Service start: find all customers.");

        List<CustomerResponse> response = customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Service success: find all customers. count={}", response.size());
        return response;
    }

    /**
     * Entity → Response DTO 変換処理
     *
     * @param customer 顧客エンティティ
     * @return 顧客レスポンスDTO
     */
    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setCustomerCode(customer.getCustomerCode());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setIsActive(customer.getIsActive());
        return response;
    }

}