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

@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        log.info("Service start: create customer. customerCode={}", request.getCustomerCode());

        if (customerRepository.existsByCustomerCode(request.getCustomerCode())) {
            log.warn("Validation error: customer code already exists. customerCode={}", request.getCustomerCode());
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Customer code already exists.");
        }

        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setCustomerCode(request.getCustomerCode());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setIsActive(true);

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Service success: create customer. customerId={}", savedCustomer.getCustomerId());
        return toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse update(UUID customerId, CustomerUpdateRequest request) {
        log.info("Service start: update customer. customerId={}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn("Resource not found: customer. customerId={}", customerId);
                    return new ResourceNotFoundException("Customer not found.");
                });

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        }

        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Service success: update customer. customerId={}", updatedCustomer.getCustomerId());
        return toResponse(updatedCustomer);
    }

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