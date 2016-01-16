package org.teacherbucks.model;

import java.util.List;

public class LogInResponse {
	
	private String token;
	private Company company;
	private User user;
	private List<Promotion> promotionList;
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
	public List<Promotion> getPromotionList() {
		return promotionList;
	}
	public void setPromotionList(List<Promotion> promotionList) {
		this.promotionList = promotionList;
	}
	
	

}
