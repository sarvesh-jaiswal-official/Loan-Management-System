package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.Customer;
import com.lms.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public List<Customer> getAllCustomer() {
		return customerRepository.findAll();
	}

	public Customer getCustomerByID(Long id) {
		return customerRepository.findById(id).orElse(null);
	}

	public List<Customer> getCustomerByMaker(Long maker) {
		return customerRepository.findByMaker(maker);
	}

	public void saveCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}

	public List<Customer> getCustomerByKycst(String kycst) {
		return customerRepository.findByKycst(kycst);
	}

	public List<Customer> getCustomerByName(String name) {
		return customerRepository.findByNameContaining(name);
	}

	public List<Customer> getCustomerByPhone(String phone) {
		return customerRepository.findByPhoneContaining(phone);
	}

	public Long countAllRecords() {
		return customerRepository.count();
	}
}
