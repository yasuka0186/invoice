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

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        if (customerRepository.existsByCustomerCode(request.getCustomerCode())) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Customer code already exists.");
        }

        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setCustomerCode(request.getCustomerCode());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setIsActive(true);

        Customer savedCustomer = customerRepository.save(customer);
        return toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse update(UUID customerId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return toResponse(updatedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
        return toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
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