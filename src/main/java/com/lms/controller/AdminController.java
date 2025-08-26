package com.lms.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lms.model.Admin;
import com.lms.model.Agent;
import com.lms.model.Transaction;
import com.lms.model.Userinfo;
import com.lms.service.AdminService;
import com.lms.service.AgentService;
import com.lms.service.SessionManager;
import com.lms.service.TransactionService;
import com.lms.service.UserinfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;
	private UserinfoService userinfoService;
	private AgentService agentService;
	private TransactionService transactionService;

	public AdminController(AdminService adminService, UserinfoService userinfoService,
			TransactionService transactionService, AgentService agentService) {
		this.adminService = adminService;
		this.userinfoService = userinfoService;
		this.transactionService = transactionService;
		this.agentService = agentService;
	}

	@PostMapping("/adminRegister")
	public String register(@RequestParam("password") String password1, @RequestParam("password2") String password2, Model model, Admin admin, BindingResult bindingResult) {

		if (admin != null) {

			System.out.println(password1+" "+password2);
			
			
			
			if(!(password1.equals(password2))) {
				model.addAttribute("successMessage", "Both passwords should be same !");
				return "/pages/admin/admin_reg";
			}
			
			adminService.saveUser(admin);
			return "/pages/admin/admin_login";
		} else {
			model.addAttribute("successMessage", "Registration Failed !");
			return "/pages/admin/admin_reg";
		}
	}

	@PostMapping("/adminLogin")
	public String adminLoginDashboard(@ModelAttribute("admin") Admin admin,  Model model, HttpServletRequest request,
			HttpSession session, Userinfo userinfo1) {

		SessionManager.userCount = (long) 0;
		long id1 = admin.getId();
		String pass1 = admin.getPassword();
		Admin adminData = null;
		String pass2 = "";

		if (adminService.getAdminByID(id1) != null) {

			adminData = adminService.getAdminByID(id1);

			pass2 = adminData.getPassword();

		
			
			if (pass1.equals(pass2)) {
				request.getSession().setAttribute("CURRENT_ID", id1);
				request.getSession().setAttribute("CURRENT_NAME", adminData.getName());
				SessionManager.accountType = "Admin Account";
				SessionManager.profilePic = adminData.getProfile();
				SessionManager.sessionId = (Long) session.getAttribute("CURRENT_ID");
				SessionManager.sessionName = (String) session.getAttribute("CURRENT_NAME");

				model.addAttribute("session_id", SessionManager.sessionId);
				model.addAttribute("session_name", SessionManager.sessionName);
				model.addAttribute("acc_type", SessionManager.accountType);
				model.addAttribute("profile_pic", SessionManager.profilePic);

				Userinfo userinfo = userinfoService.getUserinfo(SessionManager.sessionId);

				if (userinfo != null) {
					model.addAttribute("disbursed", userinfo.getLoandis());
					model.addAttribute("outstand", userinfo.getOutstand());
					model.addAttribute("repayment", userinfo.getRepayment());
					model.addAttribute("client", userinfo.getClient());
				} else {
					
					userinfo1.setId(SessionManager.sessionId);
					userinfo1.setClient(0);
					userinfo1.setLoandis(0);
					userinfo1.setOutstand(0);
					userinfo1.setRepayment(0);
					userinfoService.saveUserinfo(userinfo1);
					model.addAttribute("disbursed", 0);
					model.addAttribute("outstand", 0);
					model.addAttribute("repayment", 0);
					model.addAttribute("client", 0);
				}
				return "/pages/admin/admin_dashboard";
			} else {
				model.addAttribute("successMessage", "Wrong Credintials !");
				return "/pages/admin/admin_login";
			}
		}
		model.addAttribute("successMessage", "Wrong Credintials !");
		return "/pages/admin/admin_login";
	}

	@GetMapping("/walletBal")
	public String walletBal(Model model) {

		if (SessionManager.sessionId != null) {
			
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			
			if (SessionManager.accountType.equals("Admin Account")) {
				Admin admin = adminService.getAdminByID(SessionManager.sessionId);
				model.addAttribute("bal", admin.getWalletbal());
				return "/pages/common/wallet_bal";
			} else {
				Agent agent = agentService.getAgentByID(SessionManager.sessionId);
				model.addAttribute("bal", agent.getWalletbal());
				return "/pages/agent_pages/wallet_bal";
			}
		}
		return "index";
	}

	@GetMapping("/adminLoginPage")
	public String adminLogin() {
		return "/pages/admin/admin_login";
	}

	@GetMapping("/adminRegPage")
	public String adminReg(Model model) {

		SessionManager.userCount = adminService.countAllRecords();
		model.addAttribute("session_id", SessionManager.sessionId);
		model.addAttribute("session_name", SessionManager.sessionName);
		model.addAttribute("acc_type", SessionManager.accountType);
		return "/pages/admin/admin_reg";
	}

	@GetMapping("/adminHomeTab")
	public String adminHomeTab(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			Userinfo userinfo = userinfoService.getUserinfo(SessionManager.sessionId);

			if (userinfo != null) {
				model.addAttribute("disbursed", userinfo.getLoandis());
				model.addAttribute("outstand", userinfo.getOutstand());
				model.addAttribute("repayment", userinfo.getRepayment());
				model.addAttribute("client", userinfo.getClient());
			} else {
				model.addAttribute("disbursed", 0);
				model.addAttribute("outstand", 0);
				model.addAttribute("repayment", 0);
				model.addAttribute("client", 0);
			}
			return "/pages/admin/admin_dashboard";
		}
		return "index";
	}

	@GetMapping("/addBalReq")
	public String addBalReq(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/add_bal";
			} else {
				return "/pages/common/add_bal";
			}
		}
		return "index";
	}

	@PostMapping("/addBal")
	public String addBalRes(Model model, @RequestParam("amt") double amt, @RequestParam("method") String method,
			Transaction transaction) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			
			SessionManager.userCount = transactionService.countAllRecords();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime datetime = LocalDateTime.now();
	
			transaction.setAmt(amt);
			transaction.setCid(0);
			transaction.setLid(0);
			transaction.setMethod(method);
			transaction.setMaker(SessionManager.sessionId);
			transaction.setDatetime(dtf.format(datetime));
			transaction.setType("credit");
			transactionService.saveTransaction(transaction);
			
			if (SessionManager.accountType.equals("Agent Account")) {
				Agent agent = agentService.getAgentByID(SessionManager.sessionId);
				agent.setWalletbal(amt+agent.getWalletbal());
				agentService.saveAgent(agent);
				model.addAttribute("bal", agent.getWalletbal());
				return "/pages/agent_pages/wallet_bal";
			} else {
				Admin admin = adminService.getAdminByID(SessionManager.sessionId);
				admin.setWalletbal(amt + admin.getWalletbal());
				adminService.saveUser(admin);
				model.addAttribute("bal", admin.getWalletbal());
				return "/pages/common/wallet_bal";
			}
		}
		return "index";
	}
	
	  static boolean isNumber(String input)
	    {
		  for(int a=0;a<input.length();a++)
			{
				if(a==0 && input.charAt(a) == '-')
					continue;
				if( !Character.isDigit(input.charAt(a)))
					return false;          	   
			}
		  return true;
	    }

}
