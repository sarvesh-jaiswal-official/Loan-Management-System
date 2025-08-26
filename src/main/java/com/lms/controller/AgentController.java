package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lms.model.Agent;
import com.lms.service.AgentService;
import com.lms.service.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AgentController {

	@Autowired
	private AgentService agentService;

	public AgentController(AgentService agentService) {

		this.agentService = agentService;

	}

	@PostMapping("/agentLogin")
	public String agentLoginDashboard(@ModelAttribute("agent") Agent agent, Model model, HttpServletRequest request,
			HttpSession session) {

		long id1 = agent.getId();
		String pass1 = agent.getPassword();
		Agent agentData = null;
		String pass2 = "";

		if (agentService.getAgentByID(id1) != null) {

			agentData = agentService.getAgentByID(id1);

			pass2 = agentData.getPassword();

			if (pass1.equals(pass2)) {
				request.getSession().setAttribute("CURRENT_ID", id1);
				request.getSession().setAttribute("CURRENT_NAME", agentData.getName());
				// System.out.println("Session : "+session.getAttribute("CURRENT_ID"));
				SessionManager.accountType = "Agent Account";
				SessionManager.profilePic = agentData.getProfile();
				SessionManager.sessionId = (Long) session.getAttribute("CURRENT_ID");
				SessionManager.sessionName = (String) session.getAttribute("CURRENT_NAME");
				model.addAttribute("session_id", SessionManager.sessionId);
				model.addAttribute("session_name", SessionManager.sessionName);
				model.addAttribute("acc_type", SessionManager.accountType);
				model.addAttribute("profile_pic", SessionManager.profilePic);
				return "/pages/agent/agent_dashboard";
			} else {
				model.addAttribute("successMessage", "Wrong Credintials !");
				return "/pages/agent/agent_login";
			}
		}
		model.addAttribute("successMessage", "Wrong Credintials !");
		return "/pages/agent/agent_login";
	}

	@PostMapping("/agentRegister")
	public String agentRegister(@RequestParam("password2") String password2, Model model, Agent agent, BindingResult bindingResult) {

		if (SessionManager.sessionId != null) {
			if (agent != null) {
				
				if(!(agent.getPassword().equals(password2))) {
					model.addAttribute("successMessage", "Both passwords should be same !");
					return "/pages/agent/agent_tab";
				}else {
					agent.setMaker(SessionManager.sessionId);
					agentService.saveAgent(agent);
					model.addAttribute("session_id", SessionManager.sessionId);
					model.addAttribute("session_name", SessionManager.sessionName);
					model.addAttribute("acc_type", SessionManager.accountType);
					model.addAttribute("successMessage", "Registered Successfully !");
					return "/pages/agent/agent_tab";
				}
				
			} else {
				model.addAttribute("session_id", SessionManager.sessionId);
				model.addAttribute("session_name", SessionManager.sessionName);
				model.addAttribute("acc_type", SessionManager.accountType);
				model.addAttribute("successMessage", "Registration Failed !");
				return "/pages/agent/agent_tab";
			}
		}
		return "index";
	}

	@GetMapping("/agentTab")
	public String agentTab(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			SessionManager.userCount = agentService.countAllRecords();
			return "/pages/agent/agent_tab";
		}
		return "index";
	}

	@GetMapping("/manageAgent")
	public String manageAgent(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("agents", agentService.getAgentByMaker(SessionManager.sessionId));
			return "/pages/agent/manage_agent";
		}
		return "index";
	}

	@GetMapping("/agent/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {

		if (SessionManager.sessionId != null) {
			Agent agent = agentService.getAgentByID(id);
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("agent", agent);
			return "/pages/agent/edit_agent";
		}
		return "index";
	}

	@PostMapping("/agent/edit/{id}")
	public String updateAgent(@PathVariable Long id, @ModelAttribute("agent") Agent agent, Model model) {

		if (SessionManager.sessionId != null) {
			agent.setId(id);
			agentService.saveAgent(agent);
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("agents", agentService.getAgentByMaker(SessionManager.sessionId));
			return "/pages/agent/manage_agent";
		}
		return "index";
	}

	@GetMapping("/agent/delete/{id}")
	public String deleteAgent(@PathVariable Long id, Model model) {

		if (SessionManager.sessionId != null) {
			agentService.deleteAgent(id);
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("agents", agentService.getAgentByMaker(SessionManager.sessionId));
			return "/pages/agent/manage_agent";
		}
		return "index";
	}

	@GetMapping("/agentLoginPage")
	public String agentLoginPage() {
		return "/pages/agent/agent_login";
	}

	@GetMapping("/agentLogin")
	public String agentLogin() {
		return "/pages/agent/agent_login";
	}

	@GetMapping("/agentHomeTab")
	public String agentHomeTab(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			return "/pages/agent/agent_dashboard";
		}
		return "index";
	}
}
