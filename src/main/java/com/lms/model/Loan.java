package com.lms.model;

import org.hibernate.annotations.GenericGenerator; 

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan")
@AllArgsConstructor
@NoArgsConstructor
public class Loan {


	@Id
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", strategy = "com.lms.customid.CustomIdLoan")
	private long id;
	
	private long custid;
	private double amt;
	private double procfees;
	private double interest;
	private double finalamt;
	private double emiamt;
	private double pendamt;
		
	private int duration;
	private String emiop;
	
	private String approvest;
	private String loanst;
	private String doa;
	private String duedate;
	private Long maker;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustid() {
		return custid;
	}
	public void setCustid(long custid) {
		this.custid = custid;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public double getProcfees() {
		return procfees;
	}
	public void setProcfees(double procfees) {
		this.procfees = procfees;
	}
	public double getInterest() {
		return interest;
	}
	public void setInterest(double interest) {
		this.interest = interest;
	}
	public double getFinalamt() {
		return finalamt;
	}
	public void setFinalamt(double finalamt) {
		this.finalamt = finalamt;
	}
	public double getEmiamt() {
		return emiamt;
	}
	public void setEmiamt(double emiamt) {
		this.emiamt = emiamt;
	}
	public double getPendamt() {
		return pendamt;
	}
	public void setPendamt(double pendamt) {
		this.pendamt = pendamt;
	}
	public String getEmiop() {
		return emiop;
	}
	public void setEmiop(String emiop) {
		this.emiop = emiop;
	}
	
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getDuedate() {
		return duedate;
	}
	public void setDuedate(String dueDate) {
		this.duedate = dueDate;
	}
	public String getApprovest() {
		return approvest;
	}
	public void setApprovest(String approvest) {
		this.approvest = approvest;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getLoanst() {
		return loanst;
	}
	public void setLoanst(String loanst) {
		this.loanst = loanst;
	}
	public Long getMaker() {
		return maker;
	}
	public void setMaker(Long maker) {
		this.maker = maker;
	}
}
