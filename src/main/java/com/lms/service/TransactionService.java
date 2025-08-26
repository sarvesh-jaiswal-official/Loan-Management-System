package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.Transaction;
import com.lms.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	public List<Transaction> getAllTransaction() {
		return transactionRepository.findAll();
	}

	public Transaction getTransactionByID(Long id) {
		return transactionRepository.findById(id).orElse(null);
	}

	public List<Transaction> getTransactionByMaker(Long maker) {
		return transactionRepository.findByMaker(maker);
	}

	public void saveTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}

	public Transaction getTransactionByCid(Long id) {
		return transactionRepository.findByCid(id).orElse(null);
	}

	public Transaction getTransactionByLid(Long id) {
		return transactionRepository.findByLid(id).orElse(null);
	}

	public void deleteTransaction(Long id) {
		transactionRepository.deleteById(id);
	}

	public Long countAllRecords() {
		return transactionRepository.count();
	}
}
