package fr.insee.omphale.dao.util;

import java.io.Serializable;
import java.util.List;

/**
 * An interface shared by all business data access objects.
 * <p>
 * All CRUD (create, read, update, delete) basic data access operations are
 * isolated in this interface and shared accross all DAO implementations. The
 * current design is for a state-management oriented persistence layer (for
 * example, there is no UDPATE statement function) that provides automatic
 * transactional dirty checking of business objects in persistent state.
 */
public interface IGenericDAO<T, ID extends Serializable> {
	T findById(ID id);

	T findById(String entityName, ID id);

	T findAndLockById(ID id);

	T findAndLockById(String entityName, ID id);

	T getById(ID id);

	T getById(String entityName, ID id);

	T getAndLockById(ID id);

	T getAndLockById(String entityName, ID id);

	List<T> findAll();

	List<T> findAll(String entityName);

	List<T> findByExample(T exampleInstance, String... excludeProperty);

	List<T> findByExample(String entityName, T exampleInstance,
			String... excludeProperty);

	T insertOrUpdate(T entity);

	T insertOrUpdate(String entityName, T entity);

	T insert(T entity);

	T insert(String entityName, T entity);

	void delete(T entity);

	void delete(String entityName, T entity);

	/**
	 * Affects every managed instance in the current persistence context!
	 */
	void flush();

	/**
	 * Affects every managed instance in the current persistence context!
	 */
	void clear();
}
