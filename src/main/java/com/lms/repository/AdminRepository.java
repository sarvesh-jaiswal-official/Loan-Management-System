package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	
}
