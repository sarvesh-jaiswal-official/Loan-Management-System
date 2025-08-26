package com.lms.repository;

import java.util.List; 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.Loan;


public interface LoanRepository extends JpaRepository<Loan, Long>{

	Optional<Loan> findByCustid(Long id);

	List<Loan> findByMaker(Long maker);

	List<Loan> findByApprovest(String approvest);
	
	List<Loan> findByMakerAndEmiop(Long maker, String emiop);
}
