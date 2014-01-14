package com.ando.architecture.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ando.architecture.exception.DataBaseException;

/**
 * Class utilities for managing mybatis sql session.
 * 
 * @author Zied ANDOLSI
 * 
 */
public abstract class MybatisUtil {

	/**
	 * {@link MybatisUtil}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisUtil.class);

	/**
	 * Default constructor to avoid class instantiation.
	 */
	private MybatisUtil() {

	}

	/**
	 * mybatis configuration file path.
	 */
	private static final String RESOURCE = "mybatis/mybatis-config.xml";

	/**
	 * Session factory.
	 */
	private static SqlSessionFactory sqlSessionFactory;

	static {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(RESOURCE);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			LOGGER.error("error occurred when trying to load mybatis configuration file [ " + RESOURCE + " ] : "
					+ e.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	/**
	 * Get ibatis sql session.
	 * 
	 * @return {@link SqlSession}.
	 * @throws DataBaseException
	 *             thrown if error occurred when trying to get sql session.
	 */
	public static SqlSession getSession() throws DataBaseException {
		if (sqlSessionFactory == null) {
			throw new DataBaseException("error occurred when trying to create sql session factory");
		}
		return sqlSessionFactory.openSession();
	}
}
