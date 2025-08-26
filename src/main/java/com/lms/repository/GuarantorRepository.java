package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.Guarantor;

public interface GuarantorRepository extends JpaRepository<Guarantor, Long>{

	List<Guarantor> findByCid(Long cid);

	Guarantor findByGcount(Long cid);
}
