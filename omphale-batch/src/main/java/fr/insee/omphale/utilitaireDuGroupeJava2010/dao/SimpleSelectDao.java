package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaSQLException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;

public class SimpleSelectDao extends AbstractDao implements ISimpleSelectDao {

	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private int nbcol = 0;

	public SimpleSelectDao(String nomPool, IConnectionManager connectionManager) {
		super(nomPool, connectionManager);
	}


	public Double getDouble(String col) {
		Double val = null;
		try {
			val = rs.getDouble(col);
		} catch (Exception e) {
			val = null;
		}

		return val;
	}


	public Integer getInteger(String col) {
		Integer val = null;
		try {
			val = rs.getInt(col);
		} catch (Exception e) {
			val = null;
		}

		return val;
	}


	public String getString(String col) {
		String val = null;
		try {
			val = rs.getString(col);
		} catch (Exception e) {
			val = null;
		}

		return val;
	}


	public Long getLong(String col) {
		Long val = null;
		try {
			val = rs.getLong(col);
		} catch (Exception e) {
			val = null;
		}

		return val;
	}


	public Date getDate(String col) {
		Date val = null;
		try {
			val = rs.getDate(col);
		} catch (Exception e) {
			val = null;
		}

		return val;
	}


	public void execute(String sql) throws GroupeJavaDaoException {
		try {
			close();
			connection();
            nbcol=0;
			ps = c.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			final String message = "Erreur lors du select";
			throw new GroupeJavaDaoException(message, e);
		} 
	}

	/* (non-Javadoc)
	 * @see fr.insee.groupejava.dao.ISimpleSelectDao#nextRow()
	 */
	public boolean nextRow() throws GroupeJavaDaoException {
		try {
			return rs.next();
		} catch (SQLException e) {
			final String message = "impossible d'accéder à la ligne suivante dans le resultSet Jdbc";
			throw new GroupeJavaSQLException(message, e);
		}
	}
	
	private int getNbColumns() throws GroupeJavaDaoException {
	    try {
            return rs.getMetaData().getColumnCount();
        } catch (SQLException e) {
            final String message = "impossible d'obtenir le nombre de colonnes du resultSet Jdbc";
            throw new GroupeJavaSQLException(message, e);
        }
	}
	
	public ArrayList<String> getColumnNames() throws GroupeJavaDaoException {
	       
	       ArrayList<String> noms=new ArrayList<String>();
	        try {
	            if (nbcol==0) nbcol= getNbColumns();
	            for (int i=1;i<=nbcol;i++) {
	                noms.add(rs.getMetaData().getColumnName(i));
	                }
	            return noms;
	        } catch (SQLException e) {
	            final String message = "impossible d'obtenir les noms de colonnes du resultSet Jdbc";
	            throw new GroupeJavaSQLException(message, e);
	        }
	}
	public ArrayList<String> getColumnValues() throws GroupeJavaDaoException {
           
           ArrayList<String> values=new ArrayList<String>();
            try {
                if (nbcol==0) nbcol= getNbColumns();
                
                for (int i=1;i<=nbcol;i++) {
                      values.add(rs.getString(i));
                    }
                return values;
            } catch (SQLException e) {
                final String message = "impossible d'obtenir les valeurs de colonnes du resultSet Jdbc";
                throw new GroupeJavaSQLException(message, e);
            }
    }

   public ArrayList<String> getColumnTypes() throws GroupeJavaDaoException {
           
           ArrayList<String> types=new ArrayList<String>();
            try {
                if (nbcol==0) nbcol= getNbColumns();
                for (int i=1;i<=nbcol;i++) {
                    types.add(rs.getMetaData().getColumnTypeName(i)+rs.getMetaData().getPrecision(i));
                   
                    }
                return types;
            } catch (SQLException e) {
                final String message = "impossible d'obtenir les types de colonnes du resultSet Jdbc";
                throw new GroupeJavaSQLException(message, e);
            }
    }

	public final void close() throws GroupeJavaDaoException {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			super.close();
		} catch (SQLException e) {
			final String message = "impossible de fermer un objet vers la base de données";
			throw new GroupeJavaSQLException(message, e);
		}

	}

}
