package fr.insee.omphale.dao.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import fr.insee.omphale.dao.factory.HibernateDAOFactory;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Implements the generic CRUD data access operations using Hibernate APIs.
 * <p>
 * To write a DAO, subclass and parameterize this class with your persistent
 * class. Of course, assuming that you have a traditional 1:1 appraoch for
 * Entity:DAO design.
 * <p>
 * You have to inject a current Hibernate <tt>Session</tt> to use a DAO.
 * Otherwise, this generic implementation will use
 * <tt>HibernateUtil.getSessionFactory()</tt> to obtain the curren
 * <tt>Session</tt>.
 * 
 * @see HibernateDAOFactory
 */
public abstract class GenericDAO<T, ID extends Serializable>
		implements IGenericDAO<T, ID> {

	private Class<T> persistentClass;
	private Session session;

	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void setSession(Session s) {
		this.session = s;
	}

	public Session getSession() {
		if (session == null) {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}

		return session;
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T getById(ID id) {
		T entity;
		try {
			entity = (T) getSession().load(getPersistentClass(), id);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T getById(String entityName, ID id) {
		T entity;
		try {
			entity = (T) getSession().load(entityName, id);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T getAndLockById(ID id) {
		T entity;
		try {
			entity = (T) getSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T getAndLockById(String entityName, ID id) {
		T entity;
		try {
			entity = (T) getSession().load(entityName, id, LockOptions.UPGRADE);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		T entity;
		try {
			entity = (T) getSession().get(getPersistentClass(), id);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T findAndLockById(ID id) {
		T entity;
		try {
			entity = (T) getSession().get(getPersistentClass(), id,
					LockOptions.UPGRADE);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T findById(String entityName, ID id) {
		T entity;
		try {
			entity = (T) getSession().get(entityName, id);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T findAndLockById(String entityName, ID id) {
		T entity;
		try {
			entity = (T) getSession().get(entityName, id, LockOptions.UPGRADE);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		return entity;
	}

	public List<T> findAll() {
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityName) {
		Criteria crit = getSession().createCriteria(entityName);
		try {
			return crit.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		try {
			return crit.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(String entityName, T exampleInstance,
			String... excludeProperty) {
		Criteria crit = getSession().createCriteria(entityName);
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		try {
			return crit.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	public T insertOrUpdate(T entity) {
		try {
			getSession().saveOrUpdate(entity);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.002"), he);
		}
		return entity;
	}

	public T insertOrUpdate(String entityName, T entity) {
		try {
			getSession().saveOrUpdate(entityName, entity);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.002"), he);
		}
		return entity;
	}

	public T insert(T entity) {
		try {
			getSession().save(entity);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.002"), he);
		}
		return entity;
	}

	public T insert(String entityName, T entity) {
		try {
			getSession().save(entityName, entity);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.002"), he);
		}
		return entity;
	}

	public void delete(T entity) {
		try {
			getSession().delete(entity);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.003"), he);
		}
	}

	public void delete(String entityName, T entity) {
		try {
			getSession().delete(entityName, entity);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.003"), he);
		}
	}

	public void flush() {
		try {
			getSession().flush();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.004"), he);
		}
	}

	public void clear() {
		getSession().clear();
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		try {
			return crit.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
