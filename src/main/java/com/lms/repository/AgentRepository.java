package com.lms.repository;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long>{
	
	List<Agent> findByMaker(Long maker);
}
