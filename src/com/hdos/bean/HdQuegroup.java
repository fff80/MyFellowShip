package com.hdos.bean;



/**
 * HdQuegroup entity. @author MyEclipse Persistence Tools
 */

public class HdQuegroup  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Integer questionid;
     private String quetiontype;
     private String quegroupnum;


    // Constructors

    /** default constructor */
    public HdQuegroup() {
    }

	/** minimal constructor */
    public HdQuegroup(Integer id) {
        this.id = id;
    }
    
    /** full constructor */
    public HdQuegroup(Integer id, Integer questionid, String quetiontype, String quegroupnum) {
        this.id = id;
        this.questionid = questionid;
        this.quetiontype = quetiontype;
        this.quegroupnum = quegroupnum;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionid() {
        return this.questionid;
    }
    
    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public String getQuetiontype() {
        return this.quetiontype;
    }
    
    public void setQuetiontype(String quetiontype) {
        this.quetiontype = quetiontype;
    }

    public String getQuegroupnum() {
        return this.quegroupnum;
    }
    
    public void setQuegroupnum(String quegroupnum) {
        this.quegroupnum = quegroupnum;
    }
   








}