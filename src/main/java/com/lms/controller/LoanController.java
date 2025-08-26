package com.lms.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lms.model.Admin;
import com.lms.model.Agent;
import com.lms.model.Loan;
import com.lms.model.Transaction;
import com.lms.model.Userinfo;
import com.lms.service.AdminService;
import com.lms.service.AgentService;
import com.lms.service.ExportPdfService;
import com.lms.service.LoanService;
import com.lms.service.SessionManager;
import com.lms.service.TransactionService;
import com.lms.service.UserinfoService;

@Controller
public class LoanController {

	@Autowired
	private LoanService loanService;
	private UserinfoService userinfoService;
	private TransactionService transactionService;
	private AgentService agentService;
	private AdminService adminService;
	private ExportPdfService pdfService;

	private String pdf_method = "";
	private String pdf_data = "";

	public LoanController(LoanService loanService, ExportPdfService pdfService, UserinfoService userinfoService,
			TransactionService transactionService, AgentService agentService, AdminService adminService) {
		super();
		this.loanService = loanService;
		this.pdfService = pdfService;
		this.userinfoService = userinfoService;
		this.transactionService = transactionService;
		this.agentService = agentService;
		this.adminService = adminService;
	}

	@GetMapping("/appliedLoan")
	public String appliedLoanReq(Model model) {

		if (SessionManager.sessionId != null) {
			if (loanService.getAllLoan() != null) {
				pdf_method = "getLoanByApprovestDisbursed";
				model.addAttribute("loans", loanService.getLoanByMaker(SessionManager.sessionId));
				// System.out.println("data found ");
			} else {
				model.addAttribute("successMessage", "data not found !");
			}
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/applied_loan";
			} else {
				return "/pages/loan/applied_loan";
			}
		}
		return "index";
	}

	@GetMapping("/approveLoan")
	public String approveLoanReq(Model model) {

		if (SessionManager.sessionId != null) {
			if (loanService.getAllLoan() != null) {
				pdf_method = "getLoanByApprovestNA";
				model.addAttribute("loans", loanService.getLoanByApprovest("NA"));
				// System.out.println("data found ");
			} else {
				model.addAttribute("successMessage", "data not found !");
			}
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			return "/pages/loan/approve_loan";
		}
		return "index";
	}

	@GetMapping("/loan/approve/{id}/{cid}")
	public String appliedLoanRes(@PathVariable Long id, @PathVariable Long cid, Model model,Userinfo userinfo) {

		if (SessionManager.sessionId != null) {
			LocalDate localDate = LocalDate.of(2024, Calendar.MONTH, 05);
			localDate = localDate.plusMonths(2);

			Loan loan = loanService.getLoanByID(id);
			loan.setApprovest("Disbursed");
			loan.setDuedate(String.valueOf(localDate));
			loan.setLoanst("Active");
			loanService.saveLoan(loan);

			userinfo = userinfoService.getUserinfo(SessionManager.sessionId);
			if (userinfo != null) {
				userinfo.setLoandis(userinfo.getLoandis() + loan.getAmt());
				userinfo.setOutstand(userinfo.getLoandis()-userinfo.getRepayment());
			}

			pdf_method = "getLoanByApprovestNA";
			model.addAttribute("loans", loanService.getLoanByApprovest("NA"));
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			return "/pages/loan/approve_loan";
		}
		return "index";
	}

	@GetMapping("/loanSearch")
	public String loanSearchReq(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/search_loan";
			} else {
				return "/pages/loan/search_loan";
			}
		}
		return "index";
	}

	@PostMapping("/loanSearch")
	public String custSearchRes(@RequestParam("data") String data, @RequestParam("searchby") String searchby,
			Model model) {

		if (SessionManager.sessionId != null) {
			if (searchby.equals("id")) {
				pdf_method = "getLoanByID";
				model.addAttribute("loans", loanService.getLoanByID(Long.parseLong(data)));
			} else if (searchby.equals("cust")) {
				pdf_method = "getLoanByCustid";
				model.addAttribute("loans", loanService.getLoanByCustid(Long.parseLong(data)));
			} else {
				model.addAttribute("successMessage", "Records not found !");
			}
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/applied_loan";
			} else {
				return "/pages/loan/applied_loan";
			}
		}
		return "index";
	}

	@GetMapping("/EMICal")
	public String EMICalRes(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("amt", "Principal Amount");
			model.addAttribute("interest", "Interest %");
			model.addAttribute("months", "Number of Months");
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/emi_calc";
			} else {
				return "/pages/loan/emi_calc";
			}
		}
		return "index";
	}

	@PostMapping("/EMICalResult")
	public String EMICalReq(@RequestParam("amt") double amt, @RequestParam("interest") double interest,
			@RequestParam("months") int months, Model model) {

		if (SessionManager.sessionId != null) {
			double monthlyInterestRate = interest / (12 * 100);
			double emi = calculateEMI(amt, monthlyInterestRate, months);

			DecimalFormat df = new DecimalFormat("#0.00");
			String formattedNumber = df.format(emi);
			double finalemi = Double.parseDouble(formattedNumber);

			model.addAttribute("amt", amt);
			model.addAttribute("interest", interest);
			model.addAttribute("months", months);
			model.addAttribute("result", finalemi);
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/emi_calc";
			} else {
				return "/pages/loan/emi_calc";
			}
		}
		return "index";
	}

	@GetMapping("/filterLoan")
	public String filterLoanReq(Model model) {

		if (SessionManager.sessionId != null) {

			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/filter_loan";
			} else {
				return "/pages/loan/filter_loan";
			}
		}
		return "index";
	}

	@PostMapping("/filterLoan")
	public String filterLoan(@RequestParam("filterby") String filterby, Model model) {

		if (SessionManager.sessionId != null) {
			
			if (filterby.equals("daily")) {
				pdf_method = "getLoanByMakerAndEmiop";
				pdf_data = "daily";
				model.addAttribute("loans", loanService.getLoanByMakerAndEmiop(SessionManager.sessionId, "daily"));
			} else if (filterby.equals("monthly")) {
				pdf_method = "getLoanByMakerAndEmiop";
				pdf_data = "monthly";
				model.addAttribute("loans", loanService.getLoanByMakerAndEmiop(SessionManager.sessionId, "monthly"));
			} else {
				model.addAttribute("successMessage", "Records not found !");
			}
			
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/applied_loan";
			} else {
				return "/pages/loan/applied_loan";
			}
		}
		return "index";
	}

	@GetMapping("/loan/addFees/{id}")
	public String addFeesReq(@PathVariable Long id, Model model) {

		if (SessionManager.sessionId != null) {
			pdf_method = "getLoanByApprovestNA";
			model.addAttribute("loans", loanService.getLoanByApprovest("NA"));
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("id", id);
			if (SessionManager.accountType.equals("Admin Account")) {
				return "/pages/loan/late_fees";
			} else {
				return "/pages/agent_pages/late_fees";
			}
		}
		return "index";
	}

	@PostMapping("/loan/addFees/{id}")
	public String addFees(@PathVariable Long id, @RequestParam("fees") Long fees, Model model) {

		if (SessionManager.sessionId != null) {

			Loan loan = loanService.getLoanByID(id);

			loan.setProcfees(loan.getProcfees() + fees);
			loanService.saveLoan(loan);
			pdf_method = "getLoanByMaker";
			model.addAttribute("loans", loanService.getLoanByMaker(SessionManager.sessionId));
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			if (SessionManager.accountType.equals("Admin Account")) {
				return "/pages/loan/applied_loan";
			} else {
				return "/pages/agent_pages/applied_loan";
			}
		}
		return "index";
	}

	@GetMapping("/loan/pay/{id}")
	public String PayReq(@PathVariable Long id, Model model) {

		if (SessionManager.sessionId != null) {

			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("id", id);

			if (SessionManager.accountType.equals("Admin Account")) {
				return "/pages/transaction/pay";
			} else {
				return "/pages/agent_pages/pay";
			}
		}
		return "index";
	}

	@PostMapping("/loan/pay/{id}")
	public String PayRes(@PathVariable Long id, @RequestParam("amt") double amt, @RequestParam("method") String method,
			Transaction transaction, Model model, RedirectAttributes redirAttrs,Userinfo userinfo) {

		if (SessionManager.sessionId != null) {
			System.out.println("Entry in session");
			SessionManager.userCount = transactionService.countAllRecords();

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime datetime = LocalDateTime.now();

			Loan loan = loanService.getLoanByID(id);
			if (loan.getPendamt() == 0) {
				loan.setLoanst("Closed");
			}
			if (!(loan.getPendamt() < amt)) {
				System.out.println("Entry : amt < pend " + loan.getPendamt() + " " + amt);

				loan.setPendamt(loan.getPendamt() - amt);
				if (loan.getPendamt() == 0) {
					loan.setLoanst("Closed");
				}
				loanService.saveLoan(loan);

				transaction.setAmt(amt);
				transaction.setCid(loan.getCustid());
				transaction.setLid(loan.getId());
				transaction.setMethod(method);
				transaction.setMaker(loan.getMaker());
				transaction.setDatetime(dtf.format(datetime));
				transaction.setType("debit");
				transactionService.saveTransaction(transaction);

				userinfo = userinfoService.getUserinfo(SessionManager.sessionId);
				if (userinfo != null) {
					userinfo.setOutstand(userinfo.getOutstand() - amt);
					userinfo.setRepayment(userinfo.getRepayment() + amt);
				}
				pdf_method = "getLoanByMaker";
				model.addAttribute("loans", loanService.getLoanByMaker(SessionManager.sessionId));
				model.addAttribute("session_id", SessionManager.sessionId);
				model.addAttribute("session_name", SessionManager.sessionName);
				model.addAttribute("acc_type", SessionManager.accountType);

				if (SessionManager.accountType.equals("Admin Account")) {
					System.out.println("Entry in: Admin acc");
					if (method.equals("wallet")) {
						System.out.println("admin : wallet");
						Admin admin = adminService.getAdminByID(SessionManager.sessionId);

						if (admin.getWalletbal() >= amt) {
							admin.setWalletbal(admin.getWalletbal() - amt);
							adminService.saveUser(admin);
						} else {
							redirAttrs.addFlashAttribute("message", "insufficient wallet bal !");
							return "redirect:/loan/pay/" + id;
						}
					}
					return "/pages/loan/applied_loan";
					
				} else {
					if (method.equals("wallet")) {
						System.out.println("agent : wallet");
						Agent agent = agentService.getAgentByID(SessionManager.sessionId);

						if (agent.getWalletbal() >= amt) {
							agent.setWalletbal(agent.getWalletbal() - amt);
							agentService.saveAgent(agent);
						} else {
							redirAttrs.addFlashAttribute("message", "insufficient wallet bal !");
							return "redirect:/loan/pay/" + id;
						}
					}
					return "/pages/agent_pages/applied_loan";
				}
			} else {
				redirAttrs.addFlashAttribute("message", "You are not pay more than pending balance !");
				return "redirect:/loan/pay/" + id;
			}
		}
		System.out.println("Failed");
		return "index";
	}

	@GetMapping("/exportLoanPdf")
	public ResponseEntity<?> exportLoanPdf() throws IOException {

		ByteArrayInputStream pdf = pdfService.exportLoanPdf(pdf_method, pdf_data);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "inline:file=loan-records.pdf");
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdf));
	}

	public static double calculateEMI(double principalAmount, double monthlyInterestRate, int numberOfMonths) {
		// Formula to calculate EMI
		double emi = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfMonths))
				/ (Math.pow(1 + monthlyInterestRate, numberOfMonths) - 1);
		return emi;
	}

}
