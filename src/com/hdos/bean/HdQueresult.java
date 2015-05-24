package com.hdos.bean;



/**
 * HdQueresult entity. @author MyEclipse Persistence Tools
 */

public class HdQueresult  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Integer userid;
     private Integer quetionid;
     private String quetionresult;


    // Constructors

    /** default constructor */
    public HdQueresult() {
    }

	/** minimal constructor */
    public HdQueresult(Integer id) {
        this.id = id;
    }
    
    /** full constructor */
    public HdQueresult(Integer id, Integer userid, Integer quetionid, String quetionresult) {
        this.id = id;
        this.userid = userid;
        this.quetionid = quetionid;
        this.quetionresult = quetionresult;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return this.userid;
    }
    
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getQuetionid() {
        return this.quetionid;
    }
    
    public void setQuetionid(Integer quetionid) {
        this.quetionid = quetionid;
    }

    public String getQuetionresult() {
        return this.quetionresult;
    }
    
    public void setQuetionresult(String quetionresult) {
        this.quetionresult = quetionresult;
    }
   








}