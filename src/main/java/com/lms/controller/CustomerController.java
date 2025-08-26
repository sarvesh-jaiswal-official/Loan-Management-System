package com.lms.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lms.model.Customer;
import com.lms.model.Guarantor;
import com.lms.model.Loan;
import com.lms.model.Userinfo;
import com.lms.service.CustomerService;
import com.lms.service.ExportPdfService;
import com.lms.service.GuarantorService;
import com.lms.service.LoanService;
import com.lms.service.SessionManager;
import com.lms.service.UserinfoService;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	private LoanService loanService;
	private GuarantorService guarantorService;
	private UserinfoService userinfoService;
	private ExportPdfService pdfService;

	private String pdf_method = "";
	@SuppressWarnings("unused")
	private String pdf_data = "";

	public CustomerController(CustomerService customerService, LoanService loanService, ExportPdfService pdfService,
			GuarantorService guarantorService, UserinfoService userinfoService) {
		super();
		this.customerService = customerService;
		this.loanService = loanService;
		this.pdfService = pdfService;
		this.guarantorService = guarantorService;
		this.userinfoService = userinfoService;
	}

	@GetMapping("/custDetails")
	public String getAllCust(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			pdf_data = "getCustomerByMaker";
			model.addAttribute("customers", customerService.getCustomerByMaker(SessionManager.sessionId));

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_details";
			} else {
				return "/pages/customer/cust_details";
			}
		}
		return "index";
	}

	@PostMapping("/custAdd")
	public String custAdd(@RequestParam("customer") MultipartFile[] files, RedirectAttributes redirectAttributes,
			Model model, Customer customer,Userinfo userinfo) throws IOException {

		if (SessionManager.sessionId != null) {

			userinfo = userinfoService.getUserinfo(SessionManager.sessionId);
			if (userinfo != null) {
				userinfo.setClient(userinfo.getClient() + 1);
			}

			customer.setAadhaar(Base64.getEncoder().encodeToString(files[0].getBytes()));
			customer.setPan(Base64.getEncoder().encodeToString(files[1].getBytes()));
			customer.setPhoto(Base64.getEncoder().encodeToString(files[2].getBytes()));
			customer.setKycst("NA");
			customer.setRejrs("NA");
			customer.setCreditsc(600);
			customer.setLoans(0);
			customer.setMaker(SessionManager.sessionId);
			customerService.saveCustomer(customer);
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("successMessage", "Customer Added !");

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_tab";
			} else {
				return "/pages/customer/cust_tab";
			}
		}
		return "index";
	}

	@GetMapping("/custPage")
	public String custTabPage(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			// model.addAttribute("customers", customerService.getAllCustomer());
			model.addAttribute("acc_type", SessionManager.accountType);

			SessionManager.userCount = customerService.countAllRecords();

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_tab";
			} else {
				return "/pages/customer/cust_tab";
			}
		}
		return "index";
	}

	@GetMapping("/doc/{id}/{doc}")
	public String getDocs(@PathVariable Long id, @PathVariable Long doc, Model model) {

		if (SessionManager.sessionId != null) {
			Customer customer = customerService.getCustomerByID(id);
			if (doc == 1) {
				model.addAttribute("docs", customer.getAadhaar());
			} else if (doc == 2) {
				model.addAttribute("docs", customer.getPan());
			} else {
				model.addAttribute("docs", customer.getPhoto());
			}
			return "/pages/common/review_docs";
		}
		return "index";
	}

	@GetMapping("/customer/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {

		if (SessionManager.sessionId != null) {
			pdf_method = "getCustomerByID";
			pdf_data = id.toString();
			Customer customer = customerService.getCustomerByID(id);
			model.addAttribute("customer", customer);
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			// tempCustomer = customer;
			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_edit";
			} else {
				return "/pages/customer/cust_edit";
			}
		}
		return "index";
	}

	@PostMapping("/customer/edit/{id}")
	public String updateCustomer(@RequestParam("customer") MultipartFile[] files, @PathVariable Long id,
			@ModelAttribute("customer") Customer customer, Model model) throws IOException {

		if (SessionManager.sessionId != null) {
			customer.setId(id);
			Customer tempCustomer = customerService.getCustomerByID(id);

			if (files[0].getSize() != 0) {
				customer.setAadhaar(Base64.getEncoder().encodeToString(files[0].getBytes()));
			} else {
				customer.setAadhaar(tempCustomer.getAadhaar());
			}

			if (files[1].getSize() != 0) {
				customer.setPan(Base64.getEncoder().encodeToString(files[1].getBytes()));
			} else {
				customer.setPan(tempCustomer.getPan());
			}

			if (files[2].getSize() != 0) {
				customer.setPhoto(Base64.getEncoder().encodeToString(files[2].getBytes()));
			} else {
				customer.setPhoto(tempCustomer.getPhoto());
			}

			customerService.saveCustomer(customer);
			pdf_method = "getCustomerByMaker";
			model.addAttribute("customers", customerService.getCustomerByMaker(SessionManager.sessionId));
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_details";
			} else {
				return "/pages/customer/cust_details";
			}
		}
		return "index";
	}

	@GetMapping("/customer/delete/{id}")
	public String deleteCustomer(@PathVariable Long id, Model model) {

		if (SessionManager.sessionId != null) {
			customerService.deleteCustomer(id);
			pdf_method = "getCustomerByMaker";
			model.addAttribute("customers", customerService.getCustomerByMaker(SessionManager.sessionId));
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_details";
			} else {
				return "/pages/customer/cust_details";
			}
		}
		return "index";
	}

	@GetMapping("/custSearch")
	public String custSearch(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/search_cust";
			} else {
				return "/pages/customer/search_cust";
			}
		}
		return "index";
	}

	@PostMapping("/custSearch")
	public String custSearchRes(@RequestParam("data") String data, @RequestParam("searchby") String searchby,
			Model model) {

		if (SessionManager.sessionId != null) {
			if (searchby.equals("id")) {
				pdf_method = "getCustomerByID";
				pdf_data = data;
				model.addAttribute("customers", customerService.getCustomerByID(Long.parseLong(data)));
			} else if (searchby.equals("name")) {
				pdf_method = "getCustomerByName";
				pdf_data = data;
				model.addAttribute("customers", customerService.getCustomerByName(data));
			} else if (searchby.equals("phone")) {
				pdf_method = "getCustomerByPhone";
				pdf_data = data;
				model.addAttribute("customers", customerService.getCustomerByPhone(data));
			} else {
				model.addAttribute("successMessage", "Records not found !");
			}
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_details";
			} else {
				return "/pages/customer/cust_details";
			}
		}
		return "index";
	}

	@GetMapping("/customer/applyLoan/{id}")
	public String applyLoan(@PathVariable Long id, Model model, RedirectAttributes redirAttrs) {

		if (SessionManager.sessionId != null) {

			Customer customer = customerService.getCustomerByID(id);
			if (customer.getKycst().equals("Approved") && customer.getGcount()==2) {

				model.addAttribute("session_id", SessionManager.sessionId);
				model.addAttribute("session_name", SessionManager.sessionName);
				model.addAttribute("acc_type", SessionManager.accountType);
				SessionManager.userCount = loanService.countAllRecords();
				model.addAttribute("cust_id", id);

				if (SessionManager.accountType.equals("Agent Account")) {
					return "/pages/agent_pages/apply_loan";
				} else {
					return "/pages/loan/apply_loan";
				}
			} else {
				redirAttrs.addFlashAttribute("message", "your e-kyc not approved yet OR guarantor not added");
				return "redirect:/custDetails";
			}
		}
		return "index";
	}

	@PostMapping("/customer/applyLoan/{id}")
	public String custAdd(@PathVariable Long id, Model model, Loan loan) {

		if (SessionManager.sessionId != null) {
			double per = loan.getAmt() * 3 / 100;
			double finalamt = (loan.getAmt() + per + 1000);
			// System.out.println("Amount : "+finalamt);
			double emiamt = loan.getAmt() / loan.getDuration();
			DecimalFormat df = new DecimalFormat("#0.00");
			String formattedNumber = df.format(emiamt);
			double finalemi = Double.parseDouble(formattedNumber);

			loan.setCustid(id);
			LocalDate currentDate = LocalDate.now();
			loan.setDoa(currentDate.toString());
			loan.setProcfees(1000);
			loan.setInterest(3);
			loan.setFinalamt(finalamt);
			loan.setPendamt(finalamt);
			loan.setLoanst("NA");
			loan.setApprovest("NA");
			loan.setDuedate("NA");
			loan.setEmiamt(finalemi);
			loan.setMaker(SessionManager.sessionId);
			loanService.saveLoan(loan);
			model.addAttribute("successMessage", "Apply Successfully !");
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/apply_loan";
			} else {
				return "/pages/loan/apply_loan";
			}
		}
		return "index";
	}

	@GetMapping("/customer/ekyc/{id}")
	public String ekycCustForm(@PathVariable Long id, Model model) {

		if (SessionManager.sessionId != null) {
			Customer customer = customerService.getCustomerByID(id);
			model.addAttribute("customer", customer);
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			return "/pages/ekyc/kyc_page";
		}
		return "index";
	}

	@PostMapping("/customer/ekyc/{id}")
	public String ekycCust(@PathVariable Long id, @ModelAttribute("customer") Customer customer, Model model) {

		if (SessionManager.sessionId != null) {
			customer.setId(id);
			customerService.saveCustomer(customer);
			pdf_method = "getAllCustomer";
			model.addAttribute("customers", customerService.getAllCustomer());
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			return "/pages/ekyc/ekyc_tab";
		}
		return "index";
	}

	@GetMapping("/ekycTab")
	public String ekycTab(Model model) {

		if (SessionManager.sessionId != null) {
			pdf_method = "getCustomerByKycst";
			pdf_data = "NA";
			model.addAttribute("customers", customerService.getCustomerByKycst("NA"));
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			return "/pages/ekyc/ekyc_tab";
		}
		return "index";
	}

	@GetMapping("/ekycCompleted")
	public String ekycCompleted(Model model) {

		if (SessionManager.sessionId != null) {
			pdf_data = "Done";
			model.addAttribute("customers", customerService.getCustomerByKycst("Approved"));
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			return "/pages/ekyc/kyc_completed";
		}
		return "index";
	}

	@GetMapping("/custTab")
	public String custTab(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			SessionManager.userCount = customerService.countAllRecords();

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/cust_tab";
			} else {
				return "/pages/customer/cust_tab";
			}
		}
		return "index";
	}

	@GetMapping("/exportCustomerPdf")
	public ResponseEntity<?> exportCustomerPdf() throws IOException {

		ByteArrayInputStream pdf = pdfService.exportCustomerPdf(pdf_method);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "inline;file=customer-records.pdf");
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdf));
	}

	@GetMapping("/customer/addGuarantor/{cid}")
	public String addGuarantor(@PathVariable Long cid, Model model) {

		if (SessionManager.sessionId != null) {

			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);
			model.addAttribute("cid", cid);
			SessionManager.userCount = guarantorService.countAllRecords();
			return "/pages/guarantor/add_guarantor";
		}
		return "index";
	}

	@PostMapping("/customer/addGuarantor/{cid}")
	public String addGuarantorRes(@RequestParam("guarantor") MultipartFile[] files,
			RedirectAttributes redirectAttributes, @PathVariable("cid") Long cid,
			@ModelAttribute("guarantor") Guarantor guarantor, Model model) throws IOException {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			Customer customer = customerService.getCustomerByID(cid);

			if (customer.getGcount() == 2) {
				model.addAttribute("successMessage", "Cannot add Guarantor more than 2 !");
				return "/pages/guarantor/add_guarantor";
			} else {
				guarantor.setAadhaar(Base64.getEncoder().encodeToString(files[0].getBytes()));
				guarantor.setPan(Base64.getEncoder().encodeToString(files[1].getBytes()));
				guarantor.setCheque(Base64.getEncoder().encodeToString(files[2].getBytes()));
				customer.setGcount(customer.getGcount() + 1);
				guarantor.setCid(cid);
				guarantorService.saveGuarantor(guarantor);
				model.addAttribute("successMessage", "Guarantor Added !");
				return "/pages/guarantor/add_guarantor";
			}
		} else {
			return "index";
		}

	}

	@GetMapping("/guarantor/{cid}/{g}")
	public String reviewGuarantor(@PathVariable Long cid, @PathVariable int g, Model model, RedirectAttributes redirAttrs) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			ArrayList<Guarantor> guarantors = (ArrayList<Guarantor>) guarantorService.getGuarantorByCid(cid);

			if (guarantors.size()==2) {
				
				if (g == 1) {
					model.addAttribute("guarantor", guarantorService.getGuarantorById(guarantors.get(0).getId()));
				} else {
					model.addAttribute("guarantor", guarantorService.getGuarantorById(guarantors.get(1).getId()));
				}
				return "/pages/guarantor/guarantor_review";
			}else {
				redirAttrs.addFlashAttribute("message", "No guarantor added");
				return "redirect:/custDetails";
			}
		}
		return "index";
	}

	@GetMapping("/gdoc/{id}/{doc}")
	public String getGDocs(@PathVariable Long id, @PathVariable Long doc, Model model) {

		if (SessionManager.sessionId != null) {
			Guarantor g = guarantorService.getGuarantorById(id);

			if (doc == 1) {
				model.addAttribute("docs", g.getAadhaar());
			} else if (doc == 2) {
				model.addAttribute("docs", g.getPan());
			} else {
				model.addAttribute("docs", g.getCheque());
			}
			return "/pages/common/review_docs";
		}
		return "index";
	}

}
