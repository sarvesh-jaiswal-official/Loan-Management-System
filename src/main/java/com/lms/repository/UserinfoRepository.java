package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.Userinfo;

public interface UserinfoRepository extends JpaRepository<Userinfo, Long> {

}
