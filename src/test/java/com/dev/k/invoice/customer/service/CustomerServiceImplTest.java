package com.dev.k.invoice.customer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dev.k.invoice.common.exception.BusinessException;
import com.dev.k.invoice.common.exception.ResourceNotFoundException;
import com.dev.k.invoice.customer.dto.CustomerCreateRequest;
import com.dev.k.invoice.customer.dto.CustomerResponse;
import com.dev.k.invoice.customer.dto.CustomerUpdateRequest;
import com.dev.k.invoice.customer.entity.Customer;
import com.dev.k.invoice.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    @DisplayName("顧客登録：正常系")
    void createCustomer_success() {

        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setCustomerCode("C0001");
        request.setName("テスト株式会社");
        request.setEmail("test@example.com");

        when(customerRepository.existsByCustomerCode("C0001")).thenReturn(false);

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(UUID.randomUUID());
        savedCustomer.setCustomerCode("C0001");
        savedCustomer.setName("テスト株式会社");
        savedCustomer.setEmail("test@example.com");
        savedCustomer.setIsActive(true);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponse result = customerService.create(request);

        assertThat(result.getCustomerCode()).isEqualTo("C0001");
        assertThat(result.getName()).isEqualTo("テスト株式会社");
        assertThat(result.getEmail()).isEqualTo("test@example.com");

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("顧客登録：customerCode重複")
    void createCustomer_duplicateCode() {

        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setCustomerCode("C0001");

        when(customerRepository.existsByCustomerCode("C0001")).thenReturn(true);

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(BusinessException.class);

        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("顧客取得：正常系")
    void findById_success() {

        UUID customerId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerCode("C0001");
        customer.setName("テスト会社");

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.findById(customerId);

        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getCustomerCode()).isEqualTo("C0001");
    }

    @Test
    @DisplayName("顧客取得：存在しない")
    void findById_notFound() {

        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById(customerId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("顧客更新：正常系")
    void updateCustomer_success() {

        UUID customerId = UUID.randomUUID();

        Customer existing = new Customer();
        existing.setCustomerId(customerId);
        existing.setCustomerCode("C0001");
        existing.setName("旧会社名");
        existing.setEmail("old@example.com");
        existing.setIsActive(true);

        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setName("新会社名");
        request.setEmail("new@example.com");

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(existing));

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(existing);

        CustomerResponse result = customerService.update(customerId, request);

        assertThat(result.getName()).isEqualTo("新会社名");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
    }
}