package com.dev.k.invoice.customer.service;

import java.util.List;
import java.util.UUID;

import com.dev.k.invoice.customer.dto.CustomerCreateRequest;
import com.dev.k.invoice.customer.dto.CustomerResponse;
import com.dev.k.invoice.customer.dto.CustomerUpdateRequest;

public interface CustomerService {

    CustomerResponse create(CustomerCreateRequest request);

    CustomerResponse update(UUID customerId, CustomerUpdateRequest request);

    CustomerResponse findById(UUID customerId);

    List<CustomerResponse> findAll();
}