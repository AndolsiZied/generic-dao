package com.ando.architecture.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ando.architecture.dao.DAO;
import com.ando.architecture.dao.MyBatisAction;
import com.ando.architecture.exception.DataBaseException;
import com.ando.architecture.util.MybatisUtil;

/**
 * This class provides the basic implementations needed to make Object-Relational operations on databases, using the
 * MyBatis framework.
 * 
 * @author Zied ANDOLSI
 * 
 * @param <Type>
 *            Entity's type this instance has to manage.
 * @param <IdType>
 *            Entity identifier's type this instance has to manage.
 */
public abstract class MyBatisDAO<Type, IdType extends Serializable> implements DAO<Type, IdType> {

	/**
	 * {@link MyBatisDAO}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisDAO.class);

	/**
	 * Entity's type this instance has to manage.
	 */
	protected Class<Type> typeClass;

	/**
	 * The column id name for the table of the mapped entity.
	 */
	private String columnIdName;

	/** Default namespace in mapper files. */
	protected static final String NAMESPACE = "mappers";

	/** prefix of find queries in mapper files. */
	protected static final String PREFIX_FIND_QUERY = "find";

	/** prefix of create queries in mapper files. */
	protected static final String PREFIX_INSERT_QUERY = "create";

	/** prefix of update queries in mapper files. */
	protected static final String PREFIX_UPDATE_QUERY = "update";

	/** prefix of delete queries in mapper files. */
	protected static final String PREFIX_DELETE_QUERY = "delete";

	/** prefix of select queries in mapper files. */
	protected static final String PREFIX_SELECT_QUERY = "select";

	/**
	 * The constructor {@link #SimpleMyBatisDAO(Class)} instantiates a new {@link MyBatisDAO} object keeping in mind the
	 * type of the class to manage.
	 * <p>
	 * This constructor must be called by all the inheriting classes.
	 * 
	 * @param typeClass
	 *            Entity's type this DAO instance has to manage.
	 * @param columnIdName
	 *            column id name
	 */
	protected MyBatisDAO(Class<Type> typeClass, String columnIdName) {
		this.typeClass = typeClass;
		this.columnIdName = columnIdName;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Type findOne(final IdType id) throws DataBaseException {
		LOGGER.debug("Starting findOne method...");

		if (id == null) {
			LOGGER.error("The identifier can not be null.");
			throw new IllegalArgumentException("The identifier can not be null.");
		}

		try {

			LOGGER.debug("Getting and returning the entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ id.toString() + " ]...");
			return executeAction(new MyBatisAction<Type>() {

				@Override
				public Type apply(SqlSession session) throws DataBaseException {
					String query = NAMESPACE + "." + PREFIX_FIND_QUERY + typeClass.getSimpleName();
					return (Type) session.selectOne(query, id);
				}
			}, false);

		} catch (Exception e) {
			LOGGER.error(
					"An error has occured while getting the entity [ " + typeClass + " ] with identifier ["
							+ id.toString() + " ].", e);
			throw new DataBaseException("An error has occured while getting the entity " + typeClass + " ["
					+ id.toString() + " ].", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Type save(final Type entity) throws DataBaseException {
		LOGGER.debug("Starting save method...");

		if (entity == null) {
			LOGGER.error("The entity [ " + typeClass.getSimpleName() + " ] to save cannot be null.");
			throw new IllegalArgumentException("The entity " + typeClass.getSimpleName() + " to save cannot be null.");
		}

		try {
			LOGGER.debug("Saving an entity [ " + typeClass.getSimpleName() + " ]...");

			return executeAction(new MyBatisAction<Type>() {

				@Override
				public Type apply(SqlSession session) throws DataBaseException {
					String query = NAMESPACE + "." + PREFIX_INSERT_QUERY + typeClass.getSimpleName();
					Map<String, Object> params = getEntityMap(entity);
					session.insert(query, params);
					IdType identifier = (IdType) params.get(columnIdName);
					LOGGER.debug("The entity [ " + typeClass.getSimpleName() + " ] with identifier [ " + identifier
							+ " ] has been successfully saved.");
					setIdentifier(entity, identifier);
					return entity;
				}
			}, true);

		} catch (Exception e) {
			LOGGER.error("An error has occured while saving the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("An error has occured while saving the entity " + typeClass.getSimpleName()
					+ ".", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public Type update(final Type entity) throws DataBaseException {
		LOGGER.debug("Starting update method...");

		if (entity == null) {
			LOGGER.error("The entity [ " + typeClass.getSimpleName() + " ] to update cannot be null.");
			throw new IllegalArgumentException("The entity " + typeClass + " to update cannot be null.");
		}

		if (getIdentifier(entity) == null) {
			LOGGER.error("The identifier of the entity [ " + typeClass.getSimpleName() + " ] to update cannot be null.");
			throw new IllegalArgumentException("The identifier of the entity [ " + typeClass.getSimpleName()
					+ " ] to update cannot be null.");
		}

		Type oldEntity = findOne(getIdentifier(entity));
		if (oldEntity == null) {
			throw new DataBaseException("No entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ getIdentifier(entity) + " ] found.");
		}

		try {
			LOGGER.debug("Updating the entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ getIdentifier(entity) + " ]...");

			executeAction(new MyBatisAction<Type>() {

				@Override
				public Type apply(SqlSession session) throws DataBaseException {
					String query = NAMESPACE + "." + PREFIX_UPDATE_QUERY + typeClass.getSimpleName();
					session.update(query, getEntityMap(entity));
					return null;
				}
			}, true);

			LOGGER.info("The entity " + typeClass.getSimpleName() + " has been successfully updated.");
			return entity;

		} catch (Exception e) {
			LOGGER.error("An error has occured while updating the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("An error has occured while updating the entity [ " + typeClass.getSimpleName()
					+ " ].", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(final Type entity) throws DataBaseException {
		LOGGER.debug("Starting delete method...");

		if (entity == null) {
			LOGGER.error("The entity [ " + typeClass.getSimpleName() + " ] to delete cannot be null.");
			throw new IllegalArgumentException("The entity [ " + typeClass.getSimpleName()
					+ " ] to delete cannot be null.");
		}

		if (getIdentifier(entity) == null) {
			LOGGER.error("The identifier of the entity [ " + typeClass.getSimpleName() + " ] to delete cannot be null.");
			throw new IllegalArgumentException("The identifier of the entity [ " + typeClass.getSimpleName()
					+ " ] to delete cannot be null.");
		}

		try {

			LOGGER.debug("Deleting the entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ getIdentifier(entity) + " ]...");

			executeAction(new MyBatisAction<Type>() {

				@Override
				public Type apply(SqlSession session) throws DataBaseException {
					String query = NAMESPACE + "." + PREFIX_DELETE_QUERY + typeClass.getSimpleName();
					session.delete(query, entity);
					return null;
				}
			}, true);

			LOGGER.debug("The entity [ " + typeClass.getSimpleName() + " ] with identifier [ " + getIdentifier(entity)
					+ " ] has been successfully deleted.");

		} catch (Exception e) {
			LOGGER.error("An error has occured while deleting the entity [ " + typeClass.getSimpleName()
					+ " ] with identifier [ " + getIdentifier(entity) + " ].", e);
			throw new DataBaseException("An error has occured while deleting the entity [ " + typeClass.getSimpleName()
					+ " ] with identifier [ " + getIdentifier(entity) + " ].", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Type> getAll() throws DataBaseException {
		LOGGER.debug("Starting getAll method...");

		try {

			LOGGER.debug("Getting and returning all entities [ " + typeClass.getSimpleName() + " ]...");
			List<Type> results = executeAction(new MyBatisAction<List<Type>>() {

				@Override
				public List<Type> apply(SqlSession session) throws DataBaseException {
					String query = NAMESPACE + "." + PREFIX_FIND_QUERY + "All" + typeClass.getSimpleName();
					return (List<Type>) session.selectList(query);
				}
			}, false);

			return (results != null ? results : new ArrayList<Type>());

		} catch (Exception e) {
			LOGGER.error("An error has occured while getting all entities [ " + typeClass + " ].", e);
			throw new DataBaseException("An error has occured while getting all entities [ " + typeClass + " ].", e);
		}
	}

	/**
	 * Execute a SQL select request with an unique result of type <code>Type</code>.
	 * 
	 * @param params
	 *            Map containing entity values to corresponding column.
	 * 
	 * @return the entity found, <code>null</code> if no entity has been found.
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to retrieve entity in the database.
	 */
	@SuppressWarnings("unchecked")
	protected Type executeSingleResult(final Map<String, Object> params) throws DataBaseException {

		try {

			return executeAction(new MyBatisAction<Type>() {

				@Override
				public Type apply(SqlSession session) throws DataBaseException {

					String query = NAMESPACE + "." + PREFIX_SELECT_QUERY + "One" + typeClass.getSimpleName();
					return (Type) session.selectOne(query, params);
				}
			}, false);

		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to execute select query.", e);
			throw new DataBaseException("An error has occurred when trying to execute select query.", e);
		}
	}

	/**
	 * Execute a SQL request that the result is a {@link List} of entities.
	 * 
	 * @param params
	 *            Map containing entity values to corresponding column.
	 * 
	 * @return the {@link List} of entities found. An empty {@link List} is returned if no result has been found.
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to execute query in the database.
	 */
	@SuppressWarnings("unchecked")
	protected List<Type> executeResultList(final Object... params) throws DataBaseException {

		try {
			List<Type> results = executeAction(new MyBatisAction<List<Type>>() {

				@Override
				public List<Type> apply(SqlSession session) throws DataBaseException {
					String query = NAMESPACE + "." + PREFIX_SELECT_QUERY + "List" + typeClass.getSimpleName();
					return (List<Type>) session.selectList(query);
				}
			}, false);

			return (results != null ? results : new ArrayList<Type>());

		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to execute select query.", e);

			throw new DataBaseException("An error has occurred when trying to execute select query.", e);
		}
	}

	/**
	 * Maps entity values to corresponding column.
	 * 
	 * @param entity
	 *            entity to map
	 * @return map of entity values.
	 */
	private Map<String, Object> getEntityMap(Type entity) {

		String[] columnsNames = getColumnNames();
		if (null == columnsNames || columnsNames.length == 0) {
			throw new NotImplementedException("No column' name specified");
		}

		Object[] columnsValues = getColumnValues(entity);
		if (null == columnsValues || columnsValues.length == 0) {
			throw new IllegalArgumentException("No column' value specified");
		}

		if (columnsValues.length != columnsNames.length) {
			throw new IllegalArgumentException("Values number different than column number");
		}

		Map<String, Object> mapEntity = new HashMap<String, Object>();
		for (int i = 0; i < columnsValues.length; i++) {
			mapEntity.put(columnsNames[i], columnsValues[i]);
		}
		return mapEntity;

	}

	/**
	 * The method {@link #getColumnValues()} returns an array of column's value.
	 * 
	 * @param entity
	 *            entity.
	 * @return column's values.
	 */
	protected abstract Object[] getColumnValues(Type entity);

	/**
	 * The method {@link #getColumnNames()} returns an array of column's names.
	 * 
	 * @return column's names.
	 */
	protected abstract String[] getColumnNames();

	/**
	 * The method {@link #setIdentifier()} sets the identifier received as parameter. It is only supposed to call the
	 * setter of entity identifier.
	 * <p>
	 * This method should not be implemented in a generic way, but by the final implementation.
	 * 
	 * @param entity
	 *            Entity for which setting the identifier.
	 * @param id
	 *            the identifier to set.
	 */
	public abstract void setIdentifier(Type entity, IdType id);

	/**
	 * Execute the action received as parameter.
	 * 
	 * @param action
	 *            object that specifies the MyBatis action
	 * @param readOnly
	 *            if this parameter is true the transaction is read-only
	 * @param <T>
	 *            object's returned type.
	 * @return a result object returned by the action.
	 * @throws DataBaseException
	 *             thrown if error occurred when trying to execute the action.
	 */
	public <T> T executeAction(MyBatisAction<T> action, boolean readOnly) throws DataBaseException {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSession();
			T results = action.apply(session);
			if (readOnly) {
				session.commit();
			}
			return results;
		} catch (DataBaseException e) {
			if (readOnly && session != null) {
				session.rollback();
			}
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

}
