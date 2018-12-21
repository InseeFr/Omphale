package fr.insee.omphale.maintenance.test;

import java.util.ArrayList;
import java.util.List;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;


public class NeuroneMethodeMaintenance implements INeuroneNonDAO {

	private String METHODE_EVOL;
	private String COMPOSANTE;
	private String TYPE_ENTITE;
	private String HYPOTHESE_REQUISE;
	private String LIBELLE;
	private List<NeuroneParamMethEvolMaintenance> neurParamMethEvols;
	
	
	public ArrayList<Object> getDeepMemory() throws Exception {
	  return getSqlMemory();
	}


	public ArrayList<Object> getSqlMemory() throws Exception {
		NeuroneMethodeMaintenance neur;
	   ArrayList<Object> lines=new ArrayList<Object>();
	   ISimpleSelectDao dao = null;
	   dao = (ISimpleSelectDao) ContexteApplication.getDao(
	   "simpleSelectDao", OmphaleBatchConfig.nomConnexion);
	   dao.execute(getSql());
	   while (dao.nextRow()) { 
	     neur=new NeuroneMethodeMaintenance();
	     neur.METHODE_EVOL=dao.getString("METHODE_EVOL");
	     neur.COMPOSANTE=dao.getString("COMPOSANTE");
	     neur.TYPE_ENTITE=dao.getString("TYPE_ENTITE");
	     neur.HYPOTHESE_REQUISE=dao.getString("HYPOTHESE_REQUISE");
	     neur.LIBELLE=dao.getString("LIBELLE");
	     lines.add(neur);
	   }
	    dao.close();
	    return lines;
	   }

	public void collabo(ArrayList<INeurone> neurones) throws Exception {
		MainTestNeuroneParamMethEvol.neurMethodeEvols = new ArrayList<NeuroneMethodeMaintenance>();
		for (INeurone nmm : neurones){
			MainTestNeuroneParamMethEvol.neurMethodeEvols.add((NeuroneMethodeMaintenance)nmm);
		}
	}


	public String getMapKey() {
		return "toto";
	}

	public boolean parseNeurone(INeurone modele) throws Exception {
		
		NeuroneMethodeMaintenance neur = (NeuroneMethodeMaintenance)modele;
		if (neur.METHODE_EVOL!= null)return neur.METHODE_EVOL.equals(this.METHODE_EVOL);
		if (neur.COMPOSANTE!= null)return neur.COMPOSANTE.equals(this.COMPOSANTE);
		if (neur.TYPE_ENTITE!= null)return neur.TYPE_ENTITE.equals(this.TYPE_ENTITE);
		return false;
	}

	public void setCreateSynapseParameters(Object fait) {
		setFait(fait);
	}


	public void setDeepFait(Object fait) {
		setFait(fait);
	}
	
	public void setFait(Object fait){
		this.METHODE_EVOL= ((NeuroneMethodeMaintenance)fait).METHODE_EVOL;
		this.COMPOSANTE= ((NeuroneMethodeMaintenance)fait).COMPOSANTE;
		this.TYPE_ENTITE= ((NeuroneMethodeMaintenance)fait).TYPE_ENTITE;
		this.HYPOTHESE_REQUISE= ((NeuroneMethodeMaintenance)fait).HYPOTHESE_REQUISE;
		this.LIBELLE= ((NeuroneMethodeMaintenance)fait).LIBELLE;
	}

	public void setRechercheParameters(Object fait) {
		setFait(fait);
	}
	
	public String getSql(){
		return "select * from METHODE_EVOL";
	}


	public String getMETHODE_EVOL() {
		return METHODE_EVOL;
	}


	public void setMETHODE_EVOL(String mETHODEEVOL) {
		METHODE_EVOL = mETHODEEVOL;
	}


	public String getCOMPOSANTE() {
		return COMPOSANTE;
	}


	public void setCOMPOSANTE(String cOMPOSANTE) {
		COMPOSANTE = cOMPOSANTE;
	}


	public String getTYPE_ENTITE() {
		return TYPE_ENTITE;
	}


	public void setTYPE_ENTITE(String tYPEENTITE) {
		TYPE_ENTITE = tYPEENTITE;
	}


	public String getHYPOTHESE_REQUISE() {
		return HYPOTHESE_REQUISE;
	}


	public void setHYPOTHESE_REQUISE(String hYPOTHESEREQUISE) {
		HYPOTHESE_REQUISE = hYPOTHESEREQUISE;
	}


	public String getLIBELLE() {
		return LIBELLE;
	}


	public void setLIBELLE(String lIBELLE) {
		LIBELLE = lIBELLE;
	}
	


	public List<NeuroneParamMethEvolMaintenance> getNeurParamMethEvols() {
		return neurParamMethEvols;
	}


	public void setNeurParamMethEvols(
			List<NeuroneParamMethEvolMaintenance> neurParamMethEvols) {
		this.neurParamMethEvols = neurParamMethEvols;
	}


	public String toString(){
		return "Methode : " + METHODE_EVOL + ", libelle : " + LIBELLE  + ", type_entite : " + TYPE_ENTITE  + ", composante : " + COMPOSANTE;
	}

}
