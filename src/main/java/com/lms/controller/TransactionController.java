package com.lms.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lms.service.ExportPdfService;
import com.lms.service.SessionManager;
import com.lms.service.TransactionService;

@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	private ExportPdfService pdfService;

	private String pdf_method = "";
	@SuppressWarnings("unused")
	private String pdf_data = "";

	public TransactionController(TransactionService transactionService, ExportPdfService pdfService) {
		super();
		this.transactionService = transactionService;
		this.pdfService = pdfService;
	}

	@GetMapping("/transReport")
	public String transReport(Model model) {

		if (SessionManager.sessionId != null) {
			
			pdf_method = "getTransactionByMaker";
			
			if (transactionService.getTransactionByMaker(SessionManager.sessionId) != null) {
				model.addAttribute("transactions", transactionService.getTransactionByMaker(SessionManager.sessionId));
				// System.out.println("data found ");
			} else {
				model.addAttribute("successMessage", "data not found !");
			}
			
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/trans_report";
			} else {
				return "/pages/transaction/trans_report";
			}
		}
		return "index";
	}

	@GetMapping("/transSearch")
	public String transSearchReq(Model model) {

		if (SessionManager.sessionId != null) {
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/search_trans";
			} else {
				return "/pages/transaction/search_trans";
			}
		}
		return "index";
	}

	@PostMapping("/transSearch")
	public String transSearchRes(@RequestParam("data") String data, @RequestParam("searchby") String searchby,
			Model model) {

		if (SessionManager.sessionId != null) {
			
			if (searchby.equals("tid")) {
				pdf_method = "getTransactionByID";
				pdf_data = data;
				model.addAttribute("transactions", transactionService.getTransactionByID(Long.parseLong(data)));
			} else if (searchby.equals("lid")) {
				pdf_method = "getTransactionByLid";
				pdf_data = data;
				model.addAttribute("transactions", transactionService.getTransactionByLid(Long.parseLong(data)));
			} else if (searchby.equals("cid")) {
				pdf_method = "getTransactionByCid";
				pdf_data = data;
				model.addAttribute("transactions", transactionService.getTransactionByCid(Long.parseLong(data)));
			} else {
				model.addAttribute("successMessage", "Records not found !");
			}
			
			model.addAttribute("session_id", SessionManager.sessionId);
			model.addAttribute("session_name", SessionManager.sessionName);
			model.addAttribute("acc_type", SessionManager.accountType);

			if (SessionManager.accountType.equals("Agent Account")) {
				return "/pages/agent_pages/trans_report";
			} else {
				return "/pages/transaction/trans_report";
			}
		}
		return "index";
	}

	@GetMapping("/exportTransactionPdf")
	public ResponseEntity<?> exportTransactionPdf() throws IOException {

		ByteArrayInputStream pdf = pdfService.exportTransactionPdf(pdf_method);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition: inline;", "filename=test.pdf");
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdf));
	}
}
