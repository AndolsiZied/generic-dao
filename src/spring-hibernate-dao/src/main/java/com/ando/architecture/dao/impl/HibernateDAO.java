package com.ando.architecture.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ando.architecture.dao.DAO;
import com.ando.architecture.exception.DataBaseException;




/**
 * This class provides the basic implementations needed to make Object-Relational operations on databases, using the
 * Hibernate framework in a spring context.
 * 
 * @author Zied ANDOLSI
 * 
 * @param <Type>
 *            Entity's type this instance has to manage.
 * @param <IdType>
 *            Entity identifier's type this instance has to manage.
 */
public abstract class HibernateDAO<Type, IdType extends Serializable> extends HibernateDaoSupport implements
		DAO<Type, IdType> {

	/**
	 * {@link HibernateDAO}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateDAO.class);

	/**
	 * Entity's type this instance has to manage.
	 */
	protected Class<Type> typeClass;

	/**
	 * The constructor {@link #SpringDAOHibernate(Class)} instantiates a new {@link HibernateDAO} object keeping
	 * in mind the type of the class to manage.
	 * <p>
	 * This constructor must be called by all the inheriting classes.
	 * 
	 * @param typeClass
	 *            Entity's type this DAO instance has to manage.
	 */
	protected HibernateDAO(Class<Type> typeClass) {
		this.typeClass = typeClass;
	}

	/**
	 * {@inheritDoc}
	 */
	public Type findOne(IdType id) throws DataBaseException {
		LOGGER.debug("starting findOne method...");

		if (id == null) {
			LOGGER.error("The identifier can not be null.");
			throw new IllegalArgumentException("The identifier can not be null.");
		}

		try {

			LOGGER.debug("Getting and returning the entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ id.toString() + " ]...");
			return (Type) getHibernateTemplate().get(typeClass, id);

		} catch (Exception e) {
			LOGGER.error("An error has occured while getting the entity [ " + typeClass + " ] with the identifier [ "
					+ id.toString() + " ].", e);
			throw new DataBaseException("An error has occured while getting the entity [ " + typeClass
					+ " ] with identifier [ " + id.toString() + " ].", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Type save(Type entity) throws DataBaseException {
		LOGGER.debug("Starting save method...");

		if (entity == null) {
			LOGGER.error("The entity [ " + typeClass.getSimpleName() + " ] to save cannot be null.");
			throw new IllegalArgumentException("The entity [ " + typeClass.getSimpleName()
					+ " ] to save cannot be null.");
		}

		try {
			LOGGER.debug("Saving an entity [ " + typeClass.getSimpleName() + " ]...");

			Serializable identifier = getHibernateTemplate().save(entity);

			LOGGER.debug("The entity [ " + typeClass.getSimpleName() + " ] with identifier [ " + identifier
					+ " ] has been successfully saved.");
			LOGGER.debug("Getting and returning the entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ identifier + " ]...");
			return (Type) getHibernateTemplate().get(typeClass, identifier);

		} catch (Exception e) {
			LOGGER.error("An error has occured while saving the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("An error has occured while saving the entity " + typeClass.getSimpleName()
					+ ".", e);
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

		try {
			LOGGER.debug("Updating the entity [ " + typeClass.getSimpleName() + " ]...");

			getHibernateTemplate().update(entity);

			LOGGER.info("The entity [ " + typeClass.getSimpleName() + " ] has been successfully updated.");

			return (Type) getHibernateTemplate().get(typeClass, getIdentifier(entity));

		} catch (Exception e) {
			LOGGER.error("An error has occured while updating the entity [ " + typeClass.getSimpleName() + " ].", e);
			throw new DataBaseException("An error has occured while updating the entity [ " + typeClass.getSimpleName()
					+ " ].", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Type entity) throws DataBaseException {
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

			LOGGER.debug("Deleting the entity [ " + typeClass.getSimpleName() + " ] with identifier "
					+ getIdentifier(entity) + "...");
			getHibernateTemplate().delete(entity);

			LOGGER.debug("The entity [ " + typeClass.getSimpleName() + " ] with identifier " + getIdentifier(entity)
					+ " has been successfully deleted.");

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
			DetachedCriteria criteria = DetachedCriteria.forClass(typeClass);
			List<Type> results = getHibernateTemplate().findByCriteria(criteria);
			return (results != null ? results : new ArrayList<Type>());
		} catch (Exception e) {
			LOGGER.error("An error has occured while getting all entities [ " + typeClass + " ].", e);
			throw new DataBaseException("An error has occured while getting all entities [ " + typeClass + " ].", e);
		}

	}

	/**
	 * Execute an HQL select request with an unique result of type <code>Type</code>.
	 * 
	 * @param request
	 *            the HQL request to execute.
	 * @param params
	 *            the parameters of the request. Parameters are included in order of position.
	 * 
	 * @return the entity found, <code>null</code> if no entity has been found.
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to retrieve entity in the database.
	 */
	@SuppressWarnings("unchecked")
	protected Type executeSingleResult(final String request, final Object... params) throws DataBaseException {

		try {

			Query query = createQuery(request, params);

			return (Type) query.uniqueResult();

		} catch (NonUniqueResultException e) {
			LOGGER.error("There is more than one matching result.", e);
			throw new DataBaseException("There is more than one matching result.", e);
		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to execute query [ " + request + " ].", e);
			throw new DataBaseException("An error has occurred when trying to execute query [ " + request + " ].", e);
		}
	}

	/**
	 * Execute a HQL request that the result is a {@link List} of entities.
	 * 
	 * @param request
	 *            the HQL request to execute.
	 * @param params
	 *            the parameters of the request. Parameters are included in order of position.
	 * 
	 * @return the {@link List} of entities found. An empty {@link List} is returned if no result has been found.
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to execute query in the database.
	 */
	@SuppressWarnings("unchecked")
	protected List<Type> executeResultList(final String request, final Object... params) throws DataBaseException {

		try {
			Query query = createQuery(request, params);

			List<Type> results = query.list();

			return (results != null ? results : new ArrayList<Type>());

		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to execute query [ " + request + " ].", e);

			throw new DataBaseException("An error has occurred when trying to execute query [ " + request + " ].", e);
		}
	}

	/**
	 * Create a new instance of Query for the given HQL query request with all parameters and return a {@link Query}
	 * object.
	 * 
	 * @param request
	 *            the HQL request.
	 * @param params
	 *            the parameters of the request. Parameters are included in order of position.
	 * 
	 * @return {@link Query}
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to create the query.
	 */
	protected Query createQuery(String request, Object... params) throws DataBaseException {

		try {
			Query query = getSession().createQuery(request);

			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}

			return query;
		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to create query [ " + request + " ].", e);

			throw new DataBaseException("An error has occurred when trying to execute query [ " + request + " ].", e);
		}
	}

	/**
	 * The method {@link #initDao()} initializes the HibernateTemplate in a <code>FLUSH_EAGER</code> mode.
	 * 
	 * @throws Exception
	 *             Thrown if an error occurs during the initialization.
	 */
	protected void initDao() throws Exception {
		getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_EAGER);
		super.initDao();
	}

}
