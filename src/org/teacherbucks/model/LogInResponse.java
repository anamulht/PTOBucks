package org.teacherbucks.model;

import java.util.Vector;

public class LogInResponse {
	
	private String token;
	private Company company;
	private User user;
	private Vector<Promotion> promotionList;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Vector<Promotion> getPromotionList() {
		return promotionList;
	}
	public void setPromotionList(Vector<Promotion> promotionList) {
		this.promotionList = promotionList;
	}
	
	

}
