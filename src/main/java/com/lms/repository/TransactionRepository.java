package com.lms.repository;

import java.util.List; 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	Optional<Transaction> findByCid(Long id);

	Optional<Transaction> findByLid(Long id);

	List<Transaction> findByMaker(Long maker);
}
