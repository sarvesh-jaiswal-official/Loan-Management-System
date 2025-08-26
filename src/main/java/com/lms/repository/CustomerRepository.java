package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	List<Customer> findByKycst(String kycst);

	List<Customer> findByNameIgnoreCase(String name);
	
	List<Customer> findByNameContaining(String name);
	
	List<Customer> findByPhoneContaining(String phone);
	
	List<Customer> findByMaker(Long maker);
}
