package com.lms.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lms.model.Admin;
import com.lms.model.Agent;
import com.lms.service.AdminService;
import com.lms.service.AgentService;
import com.lms.service.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

	@Autowired
	private AdminService adminService;
	private AgentService agentService;

	public WebController(AdminService adminService, AgentService agentService) {
		super();
		this.adminService = adminService;
		this.agentService = agentService;
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpSession session) {

		SessionManager.sessionId = null;
		SessionManager.sessionName = null;
		request.getSession().invalidate();
		return "index";
	}

	@GetMapping("/alerts")
	public String alerts(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			
			if(SessionManager.accountType.equals("Admin Account")) {
				return "/pages/common/alerts";
			}else {
				return "/pages/agent_pages/alerts";
			}
			
		}
		return "index";
	}

	@GetMapping("/updateProfile")
	public String updateProfileReq(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Admin Account")) {
				Admin admin = adminService.getAdminByID(SessionManager.sessionId);
				model.addAttribute("admin", admin);
				return "/pages/admin/update_admin";
			} else {
				Agent agent = agentService.getAgentByID(SessionManager.sessionId);
				model.addAttribute("agent", agent);
				return "/pages/agent/update_agent";
			}
		}
		return "index";
	}

	@PostMapping("/updateProfile")
	public String updateProfile(@RequestParam("pic") MultipartFile pic, @ModelAttribute("admin") Admin admin,
			@ModelAttribute("agent") Agent agent, Model model) throws IOException {

		if (SessionManager.sessionId != null) {

			if (SessionManager.accountType.equals("Admin Account")) {

				Admin tempAdmin = adminService.getAdminByID(admin.getId());

				if (pic.getSize() != 0) {
					admin.setProfile(Base64.getEncoder().encodeToString(pic.getBytes()));
				} else {
					admin.setProfile(tempAdmin.getProfile());
				}

				adminService.saveUser(admin);
				model.addAttribute("session_id", SessionManager.sessionId);
				model.addAttribute("session_name", SessionManager.sessionName);
				model.addAttribute("acc_type", SessionManager.accountType);
				return "/pages/admin/admin_dashboard";

			} else {
				Agent tempAgent = agentService.getAgentByID(agent.getId());

				if (pic.getSize() != 0) {
					agent.setProfile(Base64.getEncoder().encodeToString(pic.getBytes()));
				} else {
					admin.setProfile(tempAgent.getProfile());
				}

				agentService.saveAgent(agent);
				model.addAttribute("session_id", SessionManager.sessionId);
				model.addAttribute("session_name", SessionManager.sessionName);
				model.addAttribute("acc_type", SessionManager.accountType);

				return "/pages/agent/agent_dashboard";
			}
		}
		return "index";
	}
	
}
