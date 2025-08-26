package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.Loan;
import com.lms.repository.LoanRepository;

@Service
public class LoanService {

	@Autowired
	private LoanRepository loanRepository;

	public List<Loan> getAllLoan() {
		return loanRepository.findAll();
	}

	public Loan getLoanByID(Long id) {
		return loanRepository.findById(id).orElse(null);
	}

	public List<Loan> getLoanByMaker(Long maker) {
		return loanRepository.findByMaker(maker);
	}

	public List<Loan> getLoanByApprovest(String approvest) {
		return loanRepository.findByApprovest(approvest);
	}

	public void saveLoan(Loan Loan) {
		loanRepository.save(Loan);
	}

	public List<Loan> getLoanByMakerAndEmiop(Long maker, String emiop) {
		return loanRepository.findByMakerAndEmiop(maker, emiop);
	}

	public Loan getLoanByCustid(Long id) {
		return loanRepository.findByCustid(id).orElse(null);
	}

	public void deleteLoan(Long id) {
		loanRepository.deleteById(id);
	}

	public Long countAllRecords() {
		return loanRepository.count();
	}
}
