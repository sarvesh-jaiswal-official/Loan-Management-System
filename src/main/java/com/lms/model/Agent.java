package com.lms.model;

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
@Table(name = "agent")
@AllArgsConstructor
@NoArgsConstructor
public class Agent {

	@Id
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", strategy = "com.lms.customid.CustomIdAgent")
	private long id;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String profile;
	
	private String name;
	private String email;

	private String password;
	private Long maker;
	private double walletbal;
	
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
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public Long getMaker() {
		return maker;
	}
	public void setMaker(Long maker) {
		this.maker = maker;
	}
	public double getWalletbal() {
		return walletbal;
	}
	public void setWalletbal(double walletbal) {
		this.walletbal = walletbal;
	}
	
}