package com.hdos.dao;

import org.hibernate.Session;

import com.hdos.utils.HibernateSessionFactory;

/**
 * Data access object (DAO) for domain model
 * 
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	public void beginTransaction() {
		HibernateSessionFactory.beginTransaction();
	}

	public void commit() {
		Session session = HibernateSessionFactory.getSession();
		session.getTransaction().commit();
	}

	public void rollback() {
		if (HibernateSessionFactory.isTransactionActive())
			HibernateSessionFactory.rollback();
	}

	public void closeSession() {
		HibernateSessionFactory.closeSession();
	}
}