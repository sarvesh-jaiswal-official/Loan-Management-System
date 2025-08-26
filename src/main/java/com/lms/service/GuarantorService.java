package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.Guarantor;
import com.lms.repository.GuarantorRepository;

@Service
public class GuarantorService {

	@Autowired
	private GuarantorRepository guarantorRepository;

	public Guarantor getGuarantorById(Long id) {
		return guarantorRepository.findById(id).orElse(null);
	}

	public List<Guarantor> getGuarantorByCid(Long cid) {
		return guarantorRepository.findByCid(cid);
	}

	public Guarantor getGuarantorByGcount(Long cid) {
		return guarantorRepository.findByGcount(cid);
	}

	public void saveGuarantor(Guarantor guarantor) {
		guarantorRepository.save(guarantor);
	}

	public Long countAllRecords() {
		return guarantorRepository.count();
	}
	
}
