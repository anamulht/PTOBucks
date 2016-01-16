package org.teacherbucks.model;

public class Promotion {
	
	private String id;
	private String title;
	private String description; 
	private String amount; 
	private String type_of_offer; 
	private String per_offer_amount; 
	private String starting_date;
	private String ending_date;
	private String remaining_amount; 
	private String image;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getType_of_offer() {
		return type_of_offer;
	}
	public void setType_of_offer(String type_of_offer) {
		this.type_of_offer = type_of_offer;
	}
	public String getPer_offer_amount() {
		return per_offer_amount;
	}
	public void setPer_offer_amount(String per_offer_amount) {
		this.per_offer_amount = per_offer_amount;
	}
	public String getStarting_date() {
		return starting_date;
	}
	public void setStarting_date(String starting_date) {
		this.starting_date = starting_date;
	}
	public String getEnding_date() {
		return ending_date;
	}
	public void setEnding_date(String ending_date) {
		this.ending_date = ending_date;
	}
	public String getRemaining_amount() {
		return remaining_amount;
	}
	public void setRemaining_amount(String remaining_amount) {
		this.remaining_amount = remaining_amount;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

}
