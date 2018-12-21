package fr.insee.omphale.batch.cps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe abstraite permettant d'utiliser simplement les classes du CPS pour
 * rechercher des valeurs dans une table ORACLE.
 *
 *
 */
public abstract class AbstractNeuroneDAO implements INeuroneDAO {

	public abstract String getSql();


	public abstract String getMapKey();


	public abstract void setDeepFait(Object fait);


	public ArrayList<String> getCreateSynapseParameters() {
		return null;
	}


	public void setCreateSynapseParameters(Object fait) {
	}


	public Object getSqlColumns(ResultSet r) throws SQLException {
		return null;
	}


	public boolean isReuseCursor() {
		return false;
	}


	public boolean isStringColumns() {
		return true;
	}


	public void setSqlParameters(PreparedStatement ps) throws SQLException {
	}


	public void collabo(ArrayList<INeurone> neurones) throws Exception {
	}


	public boolean parseNeurone(INeurone modele) throws Exception {
		return true;
	}


	public void setRechercheParameters(Object fait) {
	}

}
