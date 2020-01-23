package com.gpch.login.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;


@Entity
@Table(name = "application")
public class Application {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
	@Column
	private String description;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fromDate;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date toDate;
	@Column
	private int noOfTakenDays;
	@Column
	private int noOfLeftDays;
	@Column
	private boolean approved;

	public Application() {}
	
    public Application(User user, String description, Date fromDate, Date toDate, int noOfTakenDays, int noOfLeftDays) {
    	this.user=user;
    	this.description=description;
    	this.fromDate=fromDate;
    	this.toDate=toDate;
    	this.noOfTakenDays=noOfTakenDays;
    	this.noOfLeftDays=noOfLeftDays;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getNoOfTakenDays() {
		return noOfTakenDays;
	}

	public void setNoOfTakenDays(int noOfTakenDays) {
		this.noOfTakenDays = noOfTakenDays;
	}

	public int getNoOfLeftDays() {
		return noOfLeftDays;
	}

	public void setNoOfLeftDays(int noOfLeftDays) {
		this.noOfLeftDays = noOfLeftDays;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	
}
