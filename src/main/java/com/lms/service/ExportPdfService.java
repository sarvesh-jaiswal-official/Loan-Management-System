package com.lms.service;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.lms.model.Customer;
import com.lms.model.Loan;
import com.lms.model.Transaction;

@Service
public class ExportPdfService {

	@Autowired
	private CustomerService customerService;
	private LoanService loanService;
	private TransactionService transactionService;

	public ExportPdfService(CustomerService customerService, LoanService loanService,
			TransactionService transactionService) {
		this.customerService = customerService;
		this.loanService = loanService;
		this.transactionService = transactionService;
	}

	public ByteArrayInputStream exportCustomerPdf(String method) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			Paragraph p1 = new Paragraph("Loan Management System");

			float[] colWidth = { 100f, 100f, 100f, 100f, 100f, 100f, 100f };
			Table table = new Table(colWidth);
			table.addCell(new Cell().add("ID"));
			table.addCell(new Cell().add("Name"));
			table.addCell(new Cell().add("Email"));
			table.addCell(new Cell().add("Phone"));
			table.addCell(new Cell().add("DoB"));
			table.addCell(new Cell().add("Gender"));
			table.addCell(new Cell().add("Address"));

			for (Customer cust : customerService.getCustomerByMaker(SessionManager.sessionId)) {
				table.addCell(new Cell().add(String.valueOf(cust.getId())));
				table.addCell(new Cell().add(cust.getName()));
				table.addCell(new Cell().add(cust.getEmail()));
				table.addCell(new Cell().add(cust.getPhone()));
				table.addCell(new Cell().add(String.valueOf(cust.getDob())));
				table.addCell(new Cell().add(cust.getGender()));
				table.addCell(new Cell().add(cust.getAddress()));
			}
			document.add(p1);
			document.add(table);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	public ByteArrayInputStream exportLoanPdf(String method, String data) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			document.setHeight(200);
			document.setHeight(400);
			Paragraph p1 = new Paragraph("Loan Management System");

			float[] colWidth = { 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f };
			Table table = new Table(colWidth);
			table.addCell(new Cell().add("LID"));
			table.addCell(new Cell().add("CID"));
			table.addCell(new Cell().add("Amount"));
			table.addCell(new Cell().add("Interest"));
			table.addCell(new Cell().add("Fees"));
			table.addCell(new Cell().add("Pending Amt"));
			table.addCell(new Cell().add("EMI OP"));
			table.addCell(new Cell().add("Apply Date"));
			table.addCell(new Cell().add("Due Date"));
			table.addCell(new Cell().add("Approval Status"));
			table.addCell(new Cell().add("Loan Status"));

			if (method.equals("getLoanByMaker")) {
				for (Loan loan : loanService.getLoanByMaker(SessionManager.sessionId)) {
					table.addCell(new Cell().add(String.valueOf(loan.getId())));
					table.addCell(new Cell().add(String.valueOf(loan.getCustid())));
					table.addCell(new Cell().add(String.valueOf(loan.getAmt())));
					table.addCell(new Cell().add(String.valueOf(loan.getInterest())));
					table.addCell(new Cell().add(String.valueOf(loan.getProcfees())));
					table.addCell(new Cell().add(String.valueOf(loan.getPendamt())));
					table.addCell(new Cell().add(loan.getEmiop()));
					table.addCell(new Cell().add(loan.getDoa()));
					table.addCell(new Cell().add(loan.getDuedate()));
					table.addCell(new Cell().add(loan.getApprovest()));
					table.addCell(new Cell().add(loan.getLoanst()));
				}
			} else if (method.equals("getLoanByApprovestDisbursed")) {
				for (Loan loan : loanService.getLoanByApprovest("Disbursed")) {
					table.addCell(new Cell().add(String.valueOf(loan.getId())));
					table.addCell(new Cell().add(String.valueOf(loan.getCustid())));
					table.addCell(new Cell().add(String.valueOf(loan.getAmt())));
					table.addCell(new Cell().add(String.valueOf(loan.getInterest())));
					table.addCell(new Cell().add(String.valueOf(loan.getProcfees())));
					table.addCell(new Cell().add(String.valueOf(loan.getPendamt())));
					table.addCell(new Cell().add(loan.getEmiop()));
					table.addCell(new Cell().add(loan.getDoa()));
					table.addCell(new Cell().add(loan.getDuedate()));
					table.addCell(new Cell().add(loan.getApprovest()));
					table.addCell(new Cell().add(loan.getLoanst()));
				}
			} else if (method.equals("getLoanByApprovestNA")) {
				for (Loan loan : loanService.getLoanByApprovest("NA")) {
					table.addCell(new Cell().add(String.valueOf(loan.getId())));
					table.addCell(new Cell().add(String.valueOf(loan.getCustid())));
					table.addCell(new Cell().add(String.valueOf(loan.getAmt())));
					table.addCell(new Cell().add(String.valueOf(loan.getInterest())));
					table.addCell(new Cell().add(String.valueOf(loan.getProcfees())));
					table.addCell(new Cell().add(String.valueOf(loan.getPendamt())));
					table.addCell(new Cell().add(loan.getEmiop()));
					table.addCell(new Cell().add(loan.getDoa()));
					table.addCell(new Cell().add(loan.getDuedate()));
					table.addCell(new Cell().add(loan.getApprovest()));
					table.addCell(new Cell().add(loan.getLoanst()));
				}
			}

			else if (method.equals("getLoanByID")) {
				Loan loan = loanService.getLoanByID(Long.parseLong(data));

				table.addCell(new Cell().add(String.valueOf(loan.getId())));
				table.addCell(new Cell().add(String.valueOf(loan.getCustid())));
				table.addCell(new Cell().add(String.valueOf(loan.getAmt())));
				table.addCell(new Cell().add(String.valueOf(loan.getInterest())));
				table.addCell(new Cell().add(String.valueOf(loan.getProcfees())));
				table.addCell(new Cell().add(String.valueOf(loan.getPendamt())));
				table.addCell(new Cell().add(loan.getEmiop()));
				table.addCell(new Cell().add(loan.getDoa()));
				table.addCell(new Cell().add(loan.getDuedate()));
				table.addCell(new Cell().add(loan.getApprovest()));
				table.addCell(new Cell().add(loan.getLoanst()));

			} else if (method.equals("getLoanByMakerAndEmiop")) {
				for (Loan loan : loanService.getLoanByMakerAndEmiop(SessionManager.sessionId, data)) {
					table.addCell(new Cell().add(String.valueOf(loan.getId())));
					table.addCell(new Cell().add(String.valueOf(loan.getCustid())));
					table.addCell(new Cell().add(String.valueOf(loan.getAmt())));
					table.addCell(new Cell().add(String.valueOf(loan.getInterest())));
					table.addCell(new Cell().add(String.valueOf(loan.getProcfees())));
					table.addCell(new Cell().add(String.valueOf(loan.getPendamt())));
					table.addCell(new Cell().add(loan.getEmiop()));
					table.addCell(new Cell().add(loan.getDoa()));
					table.addCell(new Cell().add(loan.getDuedate()));
					table.addCell(new Cell().add(loan.getApprovest()));
					table.addCell(new Cell().add(loan.getLoanst()));
				}
			}
			document.add(p1);
			document.add(table);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	public ByteArrayInputStream exportTransactionPdf(String method) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			Paragraph p1 = new Paragraph("Loan Management System");

			float[] colWidth = { 100f, 100f, 100f, 100f, 100f, 100f };
			Table table = new Table(colWidth);
			table.addCell(new Cell().add("ID"));
			table.addCell(new Cell().add("Amount"));
			table.addCell(new Cell().add("CID"));
			table.addCell(new Cell().add("LID"));
			table.addCell(new Cell().add("Date Time"));
			table.addCell(new Cell().add("Method"));

			for (Transaction trans : transactionService.getTransactionByMaker(SessionManager.sessionId)) {
				table.addCell(new Cell().add(String.valueOf(trans.getId())));
				table.addCell(new Cell().add(String.valueOf(trans.getAmt())));
				table.addCell(new Cell().add(String.valueOf(trans.getCid())));
				table.addCell(new Cell().add(String.valueOf(trans.getLid())));
				table.addCell(new Cell().add(trans.getDatetime()));
				table.addCell(new Cell().add(trans.getMethod()));
			}
			document.add(p1);
			document.add(table);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

}
