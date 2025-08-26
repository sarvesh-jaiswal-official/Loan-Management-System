package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.Agent;
import com.lms.repository.AgentRepository;

@Service
public class AgentService {

	@Autowired
	private AgentRepository agentRepository;

	public List<Agent> getAgent() {
		return agentRepository.findAll();
	}

	public Agent getAgentByID(Long id) {
		return agentRepository.findById(id).orElse(null);
	}

	public List<Agent> getAgentByMaker(Long maker) {
		return agentRepository.findByMaker(maker);
	}

	public void saveAgent(Agent agent) {
		agentRepository.save(agent);
	}

	public List<Agent> getAllAgents() {
		return agentRepository.findAll();
	}

	public void deleteAgent(Long id) {
		agentRepository.deleteById(id);
	}

	public Long countAllRecords() {
		return agentRepository.count();
	}
}
