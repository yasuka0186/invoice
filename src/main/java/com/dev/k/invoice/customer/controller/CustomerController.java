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

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ApiResponse<CustomerResponse> create(@Valid @RequestBody CustomerCreateRequest request) {
        return ApiResponse.success(customerService.create(request));
    }

    @PutMapping("/{customerId}")
    public ApiResponse<CustomerResponse> update(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        return ApiResponse.success(customerService.update(customerId, request));
    }

    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponse> findById(@PathVariable UUID customerId) {
        return ApiResponse.success(customerService.findById(customerId));
    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> findAll() {
        return ApiResponse.success(customerService.findAll());
    }
}