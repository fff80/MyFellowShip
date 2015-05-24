package com.hdos.bean;

import java.util.Date;

/**
 * HdUser entity. @author MyEclipse Persistence Tools
 */

public class HdUser implements java.io.Serializable {

	// Fields

	private Integer id;  
	private String username;   //用户名
	private String userid;  //用户id
	private String password;  //密码
	private String realName;  //真实姓名
	private String nickname;  //昵称
	private Integer sex;  //性别
	private Integer age;  //年龄
	private Date birthday; //出生日期
	private String faith; //信仰
	private String imageurl; //图片url
	private Integer ifchild; //是否有孩子
	private Integer ifmind; //是否介意有小孩
	private Integer height; //身高
	private String address; //租住地址
	private String coord; //坐标
	private String moblie;  //移动电话
	private Integer thingask; //物质要求
	private Integer marriagestatus; //婚姻状况
	private Integer marrynum;  //婚姻期限
	private String questionsmall;  //答案小类
	private Integer nature;  //MBTI性格类型
	private Integer freetime;  //空余时间
	private String queresid;  //对应答案id
	private String quegroupid; //对应题库id
	private Integer ifhouse;  //是否有房
	private Integer viptype; //会员类型
	private Integer meter;  //体重
	private Integer hobby; //嗜好
	private String email;//邮箱
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getFaith() {
		return faith;
	}
	public void setFaith(String faith) {
		this.faith = faith;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public Integer getIfchild() {
		return ifchild;
	}
	public void setIfchild(Integer ifchild) {
		this.ifchild = ifchild;
	}
	public Integer getIfmind() {
		return ifmind;
	}
	public void setIfmind(Integer ifmind) {
		this.ifmind = ifmind;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCoord() {
		return coord;
	}
	public void setCoord(String coord) {
		this.coord = coord;
	}
	public String getMoblie() {
		return moblie;
	}
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}
	public Integer getThingask() {
		return thingask;
	}
	public void setThingask(Integer thingask) {
		this.thingask = thingask;
	}
	public Integer getMarriagestatus() {
		return marriagestatus;
	}
	public void setMarriagestatus(Integer marriagestatus) {
		this.marriagestatus = marriagestatus;
	}
	public Integer getMarrynum() {
		return marrynum;
	}
	public void setMarrynum(Integer marrynum) {
		this.marrynum = marrynum;
	}
	public String getQuestionsmall() {
		return questionsmall;
	}
	public void setQuestionsmall(String questionsmall) {
		this.questionsmall = questionsmall;
	}
	public Integer getNature() {
		return nature;
	}
	public void setNature(Integer nature) {
		this.nature = nature;
	}
	public Integer getFreetime() {
		return freetime;
	}
	public void setFreetime(Integer freetime) {
		this.freetime = freetime;
	}
	public String getQueresid() {
		return queresid;
	}
	public void setQueresid(String queresid) {
		this.queresid = queresid;
	}
	public String getQuegroupid() {
		return quegroupid;
	}
	public void setQuegroupid(String quegroupid) {
		this.quegroupid = quegroupid;
	}
	public Integer getIfhouse() {
		return ifhouse;
	}
	public void setIfhouse(Integer ifhouse) {
		this.ifhouse = ifhouse;
	}
	public Integer getViptype() {
		return viptype;
	}
	public void setViptype(Integer viptype) {
		this.viptype = viptype;
	}
	public Integer getHobby() {
		return hobby;
	}
	public void setHobby(Integer hobby) {
		this.hobby = hobby;
	}
	public Integer getMeter() {
		return meter;
	}
	public void setMeter(Integer meter) {
		this.meter = meter;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

}