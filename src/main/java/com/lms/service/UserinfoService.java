 package com.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.Userinfo;
import com.lms.repository.UserinfoRepository;

@Service
public class UserinfoService {

	@Autowired
	private UserinfoRepository userinfoRepository;
	
	public void saveUserinfo(Userinfo userinfo) {
		userinfoRepository.save(userinfo);
	}
	
	public Userinfo getUserinfo(Long id) {
		return userinfoRepository.findById(id).orElse(null);
	}
	
}
