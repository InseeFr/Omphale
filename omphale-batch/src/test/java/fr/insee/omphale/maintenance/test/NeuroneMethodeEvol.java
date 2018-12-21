package fr.insee.omphale.maintenance.test;

import java.util.ArrayList;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;


public class NeuroneMethodeEvol implements INeuroneNonDAO {

	private String METHODE_EVOL;
	private String COMPOSANTE;
	
	public NeuroneMethodeEvol(String METHODE_EVOL, String COMPOSANTE){
		this.METHODE_EVOL = METHODE_EVOL;
		this.COMPOSANTE = COMPOSANTE;
	}

	public String getMapKey() {
		return COMPOSANTE;
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
		this.METHODE_EVOL= ((NeuroneMethodeEvol)fait).METHODE_EVOL;
		this.COMPOSANTE= ((NeuroneMethodeEvol)fait).COMPOSANTE;
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



	public String toString(){
		return "Methode : " + METHODE_EVOL + ", composante : " + COMPOSANTE;
	}



	public void collabo(ArrayList<INeurone> neurones) throws Exception {
		
	}

	@Override
	public ArrayList<Object> getDeepMemory() throws Exception {
		return null;
	}

}
