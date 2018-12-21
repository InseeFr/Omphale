package fr.insee.omphale.maintenance.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import fr.insee.omphale.batch.cps.Cerveau;
import fr.insee.omphale.batch.cps.INeurone;


public class MainTestNeuroneParamMethEvol extends TestCase{
	
	private Cerveau cerveau;
	public static List<NeuroneMethodeMaintenance> neurMethodeEvols;

	public void setUp() throws Exception{
		cerveau = new Cerveau();
		cerveau.createSynapse(new NeuroneParamMethEvolMaintenance());
		cerveau.createSynapse(new NeuroneMethodeMaintenance());
		for (NeuroneMethodeMaintenance nmm :neurMethodeEvols){
			NeuroneParamMethEvolMaintenance modeleDeNeuronePourRecherche = new NeuroneParamMethEvolMaintenance();
			modeleDeNeuronePourRecherche.setMETHODE_EVOL(nmm.getMETHODE_EVOL());
			List<INeurone> list = cerveau.getAllRegles(modeleDeNeuronePourRecherche);
			if (list != null && !list.isEmpty()){
				nmm.setNeurParamMethEvols(new ArrayList<NeuroneParamMethEvolMaintenance>());
				for (INeurone neur : list){
					nmm.getNeurParamMethEvols().add((NeuroneParamMethEvolMaintenance)neur);
				}
			}
		
		}
	}

	public void testVoirGetMapKey() throws Exception{
		NeuroneMethodeMaintenance neur = new NeuroneMethodeMaintenance();
		neur.setMETHODE_EVOL("FECON_ICF_GAIN");
		neur = (NeuroneMethodeMaintenance)cerveau.getRegle(neur);
		assertNotNull("L'objet neur ne doit pas être nul",neur);
		assertNotNull("Le tableau n'est pas vide", neur.getNeurParamMethEvols());
		
		
	}
	

	
}
