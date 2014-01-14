package com.ando.architecture.dao;

import javax.persistence.EntityManager;

import com.ando.architecture.exception.DataBaseException;

/**
 * Represents a jpa action to be taken.
 * 
 * @author Zied ANDOLSI
 * 
 * @param <Type>
 *            Object's type
 */
public interface JPAAction<Type> {

	/**
	 * Execute the action.
	 * 
	 * @param em
	 *            entity manager
	 * @return object
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to execute the action.
	 */
	Type apply(EntityManager em) throws DataBaseException;

}
