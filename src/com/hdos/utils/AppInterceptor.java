package com.hdos.utils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;



public class AppInterceptor implements Interceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		System.out.println("-------hibernate Transaction begin--------");
	    HibernateSessionFactory.beginTransaction();
	    String r = invocation.invoke();
	    try {
	    	if(HibernateSessionFactory.isTransactionActive()){
	    		System.out.println("commit");
	    		HibernateSessionFactory.commit();
			};
	    	
		} catch (Exception e) {
			if(HibernateSessionFactory.isTransactionActive()){
				HibernateSessionFactory.rollback();
			};
			e.printStackTrace();
			throw e;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	    
		return r;
	}

	

}
