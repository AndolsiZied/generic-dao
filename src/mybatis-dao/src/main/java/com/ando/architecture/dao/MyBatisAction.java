package com.ando.architecture.dao;

import org.apache.ibatis.session.SqlSession;

import com.ando.architecture.exception.DataBaseException;

/**
 * Represents a mybatis action to be taken.
 * 
 * @author Zied ANDOLSI
 * 
 * @param <Type>
 *            Object's type
 */
public interface MyBatisAction<Type> {

	/**
	 * Execute the action.
	 * 
	 * @param sqlSession
	 *            mybatis sqlSession
	 * @return object
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to execute the action.
	 */
	Type apply(SqlSession sqlSession) throws DataBaseException;

}
