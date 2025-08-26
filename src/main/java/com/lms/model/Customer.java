package com.lms.model;

import java.sql.Date;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", strategy = "com.lms.customid.CustomIdCustomer")
	private long id;
	
	private String name;
	private String email;
	private String phone;
	private String address;
	private Date dob;
	private String gender;
	private String kycst;
	private String rejrs; 
	private int creditsc;
	private int loans;
	private Long maker;
	private int gcount;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String aadhaar;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String pan;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String photo;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAadhaar() {
		return aadhaar;
	}

	public void setAadhaar(String aadhaar) {
		this.aadhaar = aadhaar;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getKycst() {
		return kycst;
	}

	public void setKycst(String kycst) {
		this.kycst = kycst;
	}

	public String getRejrs() {
		return rejrs;
	}

	public void setRejrs(String rejrs) {
		this.rejrs = rejrs;
	}

	public int getCreditsc() {
		return creditsc;
	}

	public void setCreditsc(int creditsc) {
		this.creditsc = creditsc;
	}

	public int getLoans() {
		return loans;
	}

	public void setLoans(int loans) {
		this.loans = loans;
	}
	
	public Long getMaker() {
		return maker;
	}
	public void setMaker(Long maker) {
		this.maker = maker;
	}

	public int getGcount() {
		return gcount;
	}

	public void setGcount(int gcount) {
		this.gcount = gcount;
	}
	
}
