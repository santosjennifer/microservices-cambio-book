package com.jads.dto;

import java.util.Date;

public class BookDto {
	
	private Long id;
	private String title;
	private String author;
	private Date launchDate;
	private Double price;
	private String currency;
	private String environment;
	
	public BookDto() {}
	
	public BookDto(Long id, String title, String author, Date launchDate, Double price, String currency, String environment) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.launchDate = launchDate;
		this.price = price;
		this.currency = currency;
		this.environment = environment;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getLaunchDate() {
		return launchDate;
	}
	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
}
