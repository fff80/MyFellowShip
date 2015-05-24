package com.hdos.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hdos.bean.HdUser;
import com.hdos.utils.DBAccess;
import com.hdos.utils.MD5Util;
import com.hdos.utils.PageBean;
import com.hdos.utils.StringUtils;
import com.hdos.utils.Tool;
import com.mysql.jdbc.Connection;

import freemarker.template.SimpleDate;

public class UserDAO extends BaseDAO<HdUser> {
	DBAccess db=DBAccess.getInstance();

	public UserDAO() {
		super(HdUser.class);
	}

	public PageBean list(String username, int currPage, int pageSize)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		StringBuffer countsb=new StringBuffer();
		sb.append("select * from hd_user where 1=1 ");
		if (username != null && !username.equals("")) {
			sb.append("and username like BINARY '%" + username + "%'").append("\n");
		}
		countsb.append("select count(*) from ("+sb.toString()+")t");
		int count = db.QueryForInt(countsb.toString(), null);
		sb.append(" LIMIT "+(currPage-1)*pageSize+","+pageSize+"");
		List list=db.QueryForList(sb.toString(),null);
		PageBean pb = new PageBean(currPage, pageSize);
		pb.setPageCount(count);
		pb.setResultlist(list);
		return pb;
	}
	
	public List seluser() throws SQLException {
		List list;
		StringBuffer sb=new StringBuffer();
		sb.append("select t.* from hd_user t where t.type<>0").append("\n");
		System.out.println("sb.toString()============="+sb.toString());
		list=DBAccess.getInstance().QueryForList(sb.toString(), null);
		return list;
	}
	
	public void deletePojo(int iid) {
		this.beginTransaction();
		this.remove(iid);
		this.commit();
	}

	public Map<String, Object> queryUser(String sql) throws SQLException {
		return StringUtils.mapKeyLower(DBAccess.getInstance().QueryForMap(sql,
				null));
	}

	public Map<String, Object> queryUser(String username, String password)
			throws SQLException {
		String sql = "select * from hd_user t where t.username='"+username+"' and t.password='"+MD5Util.getMD5String(password)+"'";
		System.out.println(sql);
		return StringUtils.mapKeyLower(DBAccess.getInstance().QueryForMap(sql,null));
	}
	
//	create table Hd_txl
//	(
//	   id                   int not null comment '主键id',
//	   name                 varchar(255) comment '姓名',
//	   mobile               varchar(255) comment '手机号',
//	   txlloadid            int comment '通讯录上传id',
//	   primary key (id)
//	);
	//创建表
	public Boolean createTable(String tablename){
		   Boolean flag=true;
           StringBuffer sb=new StringBuffer();
           sb.append("create table hd_"+tablename+" (").append("\n");
           sb.append("id int not null auto_increment,").append("\n");
           sb.append("name varchar(255),").append("\n");
           sb.append("mobile varchar(255),").append("\n");
           sb.append("txlloadid int,").append("\n");
           sb.append("primary key(id)");
           sb.append(")ENGINE= MYISAM CHARACTER SET utf8;");
           db.commit();
           System.out.println("创建表语句==="+sb.toString());
           try {
			db.exeInsert(sb.toString());
		} catch (SQLException e) {
			flag=false;
			e.printStackTrace();
		}
		System.out.println("flag:"+flag);
           return flag;
	}
	
	//修改表名
	public Boolean updateTable(String oldtablename,String newtablename){
           StringBuffer sb=new StringBuffer();
           Boolean flag=true;
           sb.append("alter table hd_"+oldtablename+" RENAME hd_"+newtablename+";").append("\n");
           System.out.println("修改表名称==="+sb.toString());
           try {
			db.exeInsert(sb.toString());
			db.commit();
		} catch (SQLException e) {
			flag=false;
			e.printStackTrace();
		}
          return flag;
	}
	
	//删除表
	public Boolean dropTable(String tablename) {
           StringBuffer sb=new StringBuffer();
           Boolean flag=true;
           sb.append("drop table hd_"+tablename+";").append("\n");
           System.out.println("删除表==="+sb.toString());
           try {
			db.exeInsert(sb.toString());
			db.commit();
		} catch (SQLException e) {
			flag=false;
			e.printStackTrace();
		}
          return flag;
	}
	
	public void droptables(String s) throws SQLException {
		StringBuffer sb=new StringBuffer();
		sb.append("select id,username,userid from hd_user where id in ("+s+");");
		List list=db.QueryForList(sb.toString(), null);
		for(int i=0;i<list.size();i++){
			String username=(String)((Map)list.get(i)).get("username");
			String userid=(String)((Map)list.get(i)).get("userid");
			new UserDAO().dropTable(username);
		}
	}
	/**
	 * 根据年龄，性别，是否有小孩删选会员(按照特定规则)
	 * sex：如果为男性，则选择女性
	 * age：若为男性，则选择比自己小8岁以内的女性，若为女性，则选择比自己大8岁以内的男性
	 * ifChild：按照前端传值选择
	 * @param age
	 * @param sex
	 * @param ifChild
	 * @return
	 */
	public List<HdUser> getHdUserbyAgeAndSexAndChild(int sex,int age,int ifChild,int height){
		StringBuffer sb = new StringBuffer();
		List<Object> parameters = new ArrayList<Object>();
		//sb.append("from HdUser where sex = ? and ifchild = ?");
		sb.append("from HdUser where sex = ? ");
		if(sex==1){//为男性  男性匹配女性，男性的年龄只能比女性大
			parameters.add(2);
			//parameters.add(ifChild);
			//年龄刷选
			sb.append(" and age >=? and age<=?");
			parameters.add(age-12);
			parameters.add(age);
			//身高删选
			sb.append(" and height >=? and height<=?");
			parameters.add(height-25);
			parameters.add(height);
		}
		if(sex==2){//为女性  女性只能匹配比自己大的男性
			parameters.add(1);
			//parameters.add(ifChild);
			sb.append(" and age >=? and age<=?");
			parameters.add(age);
			parameters.add(age+12);
			//身高删选
			sb.append(" and height >=? and height<=?");
			parameters.add(height);
			parameters.add(height+25);
		}
		return this.findByHql(sb.toString(), parameters.toArray());
	}
	
	 /**
     * 智能匹配并返回前五名的会员
     * @param user
     * @param userList
     */
	public List<HdUser> matchUser(HdUser user, List<HdUser> userList) {
		Map<HdUser,Integer> userMap = new HashMap<HdUser,Integer>();
		Integer sex = user.getSex();//1.性别
		//Integer age = user.getAge();//2.年龄匹配
		int nature = user.getNature();//3.性格类型匹配
		Date birthday=user.getBirthday(); //出生日期
		String coord=user.getCoord();//4.距离匹配
		Integer height=user.getHeight();//5.身高匹配
		Integer meter =user.getMeter();//6.体重匹配
		Integer ifhouse =user.getIfhouse();//7.有房匹配
		Integer marryNum = user.getMarrynum();//8.结婚期限匹配
		Integer viptype = user.getViptype();//9.会员(诚意)匹配
		Integer marriagestatus = user.getMarriagestatus();//10.婚恋状态匹配
		Integer hobby = user.getHobby();//11.嗜好匹配
		Integer freetime=user.getFreetime();//12.空余时间匹配
		
		System.out.println("size:"+userList.size());
		String faith = user.getFaith();//信仰
		for(int i = 0;i<userList.size();i++){
			int weight = 0;
			HdUser mUser = userList.get(i);
			//step 1.性格类型匹配
			int mUserNature = mUser.getNature();
			if(1==nature){   //1、NT ，2、NF ，3、SJ ，4、SP
				if(1==mUserNature) weight = 8;
				if(2==mUserNature) weight = 5;
				if(3==mUserNature) weight = 3;
				if(4==mUserNature) weight = 1;
			}
			if(2==nature){
				if(2==mUserNature) weight = 8;
				if(3==mUserNature) weight = 3;
				if(4==mUserNature) weight = 1;
			}
			if(3==nature){
				if(3==mUserNature) weight = 8;
				if(4==mUserNature) weight = 1;
			}
			if(4==nature){
				if(4==mUserNature) weight = 8;
			}
			//step 2.年龄匹配
			Date mDate = mUser.getBirthday();
			if(mDate!=null){
				int age = countAge(birthday);
				int mAge = countAge(mDate);
				int ageDiff = Math.abs(age-mAge);
				if(ageDiff<6) weight+=7*9;
				if(ageDiff>=6&&ageDiff<8) weight+=6*9;
				if(ageDiff>=9&&ageDiff<12) weight+=5*9;
			}
			//step 3.物质条件匹配
		    Integer thingAsk = mUser.getThingask();
		    if(thingAsk==1) weight-=1;//如果为1，则要求有房
		    if(thingAsk==2) weight+=4;//如果为2，则为没有要求
		    //step 4.信仰匹配
		    String mFaith = mUser.getFaith();
		    if(faith.equals(mFaith)) {
		    	weight+=3;
		    }else{
		    	weight-=1;
		    }
		    //step 5.结婚期限匹配
		    Integer mMarryNum = mUser.getMarrynum();
		    if(Math.abs(marryNum-mMarryNum)==4) weight+=1*6;
		    if(Math.abs(marryNum-mMarryNum)==3) weight+=2*6;
		    if(Math.abs(marryNum-mMarryNum)==2) weight+=3*6;
		    if(Math.abs(marryNum-mMarryNum)==1) weight+=4*6;
		    if(Math.abs(marryNum-mMarryNum)==0) weight+=5*6;
		    
		    //step 6.空余时间权值匹配
		    Integer mFreetime = mUser.getFreetime();//取得的是二进制数
		    if(mFreetime!=null){
			    int re = freetime&mFreetime;
			    String timeMatch = Integer.toBinaryString(re);
			    int count = 0;
			    for(int index=0;index<timeMatch.length();index++){
			    	  char xx = timeMatch.charAt(index);
			    	  if(xx==1){count++;}
			    }
			    weight+=count*6;
		    }
		    //step 7.相隔距离权值匹配
		    String mcoord=mUser.getCoord();  //坐标
		    String scoord[];
		    String smcoord[];
		    if(!mcoord.equals("")&&!coord.equals("")){
		    	scoord=coord.split(",");
		    	smcoord=mcoord.split(",");
		    	String jl=Tool.Distance(Double.parseDouble(scoord[0]),Double.parseDouble(scoord[1]),Double.parseDouble(smcoord[0]),Double.parseDouble(smcoord[1]));
		    	float k=Float.parseFloat(jl);
		    	if(0<=k&&k<=10) weight+=5*7;
		    	if(10<k&&k<=20) weight+=4*7;
		    	if(20<k&&k<=50) weight+=3*7;
		    	if(50<k&&k<=100) weight+=2*7;
		    	if(k>100) weight+=1*7;
		    }
		    //step 8.身高权值匹配
		    Integer mheight=mUser.getHeight(); //身高
		    int heightDiff = Math.abs(mheight-height);
		    if(heightDiff<=4) weight+=4*6;
		    if(heightDiff>=5&&heightDiff<=15) weight+=5*6;
		    if(heightDiff>=16&&heightDiff<=25) weight+=1*6;
		    
		    //step 9.体重匹配
		    Integer mMeter=mUser.getMeter(); //体重
		    if(meter!=null&&mMeter!=null){
		       int meterDiff = mMeter-meter;
		       if(sex==1){
		    	   if(meterDiff>=-15&&meterDiff<=-5) weight+=5*5;
		    	   if(meterDiff>-5) weight+=1*5;
		       }
		       if(sex==2){
		    	   if(meterDiff>=5&&meterDiff<=15) weight+=5*5;
		    	   if(meterDiff>15) weight+=1*5;
		       }
		    }
		    
		    //step 10.有房匹配
		    Integer mIfhouse = mUser.getIfhouse();
		    if(ifhouse!=null&&mIfhouse!=null){
		    	if(ifhouse==1&&ifhouse==mIfhouse)
		    		 weight+=5*7;
		    	if(ifhouse==1&&ifhouse!=mIfhouse)
		    		 weight+=2*7;
		    };
		    
		    //step 11.诚意匹配
		    Integer mVIP = mUser.getViptype();
		    if(mVIP!=null){
			    if(mVIP==1) weight+=1*3;
			    if(mVIP==2) weight+=2*3;
			    if(mVIP==3) weight+=3*3;
			    if(mVIP==4) weight+=4*3;
			    if(mVIP==5) weight+=2*3;
		    }
		    
		    //step 12.婚恋状态marriagestatus
		    Integer marryStatus = mUser.getMarriagestatus();
		    if(marryStatus!=null){
				    if(marriagestatus==1&&marriagestatus==marryStatus) weight+=2*4;
				    if(marriagestatus==2&&marriagestatus==marryStatus) weight+=1*4;
				    else weight+=1*4;
		    }
		    //step 13.嗜好
		    Integer mhobby = mUser.getHobby();
		    if(mhobby!=null){
			    if(hobby==mhobby) weight+=2*3;
			    else weight+=1*3;
		    }
		    System.out.println("username"+mUser.getUsername()+"     "+"weight:"+weight+"\n");
		    userMap.put(mUser, weight);
		}
		//根据权重weight值对userMap进行排序并选出前5名
		List<Entry<HdUser,Integer>> list = new ArrayList<Entry<HdUser,Integer>>(userMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<HdUser, Integer>>() {
			    public int compare(Map.Entry<HdUser, Integer> o1, Map.Entry<HdUser, Integer> o2) {
			        return (o2.getValue() - o1.getValue());
			    }
		});
       //获取前五名
		List<HdUser> uList = new ArrayList<HdUser>();
		Iterator itr = userMap.keySet().iterator();
		int i=0;
		while(itr.hasNext()){
			uList.add((HdUser)itr.next());
			if(i==4) break;
			i++;
		}
		return uList;
	}
	/**
	 * 根据用户userid获取用户
	 * @param userId
	 * @return
	 */
    public HdUser getUser(String userId){
    	StringBuffer sb = new StringBuffer();
    	sb.append("from HdUser where userid = ?");
    	return this.findUnique(sb.toString(),new Object[]{userId});
    }
    /**
     * n
     * @description （方法说明）
     * @auther hhq
     * @time 2015-5-17	
     * @param date 传入用户的出生日期
     * @return
     */
    public int countAge(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Integer birthYear = Integer.parseInt(sdf.format(date));
		System.out.println("birthYear:"+birthYear);
		Integer curYear = Integer.parseInt(sdf.format(new Date()));
		System.out.println("curYear:"+curYear);
		int age = curYear - birthYear;
		return age;
    }
    
	public static void main(String[] args) throws Exception {
		//new UserDAO().createTable("admin1");
		//new UserDAO().updateTable("admin2", "admin3");
		//new UserDAO().dropTable("admin1");
		//System.out.println(new UserDAO().getUser("adcd").getUserid());
	}

}
