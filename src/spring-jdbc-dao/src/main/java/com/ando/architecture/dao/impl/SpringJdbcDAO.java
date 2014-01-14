package com.ando.architecture.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ando.architecture.dao.DAO;
import com.ando.architecture.exception.DataBaseException;

/**
 * This class provides the basic implementations needed to make Object-Relational operations on databases, using spring
 * jdbc.
 * 
 * @author Zied ANDOLSI
 * 
 * @param <Type>
 *            Entity's type.
 * @param <IdType>
 *            Entity identifier's type.
 */
public abstract class SpringJdbcDAO<Type, IdType extends Serializable> extends JdbcDaoSupport implements
		DAO<Type, IdType> {

	/**
	 * {@link SpringJdbcDAO}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringJdbcDAO.class);

	/**
	 * Entity's type this instance has to manage.
	 */
	protected Class<Type> typeClass;

	/**
	 * The table name of the mapped entity.
	 */
	private String tableName;

	/**
	 * The column id name for the table of the mapped entity.
	 */
	private String columnIdName;

	/**
	 * The constructor {@link #SpringJdbcDAO(Class, String, String)} instantiates a new {@link SpringJdbcDAO} object
	 * keeping in mind the type of the class to manage, the table name and the column id name.
	 * <p>
	 * This constructor must be called by all the inheriting classes.
	 * 
	 * @param typeClass
	 *            the entity's type this DAO instance has to manage.
	 * @param tableName
	 *            table name
	 * @param columnIdName
	 *            column id name
	 */
	public SpringJdbcDAO(Class<Type> typeClass, String tableName, String columnIdName) {
		this.typeClass = typeClass;
		this.columnIdName = columnIdName;
		this.tableName = tableName;
	}

	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unchecked")
	public Type save(Type entity) throws DataBaseException {
		LOGGER.debug("Starting save method...");

		if (entity == null) {
			LOGGER.error("The entity [ " + typeClass.getSimpleName() + " ] to save cannot be null.");
			throw new IllegalArgumentException("The entity " + typeClass.getSimpleName() + " to save cannot be null.");
		}

		try {
			LOGGER.debug("Saving an entity [ " + typeClass.getSimpleName() + " ]...");

			SimpleJdbcInsert insertActor = new SimpleJdbcInsert(getDataSource()).withTableName(tableName);
			Map<String, Object> parameters = new HashMap<String, Object>();
			String[] columnNames = getColumnNames();
			Object[] columnValues = getColumnValues(entity);
			for (int i = 0; i < columnNames.length; i++) {
				parameters.put(columnNames[i], columnValues[i]);
			}

			if (getIdentifier(entity) != null) {
				parameters.put(columnIdName, getIdentifier(entity));
				insertActor.execute(parameters);
			} else {
				insertActor = insertActor.usingGeneratedKeyColumns(columnIdName);
				Number newId = insertActor.executeAndReturnKey(parameters);
				setIdentifier(entity, (IdType) newId);
			}

			return entity;
		} catch (Exception e) {
			LOGGER.error("Error occurred when trying to save the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("Error occurred when trying to save the entity [ " + typeClass.getSimpleName()
					+ " ].", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Type findOne(IdType id) throws DataBaseException {
		LOGGER.debug("Starting findOne method...");

		if (id == null) {
			LOGGER.error("The identifier can not be null.");
			throw new IllegalArgumentException("The identifier can not be null.");
		}

		try {
			LOGGER.debug("Getting and returning the entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ id.toString() + " ]...");
			String query = "select * from " + tableName + " where " + columnIdName + " = ?";
			List<Type> results = getJdbcTemplate().query(query, new Object[]{ id }, getRowMapper());
			if (results != null && results.size() > 0) {
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("Error occured when trying to load the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("Error occured when trying to load the entity [ " + typeClass.getSimpleName()
					+ " ].");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Type> findAll() throws DataBaseException {
		LOGGER.info("Starting findAll method...");

		try {
			LOGGER.debug("Getting all entities [ " + typeClass.getSimpleName() + " ]...");
			String query = "select * from " + tableName;
			return getJdbcTemplate().query(query, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("Error occured when trying to load all entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("Error occured when trying to load all entity [ " + typeClass.getSimpleName()
					+ " ].");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Type update(Type entity) throws DataBaseException {
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
			LOGGER.debug("Updating the entity [ " + typeClass.getSimpleName() + " ]...");

			String[] columnsNames = getColumnNames();
			Object[] columnValues = getColumnValues(entity);
			StringBuilder query = new StringBuilder("update " + tableName + " set ");
			for (int i = 0; i < columnsNames.length; i++) {
				query.append(columnsNames[i] + " =?");
				if (i < columnsNames.length - 1) {
					query.append(", ");
				}
			}
			query.append(" where " + columnIdName + " = ?");
			columnValues = ArrayUtils.add(columnValues, getIdentifier(entity));
			getJdbcTemplate().update(query.toString(), columnValues);

			LOGGER.debug("The entity [ " + typeClass.getSimpleName() + " ] has been successfully updated.");

			return entity;

		} catch (Exception e) {
			LOGGER.error("Error occured when trying to update the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("Error occured when trying to update the entity [ " + typeClass.getSimpleName()
					+ " ].");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Type entity) throws DataBaseException {
		LOGGER.info("Starting delete method...");

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
			LOGGER.debug("Deleting the entity " + typeClass.getSimpleName() + " with identifier "
					+ getIdentifier(entity) + "...");

			String query = "delete from " + tableName + " where " + columnIdName + " = ?";
			getJdbcTemplate().update(query, new Object[]{ getIdentifier(entity) });
		} catch (Exception e) {
			LOGGER.error("Error occured when trying to delete the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("Error occured when trying to delete the entity [ " + typeClass.getSimpleName()
					+ " ].", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Type> getAll() throws DataBaseException {
		LOGGER.info("Starting getAll method...");

		try {
			LOGGER.debug("Getting all entities [ " + typeClass.getSimpleName() + " ]...");

			String query = "select * from " + tableName;
			List<Type> results = getJdbcTemplate().query(query, getRowMapper());

			return (results != null ? results : new ArrayList<Type>());

		} catch (Exception e) {
			LOGGER.error("Error occured when trying to load all entity [ " + this.getTypeClass().getSimpleName()
					+ " ].", e);
			throw new DataBaseException("Error occured when trying to load all entity [ " + typeClass.getSimpleName()
					+ " ].");
		}
	}

	/**
	 * Execute a SQL select request with an unique result of type <code>Type</code>.
	 * 
	 * @param request
	 *            the request to execute.
	 * @param params
	 *            the parameters of the request. Parameters are included in order of position.
	 * 
	 * @return the entity found, <code>null</code> if no entity has been found.
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to retrieve entity in the database.
	 */
	protected Type executeSingleResult(final String request, final Object... params) throws DataBaseException {

		List<Type> results = null;
		try {
			results = getJdbcTemplate().query(request, params, getRowMapper());
			if (results == null || results.isEmpty()) {
				return null;
			}

		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to execute query [ " + request + " ].", e);
			throw new DataBaseException("An error has occurred when trying to execute query [ " + request + " ].", e);
		}

		if (results.size() > 1) {
			LOGGER.error("There is more than one matching result.");
			throw new DataBaseException("There is more than one matching result.");
		}
		return results.get(0);
	}

	/**
	 * Execute a SQL request that the result is a {@link List} of entities.
	 * 
	 * @param request
	 *            the request to execute.
	 * @param params
	 *            the parameters of the request. Parameters are included in order of position.
	 * 
	 * @return the {@link List} of entities found. An empty {@link List} is returned if no result has been found.
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to execute query in the database.
	 */
	protected List<Type> executeResultList(final String request, final Object... params) throws DataBaseException {

		try {
			List<Type> results = getJdbcTemplate().query(request, params, getRowMapper());

			return (results != null ? results : new ArrayList<Type>());

		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to execute query [ " + request + " ].", e);

			throw new DataBaseException("An error has occurred when trying to execute query [ " + request + " ].", e);
		}

	}

	/**
	 * The method {@link #getColumnValues()} returns an array of column's value.
	 * 
	 * @param entity
	 *            entity.
	 * @return column's values.
	 */
	public abstract Object[] getColumnValues(Type entity);

	/**
	 * The method {@link #getColumnNames()} returns an array of column's names.
	 * 
	 * @return column's names.
	 */
	public abstract String[] getColumnNames();

	/**
	 * The method {@link #getRowMapper()} returns entity's row mapper.
	 * 
	 * @return {@link RowMapper}
	 */
	public abstract RowMapper<Type> getRowMapper();

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
	 * {@inheritDoc}
	 */
	public Class<Type> getTypeClass() {
		return this.typeClass;
	}

}
