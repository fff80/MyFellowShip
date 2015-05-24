package com.hdos.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadlocal = new ThreadLocal<Session>();
	private static Configuration config = new Configuration();
	private static SessionFactory sessionFactory;

	static {
		config.configure(CONFIG_FILE_LOCATION);
		sessionFactory = config.buildSessionFactory();
	}

	private HibernateSessionFactory() {
	};

	public static Session getSession() {
		Session session = (Session) threadlocal.get();
		if (session == null || !session.isOpen()) {
			if (sessionFactory == null) {
				sessionFactory = rebuildSessionFactory();
			}
			session = sessionFactory.openSession();
			threadlocal.set(session);
		}
		return session;
	}

	public static void beginTransaction() {
		getSession().beginTransaction();
	}

	public static void commit() {
		getSession().beginTransaction().commit();
	}

	public static void rollback() {
		getSession().beginTransaction().rollback();
	}

	public static boolean isTransactionActive() {
		Session session = (Session) threadlocal.get();
		if (session == null)
			return false;
		return session.getTransaction().isActive();
	}

	public static void closeSession() {
		Session session = (Session) threadlocal.get();
		if (session != null) {
			session.close();
			threadlocal.set(null);
		}
		;
	}

	public static SessionFactory rebuildSessionFactory() {
		config.configure(CONFIG_FILE_LOCATION);
		sessionFactory = config.buildSessionFactory();
		return sessionFactory;
	}

	public static String getCONFIG_FILE_LOCATION() {
		return CONFIG_FILE_LOCATION;
	}

	public static void setCONFIG_FILE_LOCATION(String cONFIGFILELOCATION) {
		HibernateSessionFactory.CONFIG_FILE_LOCATION = cONFIGFILELOCATION;
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
