package com.dev.k.invoice.customer.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.dev.k.invoice.customer.entity.Customer;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("customerCodeで顧客を取得できる")
    void findByCustomerCode_shouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setCustomerCode("C0001");
        customer.setName("株式会社テスト");
        customer.setEmail("test@example.com");
        customer.setIsActive(true);

        customerRepository.save(customer);

        Optional<Customer> result = customerRepository.findByCustomerCode("C0001");

        assertThat(result).isPresent();
        assertThat(result.get().getCustomerCode()).isEqualTo("C0001");
        assertThat(result.get().getName()).isEqualTo("株式会社テスト");
    }

    @Test
    @DisplayName("存在するcustomerCodeならtrueを返す")
    void existsByCustomerCode_shouldReturnTrue() {
        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setCustomerCode("C0002");
        customer.setName("株式会社確認");
        customer.setEmail("check@example.com");
        customer.setIsActive(true);

        customerRepository.save(customer);

        boolean exists = customerRepository.existsByCustomerCode("C0002");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("存在しないcustomerCodeならfalseを返す")
    void existsByCustomerCode_shouldReturnFalse() {
        boolean exists = customerRepository.existsByCustomerCode("NOT_EXISTS");

        assertThat(exists).isFalse();
    }
}