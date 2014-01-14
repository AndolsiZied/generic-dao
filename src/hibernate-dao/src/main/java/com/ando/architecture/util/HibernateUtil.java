package com.ando.architecture.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Class utilities that takes care of startup and makes {@link Session} management easier.
 * 
 * @author andolsi
 * 
 */
public abstract class HibernateUtil {

	/**
	 * sessionFactory attribute.
	 */
	private static final SessionFactory SESSION_FACTORY;

	/**
	 * Default constructor that avoid class instantiation.
	 */
	private HibernateUtil() {

	}

	static {
		Configuration configuration = new Configuration().configure();
		SESSION_FACTORY = configuration.buildSessionFactory();
	}

	/**
	 * Opens new {@link Session}.
	 * 
	 * @return {@link Session}
	 */
	public static Session openSession() {
		return SESSION_FACTORY.openSession();
	}

	/**
	 * SessionFactory getter.
	 * 
	 * @return the sessionfactory
	 */
	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

}
