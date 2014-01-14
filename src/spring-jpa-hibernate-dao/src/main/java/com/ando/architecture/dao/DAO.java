package com.ando.architecture.dao;

import java.io.Serializable;
import java.util.List;

import com.ando.architecture.exception.DataBaseException;

/**
 * The interface {@link DAO} provides all the generic methods required to link an entity to a database. The provided
 * methods allow performing the CRUD operations on the regarded entity.
 * 
 * @param <Type>
 *            Class of the entity to managed by this DAO.
 * @param <IdType>
 *            Class of the entity identifier.
 * 
 * @author Zied ANDOLSI
 */
public interface DAO<Type, IdType extends Serializable> {

	/**
	 * The method {@link #findOne(Serializable)} loads and returns the entity whose identifier is received as parameter.
	 * <p>
	 * This method returns <code>null</code> if no entity is found in the database.
	 * 
	 * @param id
	 *            Identifier of the entity to load.
	 * @return The entity corresponding to the identifier received as parameter. <code>null</code> if no entity is found
	 *         corresponding to the identifier.
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to load the entity from the database.
	 */
	Type findOne(IdType id) throws DataBaseException;

	/**
	 * The method {@link #save(Object)} saves an entity to the database and returns it after the operation has been
	 * successfully performed.
	 * <p>
	 * This method is useful when some modifications are done on the entity during its insertion (auto-incremented
	 * primary key for example) and these modifications have to be loaded in the returned object.
	 * 
	 * @param entity
	 *            Entity to save in the database.
	 * @return Entity loaded from the database once the operation has been successfully performed.
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to save the entity in the database.
	 */
	Type save(Type entity) throws DataBaseException;

	/**
	 * The method {@link #update(Object)} updates the entity received as parameter in the database. The entity has to
	 * exist in the data source if you want the update operation to success.
	 * 
	 * @param entity
	 *            Entity to update in the database.
	 * @return Entity loaded from the database once the operation has been successfully performed.
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to update the entity in the database.
	 */
	Type update(Type entity) throws DataBaseException;

	/**
	 * The method {@link #delete(Object)} deletes the entity received as parameter from the data source. The entity has
	 * to exist in the data source if you want the operation to success.
	 * 
	 * @param entity
	 *            Entity to delete from the database.
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to delete the entity from the database.
	 */
	void delete(Type entity) throws DataBaseException;

	/**
	 * The method {@link #getAll()} loads and returns all entities.
	 * <p>
	 * This method returns empty list if no entity is found in the database.
	 * 
	 * @return returns list of all entities found in the database. Empty list if no entity found in the database..
	 * @throws DataBaseException
	 *             Thrown if an exception occurs when trying to load entities from the database.
	 */
	List<Type> getAll() throws DataBaseException;

	/**
	 * The method {@link #getIdentifier()} returns the identifier of the entity received as parameter. It is only
	 * supposed to call the getter returning the entity identifier.
	 * <p>
	 * This method should not be implemented in a generic way, but by the final implementation.
	 * 
	 * @param entity
	 *            Entity for which getting the identifier.
	 * @return The identifier of the entity.
	 */
	IdType getIdentifier(Type entity);

}