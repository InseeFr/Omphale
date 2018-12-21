/**
 * 
 */
package fr.insee.omphale.maintenance.test;

import java.util.ArrayList;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;


public class NeuroneParamMethEvolMaintenance implements INeuroneNonDAO {

	private String METHODE_EVOL;
	private String TYPE_PARAM;
	private String RANG;
	private String VAL_DEF;
	private String LIBELLE;

	
	public ArrayList<Object> getDeepMemory() throws Exception {
		  return getSqlMemory();
		}


		public ArrayList<Object> getSqlMemory() throws Exception {
			NeuroneParamMethEvolMaintenance neur;
		   ArrayList<Object> lines=new ArrayList<Object>();
		   ISimpleSelectDao dao = null;
		   dao = (ISimpleSelectDao) ContexteApplication.getDao(
		   "simpleSelectDao", OmphaleBatchConfig.nomConnexion);
		   dao.execute(getSql());
		   while (dao.nextRow()) { 
		     neur=new NeuroneParamMethEvolMaintenance();
		     neur.METHODE_EVOL=dao.getString("METHODE_EVOL");
		     neur.TYPE_PARAM=dao.getString("TYPE_PARAM");
		     neur.RANG=dao.getString("RANG");
		     neur.VAL_DEF=dao.getString("VAL_DEF");
		     neur.LIBELLE=dao.getString("LIBELLE");
		     lines.add(neur);
		   }
		    dao.close();
		    return lines;
		   }

	public void collabo(ArrayList<INeurone> neurones) throws Exception {

	}

	public String getMapKey() {
		return METHODE_EVOL;
	}

	public boolean parseNeurone(INeurone modele) throws Exception {
		return true;
	}

	public void setCreateSynapseParameters(Object fait) {
		setFait(fait);
	}


	public void setDeepFait(Object fait) {
		setFait(fait);
	}
	
	public void setFait(Object fait){
		this.METHODE_EVOL= ((NeuroneParamMethEvolMaintenance)fait).METHODE_EVOL;
		this.TYPE_PARAM= ((NeuroneParamMethEvolMaintenance)fait).TYPE_PARAM;
		this.RANG= ((NeuroneParamMethEvolMaintenance)fait).RANG;
		this.VAL_DEF= ((NeuroneParamMethEvolMaintenance)fait).VAL_DEF;
		this.LIBELLE= ((NeuroneParamMethEvolMaintenance)fait).LIBELLE;
	}

	public void setRechercheParameters(Object fait) {
		setFait(fait);
	}
	
	public String getSql(){
		return "select * from PARAM_METH_EVOL order by RANG";
	}

	public String getMETHODE_EVOL() {
		return METHODE_EVOL;
	}

	public void setMETHODE_EVOL(String mETHODEEVOL) {
		METHODE_EVOL = mETHODEEVOL;
	}

	public String getTYPE_PARAM() {
		return TYPE_PARAM;
	}

	public void setTYPE_PARAM(String tYPEPARAM) {
		TYPE_PARAM = tYPEPARAM;
	}

	public String getRANG() {
		return RANG;
	}

	public void setRANG(String rANG) {
		RANG = rANG;
	}

	public String getVAL_DEF() {
		return VAL_DEF;
	}

	public void setVAL_DEF(String vALDEF) {
		VAL_DEF = vALDEF;
	}

	public String getLIBELLE() {
		return LIBELLE;
	}

	public void setLIBELLE(String lIBELLE) {
		LIBELLE = lIBELLE;
	}
	



	public String toString(){
		return "Methode : " + METHODE_EVOL + ", libelle : " + LIBELLE  + ", TYPE_PARAM : " + TYPE_PARAM  + ", RANG : " + RANG +", Val_def : " + VAL_DEF;
	}
	

}
