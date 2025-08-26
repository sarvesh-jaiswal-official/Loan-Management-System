package com.lms.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userinfo")
@AllArgsConstructor
@NoArgsConstructor
public class Userinfo {

	@Id
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", strategy = "com.lms.customid.CustomIdUserinfo")
	private Long id;
	
	private double loandis;
	private double outstand;
	private double repayment;
	private int client;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getLoandis() {
		return loandis;
	}
	public void setLoandis(double loandis) {
		this.loandis = loandis;
	}
	public double getOutstand() {
		return outstand;
	}
	public void setOutstand(double outstand) {
		this.outstand = outstand;
	}
	public double getRepayment() {
		return repayment;
	}
	public void setRepayment(double repayment) {
		this.repayment = repayment;
	}
	public int getClient() {
		return client;
	}
	public void setClient(int client) {
		this.client = client;
	}
	
	
	
	
}
