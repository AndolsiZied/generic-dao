package com.ando.architecture.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ando.architecture.dao.DAO;
import com.ando.architecture.dao.HibernateAction;
import com.ando.architecture.exception.DataBaseException;
import com.ando.architecture.util.HibernateUtil;

/**
 * This class provides the basic implementations needed to make Object-Relational operations on databases, using the
 * Hibernate framework.
 * 
 * @author Zied ANDOLSI
 * 
 * @param <Type>
 *            Entity's type this instance has to manage.
 * @param <IdType>
 *            Entity identifier's type this instance has to manage.
 */
public abstract class HibernateDAO<Type, IdType extends Serializable> implements DAO<Type, IdType> {

	/**
	 * {@link HibernateDAO}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateDAO.class);

	/**
	 * SessionFactory attribute.
	 */
	private SessionFactory sessionFactory;

	/**
	 * Entity's type this instance has to manage.
	 */
	protected Class<Type> typeClass;

	/**
	 * The constructor {@link #SimpleHibernateDAO(Class)} instantiates a new {@link HibernateDAO} object keeping in mind
	 * the type of the class to manage.
	 * <p>
	 * This constructor must be called by all the inheriting classes.
	 * 
	 * @param typeClass
	 *            Entity's type this DAO instance has to manage.
	 */
	protected HibernateDAO(Class<Type> typeClass) {
		this.typeClass = typeClass;
		this.sessionFactory = HibernateUtil.getSessionFactory();
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
			return executeAction(new HibernateAction<Type>() {

				@Override
				public Type apply(Session session) throws DataBaseException {
					return (Type) session.get(typeClass, id);
				}
			});

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

			return executeAction(new HibernateAction<Type>() {

				@Override
				public Type apply(Session session) throws DataBaseException {
					Serializable identifier = session.save(entity);
					LOGGER.debug("The entity [ " + typeClass.getSimpleName() + " ] with identifier [ " + identifier
							+ " ] has been successfully saved.");
					LOGGER.debug("Getting and returning the entity [ " + typeClass.getSimpleName()
							+ " ] with identifier [ " + identifier + " ]...");
					return (Type) session.get(typeClass, identifier);
				}
			});

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

		try {
			LOGGER.debug("Updating the entity [ " + typeClass.getSimpleName() + " ] with identifier [ "
					+ getIdentifier(entity) + " ]...");

			executeAction(new HibernateAction<Type>() {

				@Override
				public Type apply(Session session) throws DataBaseException {
					session.update(entity);
					return null;
				}
			});

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

			executeAction(new HibernateAction<Type>() {

				@Override
				public Type apply(Session session) throws DataBaseException {
					session.delete(entity);
					return null;
				}
			});

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
			List<Type> results = executeAction(new HibernateAction<List<Type>>() {

				@Override
				public List<Type> apply(Session session) throws DataBaseException {
					Criteria criteria = session.createCriteria(typeClass);
					return criteria.list();
				}
			});

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

			return executeAction(new HibernateAction<Type>() {

				@Override
				public Type apply(Session session) throws DataBaseException {
					Query query = createQuery(request, params);

					return (Type) query.uniqueResult();
				}
			});

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
			List<Type> results = executeAction(new HibernateAction<List<Type>>() {

				@Override
				public List<Type> apply(Session session) throws DataBaseException {
					Query query = createQuery(request, params);

					return query.list();
				}
			});

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
	 * Check if the session is related to a context (JTASessionContext, ThreadLocalSessionContext,
	 * ManagedSessionContext).
	 * 
	 * @return true if current session is related to a context.
	 */
	private boolean isSessionInContext() {
		try {
			sessionFactory.getCurrentSession();
			return true;
		} catch (HibernateException e) {
			return false;
		}
	}

	/**
	 * Execute the action received as parameter.
	 * 
	 * @param action
	 *            object that specifies the Hibernate action
	 * @param <T>
	 *            object's returned type.
	 * @return a result object returned by the action.
	 * @throws DataBaseException
	 *             thrown if error occurred when trying to execute the action.
	 */
	public <T> T executeAction(HibernateAction<T> action) throws DataBaseException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			T results = action.apply(session);
			session.getTransaction().commit();
			return results;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new DataBaseException(e);
		} finally {
			if (!isSessionInContext()) {
				session.close();
			}
		}

	}

	/**
	 * Return an hibernate Session.
	 * 
	 * <p>
	 * Returns session controlled by the context or otherwise new session.
	 * 
	 * @return the session to use.
	 */
	private Session getSession() {
		if (isSessionInContext()) {
			return sessionFactory.getCurrentSession();
		}
		return sessionFactory.openSession();
	}
}
