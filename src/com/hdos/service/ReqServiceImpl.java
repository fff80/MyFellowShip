package com.hdos.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.google.gson.Gson;
import com.hdos.bean.HdUser;
import com.hdos.bean.Quegroup;
import com.hdos.bean.Question;
import com.hdos.bean.Res;
import com.hdos.bean.StructBaseUserInfo;
import com.hdos.dao.UserDAO;
import com.hdos.utils.DBAccess;
import com.hdos.utils.MD5Util;
import com.hdos.utils.Tool;

public class ReqServiceImpl implements ReqService {
    private DBAccess db = DBAccess.getInstance();
    private UserDAO userDao = new UserDAO();
    private JSONObject jsonObject = null;
    private final int NT = 1;//"NT";
	private final int NF = 2;//"NF";
	private final int SJ = 3;//"SJ";
	private final int SP = 4;//"SP";

	//用户注册接口，返回userid
	public String register(String username, String pwd) {
		Map<String,String> regMap = new HashMap<String,String>();
		List userList = new ArrayList();
		String getjson="";
		String sql = "select username,password from hd_user where username = ?";
		try {
			userList = db.QueryForList(sql, new Object[]{username});
			if(userList.size()!=0){
				regMap.put("respCode", "E01");
				regMap.put("respMsg", "用户名已存在！");
				getjson = new Gson().toJson(regMap);
				return getjson;
			}else{
				HdUser userinfo = new HdUser();
				String randomstr=Tool.getRandomStr(16); //得到指定长度的16位字符串
				pwd = MD5Util.getMD5String(pwd);
				userinfo.setUsername(username);
				userinfo.setPassword(pwd);
//				userinfo.setNickname(nickname);
				userinfo.setUserid(randomstr);
				userDao.beginTransaction();
				userDao.save(userinfo);
				userDao.commit();
				new ReqServiceImpl().setQueGroup(randomstr); //生成随机的题库
			    regMap.put("respCode", "E00");
				regMap.put("respMsg", randomstr);
			}
		} catch (SQLException e) {
			regMap.put("respCode", "E02");
			regMap.put("respMsg", "数据库异常！");
			e.printStackTrace();
		}
		getjson = new Gson().toJson(regMap);
		return getjson;
	}
	
	//用户登录接口，返回userid
	public String getUserid(String username, String pwd) {
		Map<String,String> remap=new HashMap<String,String>();
		Res res=new Res();
		String getjson="";
		String userinfojson="";
		try {
			Map<String, Object> user = userDao.queryUser(username,pwd);
			if(user==null){
				res.setRespCode("E01");
				res.setRespMsg("用户名密码错误！");
				getjson = new Gson().toJson(res);
				return getjson;
			}else{
				StructBaseUserInfo userinfo=new StructBaseUserInfo();
				String userid=(String)user.get("userid");
				String coord=(String)user.get("coord");
				System.out.println("coord:"+coord);
				String coor[]=null;
				List coorlist=new LinkedList();
				if(coord!=null&&!"".equals(coord)){
					coor=coord.split(",");
					coorlist.add(coor[0]);
					coorlist.add(coor[1]);
					userinfo.setCoordinates(coorlist);
				}else{
					userinfo.setCoordinates(null);
				}
				userinfo.setUserid(user.get("userid")+"");
				userinfo.setSex(user.get("sex")+"");
				userinfo.setAge(user.get("age")+"");
				userinfo.setStature(user.get("height")+"");
				userinfo.setIfHaveChildren(user.get("ifchild")+"");
				userinfo.setIfMindHaveChildren(user.get("ifmind")+"");
				userinfo.setSubstanceNeeds(user.get("thingask")+"");
				userinfo.setInLovePeriod(user.get("marrynum")+"");
				userinfo.setFaith(user.get("faith")+"");
				userinfo.setSpareTime(user.get("freetime")+"");
				userinfo.setMBTI(user.get("nature")+"");
				userinfo.setNickname(user.get("nickname")+"");
				userinfo.setBirthday(user.get("birthday")+"");
				userinfo.setUserimage(user.get("imageurl")+"");
				System.out.println("空闲时间："+user.get("marrynum")+"");
				res.setData(userinfo);
				res.setRespCode("E00");
				res.setRespMsg("登录成功!");
			}
		} catch (Exception e) {
			res.setRespCode("E02");
			res.setRespMsg("数据库异常！");
			e.printStackTrace();
		}
		getjson = new Gson().toJson(res);
		System.out.println("getjson:"+getjson);
		return getjson;
	}
	
	//获取随机的题库问题，更新用户的题库userid对应的题库userid,返回MBTI性格测试的四个题目
	 public String getQueGroup(String userid){
		Quegroup quegroup=new Quegroup();
		List<Question> questions=new LinkedList<Question>();
		String getjson="";
		List useridList;
		String groupid;
		try {
			String sql = "select username,password from hd_user where userid = ?";
			useridList = db.QueryForList(sql, new Object[]{userid});
			if(useridList.size()<1){
				quegroup.setRespCode("E01");
				quegroup.setRespMsg("没有对应的用户");
				getjson = new Gson().toJson(quegroup);
				return getjson;
			}
			//随机获取题库的唯一标示信息
			String groupsql="SELECT DISTINCT quegroupnum from hd_quegroup order by RAND()";
			List grouplist=db.QueryForList(groupsql, null);
			if(grouplist.size()>0){
				groupid=((Map)grouplist.get(0)).get("quegroupnum")+""; //获取随机的题库标示
			}else{
				quegroup.setRespCode("E02");
				quegroup.setRespMsg("没有对应的题库");
				getjson = new Gson().toJson(quegroup);
				return getjson;
			}
			
			//查询对应的题目编号
			String queidsql="select questionid from hd_quegroup where quegroupnum='"+groupid+"'";
			List queidlist=db.QueryForList(queidsql, null);
			quegroup.setRespCode("E00");
			Question question=null;
			for(int i=0;i<queidlist.size();i++){
				question=new Question();
				String questionid=((Map)queidlist.get(i)).get("questionid")+"";
				question.setQuesionId(questionid);
				//查询题目
				String quessql="select id,quetype,queproblem,anewersnum from hd_question where id="+questionid+"";
				List queslist=db.QueryForList(quessql, null);
				//查询题目
				for(int j=0;j<queslist.size();j++){
					String id=((Map)queslist.get(j)).get("id")+"";
					String quetype=((Map)queslist.get(j)).get("quetype")+"";
					String queproblem=((Map)queslist.get(j)).get("queproblem")+"";
					String anewersnum=((Map)queslist.get(j)).get("anewersnum")+"";
					question.setQuestion(queproblem);
					//查询每到题的答案
					String answersql="select anwer,anerrtype from hd_anewers where anewersnum='"+anewersnum+"'";
					List answerlist=db.QueryForList(answersql, null);
					List answers=new LinkedList();
					List answerstype=new LinkedList();
					//查询答案list和答案类型list
					for(int k=0;k<answerlist.size();k++){
						String anwer=((Map)answerlist.get(k)).get("anwer")+"";
						String anerrtype=((Map)answerlist.get(k)).get("anerrtype")+"";
						answers.add(anwer);
						answerstype.add(anerrtype);
					}
					question.setAnswers(answers);
					question.setAnswerstype(answerstype);
				} 
				questions.add(question);
				quegroup.setData(questions);
			}
			quegroup.setCurrentAnswerIndex("1");
			quegroup.setRespMsg("获取数据成功");
		    getjson = new Gson().toJson(quegroup);
			//将当前用户对应的题库更新
			String upgroupsql="update hd_user set quegroupid='"+groupid+"' where userid='"+userid+"'";
			db.exeUpdate(upgroupsql, null);
		} catch (SQLException e) {
			quegroup.setRespCode("E03");
			quegroup.setRespMsg("数据异常");
			getjson = new Gson().toJson(quegroup);
			e.printStackTrace();
		}
		return getjson;
	 }
	 
	 /**
	  * 修改并返回User对象
	  * @param userMsg
	  * @return
	 * @throws ParseException 
	  */
	 public HdUser updateUser(String userMsg) throws Exception{
		SimpleDateFormat sdm=new SimpleDateFormat ("yyyy-MM-dd");
		//step 1.把json数组的字符串转换为Map集合
		StructBaseUserInfo ss =  new Gson().fromJson(userMsg,StructBaseUserInfo.class);
		//step 2.根据userId获取相应user对象
		HdUser user1 = userDao.getUser(ss.getUserid());
		//1.性别
		int uSex = 1;
		String sSex = ss.getSex();
		if(sSex!=null&&!"".equals(sSex)&&!"null".equals(sSex)) uSex = Integer.parseInt(sSex);
		user1.setSex(uSex);
		//2.出生年月
		Date uBirthday=null;
		String sBirthday = ss.getBirthday();
		if(sBirthday!=null&&!"".equals(sBirthday)&&!"null".equals(sBirthday)) uBirthday= sdm.parse(sBirthday);
		user1.setBirthday(uBirthday);
		//根据出生年月自动计算年龄
		int age=0;
		if(uBirthday!=null&&!"".equals(uBirthday)&&!"null".equals(uBirthday)) age=ReqServiceImpl.getAge(uBirthday);
		user1.setAge(age);
		System.out.println("age:"+age);
		//3.身高
		int uHeight = 0;
		String sHeight = ss.getStature();
		if(sHeight!=null&&!"".equals(sHeight)&&!"null".equals(sHeight)) uHeight = Integer.parseInt(sHeight);
		user1.setHeight(uHeight);
		//4.是否有小孩
		int uIfHaveChild = 1;
		String sIfHaveChild = ss.getIfHaveChildren();
		if(sIfHaveChild!=null&&!"".equals(sIfHaveChild)&&!"null".equals(sIfHaveChild)) uIfHaveChild = Integer.parseInt(sIfHaveChild);
		user1.setIfchild(uIfHaveChild);
		//5.是否介意有小孩
		int uIfMindHaveChildren = 1;
		String sIfMindHaveChildren = ss.getIfMindHaveChildren();
		if(sIfMindHaveChildren!=null&&!"".equals(sIfMindHaveChildren)&&!"null".equals(sIfMindHaveChildren)) uIfMindHaveChildren = Integer.parseInt(sIfMindHaveChildren);
		user1.setIfmind(uIfMindHaveChildren);
		//6.物质要求
		int uSubstanceNeeds = 2;
		String sSubstanceNeeds = ss.getSubstanceNeeds();
		if(sSubstanceNeeds!=null&&!"".equals(sSubstanceNeeds)&&!"null".equals(sSubstanceNeeds)) uSubstanceNeeds = Integer.parseInt(sSubstanceNeeds);
		user1.setThingask(uSubstanceNeeds);
		//7.恋爱期限
		int uInLovePeriod = 1;
		String sInLovePeriod = ss.getInLovePeriod();
		if(sInLovePeriod!=null&&!"".equals(sInLovePeriod)&&!"null".equals(sInLovePeriod)) uInLovePeriod = Integer.parseInt(sInLovePeriod);
		user1.setMarrynum(uInLovePeriod);
		//8.信仰
		String sFaith = ss.getFaith();
		user1.setFaith(sFaith);
		//9.位置
		List Coordinates = ss.getCoordinates();
		System.out.println("位置:"+Coordinates);
		Object obj[] = null;
		if(!Coordinates.isEmpty()){
			 obj=Coordinates.toArray();
			 user1.setCoord(obj[0]+","+obj[1]);
		}
		//10.空余时间
		int uSpareTime = 0;
		String sSpareTime = ss.getSpareTime();
		if(sSpareTime!=null&&!"".equals(sSpareTime)&&!"null".equals(sSpareTime)) uSpareTime = Integer.parseInt(sSpareTime);
		user1.setFreetime(uSpareTime);
		//11.性格类型
		int uMBTI =0;
		String sMBTI = ss.getMBTI();
		if(sMBTI!=null&&!"".equals(sMBTI)&&!"null".equals(sMBTI)) uMBTI = Integer.parseInt(sMBTI);
		user1.setNature(uMBTI);
		//12.昵称
		System.out.println("昵称："+ss.getNickname());
		String sNickname=new String(ss.getNickname().getBytes("iso-8859-1"),"UTF-8");
		System.out.println("sNickname:"+sNickname);
		user1.setNickname(sNickname);
		
		//13.体重
		int meter = 0;
		String sMeter = ss.getMeter();
		if(sMeter!=null&&!"".equals(sMeter)&&!"null".equals(sMeter)) meter = Integer.parseInt(sMeter);
		user1.setMeter(meter);
		
		//13.诚意
		int vipType = 1;
		String svipType = ss.getVipType();
		if(svipType!=null&&!"".equals(svipType)&&!"null".equals(svipType)) vipType = Integer.parseInt(svipType);
		user1.setMeter(vipType);
		
		//婚恋状态
		int marryStatus = 1;
		String smarryStatus = ss.getMarrigestatus();
		if(smarryStatus!=null&&!"".equals(smarryStatus)&&!"null".equals(smarryStatus)) marryStatus = Integer.parseInt(smarryStatus);
		user1.setMeter(marryStatus);
		userDao.onlyUpadte(user1); 
		return user1;
	 }
	
	 public String getUser(String userMsg){
		System.out.println("userMsg:"+userMsg);
		HdUser user = new HdUser();
	    //step 1.更新并获取用户
	    try {
			user = updateUser(userMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//---end
		System.out.println("id:"+user.getId());
		//step 2.获取性别,年龄，是否有小孩
		int sex = user.getSex();
		int age = user.getAge();
		int ifChild = user.getIfchild();
		System.out.println("ifChild:"+ifChild);
		System.out.println("sex:"+sex);
		int height=user.getHeight();
		String coord=user.getCoord();
		//step 3.根据性别，年龄，是否有小孩删选会员
		List<HdUser> userList = userDao.getHdUserbyAgeAndSexAndChild(sex,age,ifChild,height);
		//step 4.智能匹配会员并以json格式返回前5名
		userList = userDao.matchUser(user,userList);
		String getjson="";
		if(userList.size()==0){
			Map uMap = new HashMap();
			uMap.put("respCode", "E01");
			uMap.put("respMsg", "匹配失败");
			getjson = new Gson().toJson(uMap);
			return getjson.replace("\\", "");
		}
		List<Map> usList = new ArrayList<Map>();
		for(int i=0;i<userList.size();i++){
			Map userMap = new HashMap();
			String newcoord= userList.get(i).getCoord();
			String str[]=null;
			String coor[]=null;
			if(newcoord!=""&&!"".equals(newcoord)&&coord!=""&&!"".equals(coord)){
				str=newcoord.split(",");
				coor=coord.split(",");
				System.out.println(i+":"+str[0]+";"+str[1]);
				System.out.println(i+":"+coor[0]+";"+coor[1]);
				userMap.put("distance",Tool.Distance(Double.parseDouble(str[0]),Double.parseDouble(str[1]),Double.parseDouble(coor[0]),Double.parseDouble(coor[1])));
			}else{
				userMap.put("distance","");
			}
			userMap.put("userid", userList.get(i).getUserid());
			userMap.put("nickname", userList.get(i).getNickname());
			userMap.put("age", userList.get(i).getAge());
			userMap.put("imageurl", userList.get(i).getImageurl());
			userMap.put("address", userList.get(i).getAddress());
			userMap.put("mbti", userList.get(i).getNature());
			usList.add(userMap);
		}
		Map uMap = new HashMap();
		uMap.put("respCode", "E00");
		uMap.put("respMsg", "匹配成功");
		uMap.put("data", usList);
		getjson = new Gson().toJson(uMap);
		System.out.println("返回匹配数据："+getjson.toString());
		return getjson;
	}
	 
	public String getImageurl(String userid, String imageurl){
		Map uMap = new HashMap();
		String getjson="";
		if(userid.equals("")){
			uMap.put("respCode", "E01");
			uMap.put("respMsg", "上传失败");
			getjson = new Gson().toJson(uMap);
		}else{
			HdUser upuser = userDao.getUser(userid);
			upuser.setImageurl(imageurl);
			userDao.onlyUpadte(upuser); 
			uMap.put("respCode", "E00");
			uMap.put("respMsg", "上传成功");
			getjson = new Gson().toJson(uMap);
		}
		return getjson.toString();
	}
	 
	public int setQueGroup(String userid) throws SQLException{
		String EI[]=new String[]{"1001","1003","1006","1009","1015","1013","1024"};
		String JP[]=new String[]{"1002","1016","1018","1021","1022","1026","1027"};
		String FT[]=new String[]{"1004","1005","1008","1011","1012","1028","1010"};
		String NS[]=new String[]{"1007","1014","1017","1019","1020","1025","1013"};
		Random rand=new Random();
		HashSet<Integer> set = new HashSet<Integer>();
		while(set.size()<3){
			set.add(rand.nextInt(6));
		}
		StringBuffer sb=new StringBuffer();
		Iterator iter=set.iterator();
		while(iter.hasNext()){
			int k=(Integer)iter.next();
			sb.append("insert into hd_quegroup(questionid,quetiontype,quegroupnum) values ("+EI[k]+",'EI','"+userid+"');");
		    sb.append("insert into hd_quegroup(questionid,quetiontype,quegroupnum) values ("+JP[k]+",'JP','"+userid+"');");
			sb.append("insert into hd_quegroup(questionid,quetiontype,quegroupnum) values ("+FT[k]+",'FT','"+userid+"');");
			sb.append("insert into hd_quegroup(questionid,quetiontype,quegroupnum) values ("+NS[k]+",'NS','"+userid+"');");
		}
		System.out.println(sb.toString());
		db.exeInserts(sb.toString());
		return 0;
	 }
	
	public static int getAge(Date birthDate) {
		  if (birthDate == null)
		   throw new RuntimeException("出生日期不能为null");
		  int age = 0;
		  Date now = new Date();
		  SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		  SimpleDateFormat format_M = new SimpleDateFormat("MM");
		  String birth_year = format_y.format(birthDate);
		  String this_year = format_y.format(now);
		  String birth_month = format_M.format(birthDate);
		  String this_month = format_M.format(now);
		  // 初步，估算
		  age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
		  // 如果未到出生月份，则age - 1
		  if (this_month.compareTo(birth_month) < 0)
		   age -= 1;
		  if (age < 0)
		   age = 0;
		  return age;
		 }

	 
	public static void main(String[] args) {
		//String str="{\"Coordinates\":[\"114.274776\",\"22.596344\"],\"userid\":\"4185CD236C6421AE\",\"Faith\":\"A\",\"IfHaveChildren\":\"1\",\"IfMindHaveChildren\":\"1\",\"InLovePeriod\":\"1\",\"MBTI\":\"2\",\"SpareTime\":\"87\",\"SubstanceNeeds\":\"1\",\"age\":\"21\",\"sex\":\"1\",\"stature\":\"150\"}";
		ReqServiceImpl service = new ReqServiceImpl();
		StringBuffer sb=new StringBuffer();
		sb.append("{\"Coordinates\":[\"119.274776\",\"27.596344\"],");   //坐标
		sb.append("\"userid\":\"725AB8E4D5065BE5\",");    //用户userid
		sb.append("\"Faith\":\"B\",");     //信仰
		sb.append("\"IfHaveChildren\":\"1\",");    //是否有孩子 1、有小孩 2、没小孩
		sb.append("\"IfMindHaveChildren\":\"1\",");   //是否介意有孩子  1.介意 2、不介意
		sb.append("\"InLovePeriod\":\"1\",");    //恋爱期限  1、闪婚，2、三个月左右，3、半年左右，4、 一年左右，5、不着急结婚。
		sb.append("\"MBTI\":\"2\",");    //MBTI性格类型  1、NT ，2、NF ，3、SJ ，4、SP
		sb.append("\"SpareTime\":\"87\",");   //空余时间  二进制表示00000001,共八位二进制数，前七位代表星期，比如第一位是1代表星期一，第二位为1代表星期二。
		sb.append("\"SubstanceNeeds\":\"2\",");  //物质要求  1、有房 2、没房
		sb.append("\"age\":\"24\",");   //年龄 
		sb.append("\"sex\":\"1\",");   //性别 1、男 2、女
		sb.append("\"nickname\":\"你好\",");   //性别 1、男 2、女
		sb.append("\"stature\":\"150\"}");    //身高
		System.out.println(service.register("1234567", "admin")); //注册
//		System.out.println(service.getUserid("12527923701", "admin")); //登录  
//		System.out.println("题库:"+service.getQueGroup("5181E17054A17769")); //获取题目
//		System.out.println("返回的5个人信息:"+service.getUser(sb.toString())); //保存结果并返回信息
	}

}
