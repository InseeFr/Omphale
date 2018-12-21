package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.util.ArrayList;
import java.util.Date;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;

public interface ISimpleSelectDao {

	public abstract Double getDouble(String col);

	public abstract Integer getInteger(String col);

	public abstract String getString(String col);

	public abstract Long getLong(String col);

	public abstract Date getDate(String col);

	public abstract void execute(String sql) throws GroupeJavaDaoException;

	/***
	 * Verifie s'il existe une ligne dans le resultSet jdbc.
	 * 
	 * @return true si une ligne existe dans le resultSet, false sinon.
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'accès à la base de données.
	 */
	public abstract boolean nextRow() throws GroupeJavaDaoException;

	/**
	 * Ferme un objet Dao. Les differents objets liés à jdbc (resultSet,
	 * Statement, etc...) sont fermés.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas de pb de libération.
	 */
	public abstract void close() throws GroupeJavaDaoException;
	
	public abstract ArrayList<String> getColumnNames() throws GroupeJavaDaoException;
	
	public abstract ArrayList<String> getColumnValues() throws GroupeJavaDaoException;
	
	public abstract ArrayList<String> getColumnTypes() throws GroupeJavaDaoException;

}