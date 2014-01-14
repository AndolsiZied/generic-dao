package com.ando.architecture.dao;

import org.hibernate.Session;

import com.ando.architecture.exception.DataBaseException;

/**
 * Represents an hibernate action to be taken.
 * 
 * @author Zied ANDOLSI
 * 
 * @param <Type>
 *            Object's type
 */
public interface HibernateAction<Type> {

	/**
	 * Execute the action.
	 * 
	 * @param session
	 *            hibernate session
	 * @return object
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to execute the action.
	 */
	Type apply(Session session) throws DataBaseException;

}
