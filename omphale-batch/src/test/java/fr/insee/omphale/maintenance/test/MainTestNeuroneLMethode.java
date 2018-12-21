package fr.insee.omphale.maintenance.test;

import java.util.List;

import junit.framework.TestCase;
import fr.insee.omphale.batch.cps.Cerveau;
import fr.insee.omphale.batch.cps.INeurone;


public class MainTestNeuroneLMethode extends TestCase{
	
	private Cerveau cerveau;

	public void setUp() throws Exception{
		cerveau = new Cerveau();
		cerveau.createSynapse(new NeuroneMethodeMaintenance());
	}

	public void testVoirGetMapKey() throws Exception{
		NeuroneMethodeMaintenance neur = new NeuroneMethodeMaintenance();
		neur.setMETHODE_EVOL("FECON_QF_SUR");
		neur = (NeuroneMethodeMaintenance)cerveau.getRegle(neur);
		assertNotNull("L'objet neur ne doit pas être nul",neur);
	}
	
	public void testVoirGetMapKey2() throws Exception{
		NeuroneMethodeMaintenance neur = new NeuroneMethodeMaintenance();
		neur.setTYPE_ENTITE("TAUX");
		neur = (NeuroneMethodeMaintenance)cerveau.getRegle(neur);
		assertNotNull("L'objet neur ne doit pas être nul",neur);
	}
	
	public void testVoirGetMapKey3() throws Exception{
		NeuroneMethodeMaintenance neur = new NeuroneMethodeMaintenance();
		neur.setTYPE_ENTITE("TAUX");
		@SuppressWarnings("unused")
		List<INeurone> neurs;
		neurs = cerveau.getAllRegles(neur);
		assertNotNull("L'objet neur ne doit pas être nul",neur);

	}
	
	public void testVoirGetMapKey4() throws Exception{
		NeuroneMethodeMaintenance neur = new NeuroneMethodeMaintenance();
		@SuppressWarnings("unused")
		List<INeurone> neurs;
		neurs = cerveau.getSequence(neur);
		assertNotNull("L'objet neur ne doit pas être nul",neur);

	}
	
}
