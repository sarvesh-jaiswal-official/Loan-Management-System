package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.Admin;
import com.lms.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	public List<Admin> getAdmin() {
		return adminRepository.findAll();
	}

	public Admin getAdminByID(Long id) {
		return adminRepository.findById(id).orElse(null);
	}

	public void saveUser(Admin admin) {
		adminRepository.save(admin);
	}

	public Long countAllRecords() {
		return adminRepository.count();
	}

}
