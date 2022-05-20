package com.teethcare.service;

import com.teethcare.model.entity.Customer;
import com.teethcare.repository.AccountRepository;
import com.teethcare.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImp implements CRUDService<Customer> {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(@Valid Customer customer) {
//        if (accountRepository.findAccountByUsername(customer.getUsername()) != null) {
            customer.setId(null);
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            return customerRepository.save(customer);
//        } else {
//            return (Customer) accountRepository.findAccountByUsername(customer.getUsername());
//        }
    }

    @Override
    public ResponseEntity delete(Integer id) {
        Optional<Customer> CustomerData = customerRepository.findById(id);
        if (CustomerData.isPresent()) {
            Customer customer = CustomerData.get();
            customer.setStatus(false);
            return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
