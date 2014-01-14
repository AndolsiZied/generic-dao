package com.ando.architecture.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class utilities that makes {@link EntityManager} management easier.
 * 
 * @author andolsi
 * 
 */
public abstract class JPAUtil {

	/**
	 * Map entityMnageFatcory to persistence unit.
	 */
	private static final Map<String, EntityManagerFactory> EMF_MAP = new HashMap<String, EntityManagerFactory>();

	/**
	 * Default constructor to avoid class instantiation.
	 */
	private JPAUtil() {

	}

	/**
	 * Creates and returns entitymanger of the persistence unit received as parameter.
	 * 
	 * @param persitenceUnit
	 *            persistence unit name
	 * @return {@link EntityManager}
	 */
	public static EntityManager getEntityManager(String persitenceUnit) {
		EntityManagerFactory emf = EMF_MAP.get(persitenceUnit);
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(persitenceUnit);
			EMF_MAP.put(persitenceUnit, emf);

		}
		return emf.createEntityManager();
	}
}
