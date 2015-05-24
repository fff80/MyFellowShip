package com.hdos.bean;

/**
 * HdAnewers entity. @author MyEclipse Persistence Tools
 */

public class HdAnewers implements java.io.Serializable {

	// Fields

	private Integer id;
	private String anwer;
	private String anerrtype;
	private String anewersnum;

	// Constructors

	/** default constructor */
	public HdAnewers() {
	}

	/** full constructor */
	public HdAnewers(String anwer, String anerrtype, String anewersnum) {
		this.anwer = anwer;
		this.anerrtype = anerrtype;
		this.anewersnum = anewersnum;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnwer() {
		return this.anwer;
	}

	public void setAnwer(String anwer) {
		this.anwer = anwer;
	}

	public String getAnerrtype() {
		return this.anerrtype;
	}

	public void setAnerrtype(String anerrtype) {
		this.anerrtype = anerrtype;
	}

	public String getAnewersnum() {
		return this.anewersnum;
	}

	public void setAnewersnum(String anewersnum) {
		this.anewersnum = anewersnum;
	}

}