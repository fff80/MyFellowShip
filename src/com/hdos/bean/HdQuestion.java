package com.hdos.bean;



/**
 * HdQuestion entity. @author MyEclipse Persistence Tools
 */

public class HdQuestion  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String quenum;
     private String quetype;
     private String queproblem;
     private String anewersnum;


    // Constructors

    /** default constructor */
    public HdQuestion() {
    }

	/** minimal constructor */
    public HdQuestion(Integer id) {
        this.id = id;
    }
    
    /** full constructor */
    public HdQuestion(Integer id, String quenum, String quetype, String queproblem, String anewersnum) {
        this.id = id;
        this.quenum = quenum;
        this.quetype = quetype;
        this.queproblem = queproblem;
        this.anewersnum = anewersnum;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuenum() {
        return this.quenum;
    }
    
    public void setQuenum(String quenum) {
        this.quenum = quenum;
    }

    public String getQuetype() {
        return this.quetype;
    }
    
    public void setQuetype(String quetype) {
        this.quetype = quetype;
    }

    public String getQueproblem() {
        return this.queproblem;
    }
    
    public void setQueproblem(String queproblem) {
        this.queproblem = queproblem;
    }

	public String getAnewersnum() {
		return anewersnum;
	}

	public void setAnewersnum(String anewersnum) {
		this.anewersnum = anewersnum;
	}

}