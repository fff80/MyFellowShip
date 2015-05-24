package com.hdos.service;

public interface ReqService {
   /**
    * 用户注册，返回userid
    * @param username
    * @param pwd
    * @return
    */
   public String register(String username,String pwd);
   
   
   /**
    * 通过username,password 获取用户id
    * @param userId
    * @return
    */
   public String getUserid(String username,String pwd);
   
   
   //获取随机的题库接口
   public String getQueGroup(String userid);
   
   /**
    * 
    * @param userMsg
    * @return
    */
   public String getUser(String userMsg);
   

   
   /**
    *图片保存接口
    * @param userMsg
    * @return
    */
   public String getImageurl(String userid,String imageurl);
}
