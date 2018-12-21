package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IHypotheseService;
import fr.insee.omphale.core.service.projection.IParamMethodeEvolutionService;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.domaine.projection.ParamMethodeEvolution;
import fr.insee.omphale.domaine.projection.TypeEntite;
import fr.insee.omphale.domaine.projection.TypeParam;

public class ENLGestionParamServiceTest {
	
	// initialisation des services
	private ENLGestionParamService enlGestionParam = new ENLGestionParamService();
	
	// objets utiles aux tests
	private List<ENLParam> listeParam;

	// objets de méthode
	private MethodeEvolution methodeFeconIcfGain;
	private MethodeEvolution methodeFeconQfConf;
	private MethodeEvolution methodeEmigrQeAff;
	
	private Set<TypeParam> paramsMethodeFeconIcfGain;
	private Set<TypeParam> paramsMethodeFeconQfConf;
	private Set<TypeParam> paramsMethodeEmigrQeAff;
	private List<ParamMethodeEvolution> paramMethodEvols;
	
	private Composante composanteEmigr;
	private Composante composanteFecon;
	
	private TypeEntite typeEntiteICF;
	private TypeEntite typeEntiteQF;
	private TypeEntite typeEntiteQE;
	
	private TypeParam typeParamANCIBLE;
	private TypeParam typeParamGAINICF;
	private TypeParam typeParamGAINEV;
	private TypeParam typeParamCOEFHOM;
	private TypeParam typeParamCOEFTRAN;
	

	private ParamMethodeEvolution paramMethodeEvolFECONQFCONV ;
	private ParamMethodeEvolution paramMethodeEvolFECONICFGAIN1 ;
	private ParamMethodeEvolution paramMethodeEvolFECONICFGAIN2 ;
	private ParamMethodeEvolution paramMethodeEvolEMIGRQEAFF1 ;
	private ParamMethodeEvolution paramMethodeEvolEMIGRQEAFF2 ;
	
	private List<ParamMethodeEvolution> paramsMethodeEvolFECONQFCONV;
	private List<ParamMethodeEvolution> paramsMethodeEvolFECONICFGAIN;
	private List<ParamMethodeEvolution> paramsMethodeEvolEMIGRQEAFF;
	
	private Hypothese hypoQF;
	private Utilisateur test;
	
	private List<Hypothese> hypothesesQF;
	
	@Test
	public void initialise(){
		creationObjetsEtinitialise();
		IHypotheseService hypotheseServiceMock = EasyMock.createMock(IHypotheseService.class);
		EasyMock.expect(hypotheseServiceMock.findByTypeEntitePourENLGestionParams(typeEntiteQF, test.getIdep(), hypotheseServiceMock)).andReturn(hypothesesQF).anyTimes();
		IParamMethodeEvolutionService paramMethodeEvolutionServiceMock = EasyMock.createMock(IParamMethodeEvolutionService.class);
		EasyMock.expect(paramMethodeEvolutionServiceMock.findByMethodeEvolution(methodeFeconQfConf)).andReturn(paramsMethodeEvolFECONQFCONV).anyTimes();
		
		EasyMock.replay(hypotheseServiceMock, paramMethodeEvolutionServiceMock);
		
		ENLGestionParamService enlGestionParam = new ENLGestionParamService();

		enlGestionParam.initialise(methodeFeconQfConf, test.getIdep(), hypotheseServiceMock, paramMethodeEvolutionServiceMock);

		// test methode
		Assert.assertEquals("methode différente : ",enlGestionParam.getMethode().getCode(), methodeFeconQfConf.getCode());
		// test entite
		Assert.assertEquals("entité différente : ",enlGestionParam.getEntite().getCode(), typeEntiteQF.getCode());
		// Hypothese requise
		Assert.assertEquals("isHypotheseRequise différente : ", enlGestionParam.getHypotheseRequise(), methodeFeconQfConf.isHypotheseRequise());
		// age
		Assert.assertEquals("getAge différent : ", enlGestionParam.getAge(), typeEntiteQF.getAge());
		// Sexe
		Assert.assertEquals("getSexe diiférent : ", enlGestionParam.getSexe(), typeEntiteQF.getSexe());
	}
	
	@Test
	public void initialiseListeDesParams() {
			// création des objets
			creationObjetsEtinitialise();
			initialiseListeDesParamsGeneric(methodeFeconQfConf, paramMethodeEvolFECONQFCONV,null);
			initialiseListeDesParamsGeneric(methodeFeconIcfGain,paramMethodeEvolFECONICFGAIN1,paramMethodeEvolFECONICFGAIN2);
			initialiseListeDesParamsGeneric(methodeEmigrQeAff,paramMethodeEvolEMIGRQEAFF1,paramMethodeEvolEMIGRQEAFF2);
	}
	

	public void initialiseListeDesParamsGeneric(MethodeEvolution methode, ParamMethodeEvolution paramMethode1, ParamMethodeEvolution paramMethode2) {

		paramMethodEvols = new ArrayList<ParamMethodeEvolution>();
		paramMethodEvols.add(paramMethode1);
		if (paramMethode2 != null){
			paramMethodEvols.add(paramMethode2);
		}
		enlGestionParam.setMethode(methode);
		enlGestionParam.setParamMethodeEvols(paramMethodEvols);
		listeParam = enlGestionParam.initialiseListeDesParams();

		// test de l'objet ENLPARAM 2 à partir comparaison des listes de param
		Assert.assertSame("taille paramMethodeEvol n'est pas égal à taille listeParam",	listeParam.size(), enlGestionParam.getParamMethodeEvols()
						.size());
		
		
		//Pour chaque Parametre
		int i = 0;
		for (ENLParam enlParam : listeParam) {
			List <ParamMethodeEvolution> paramsMethodeEvol = enlGestionParam.getParamMethodeEvols();
			
			// code du paramètre
			Assert.assertEquals("Code param " + enlParam.getRang() + " pas identique à ",enlParam.getCode(), paramsMethodeEvol.get(i).getTypeParam().getCode());
			// Rang du paramètre
			Assert.assertEquals("Rang param " + enlParam.getRang() + " pas identique à ",enlParam.getRang(), paramsMethodeEvol.get(i).getRang());
			// Libelle 
			Assert.assertEquals("Libelle param " + enlParam.getRang() + " pas identique à ",enlParam.getLib(), paramsMethodeEvol.get(i).getLibelle());
			//Valeur par défaut du paramètre
			Assert.assertEquals("Val par def du param " + enlParam.getRang() + " pas identique à ",enlParam.getVal(), paramsMethodeEvol.get(i).getTypeParam().getValDef(), 0);
			// Parametre est-il un entier
			Assert.assertEquals("IsEntier param " + enlParam.getRang() + " pas identique à ",enlParam.isEntier(), paramsMethodeEvol.get(i).getTypeParam().isEntier());
			// Type de la classe du paramètre
			Assert.assertEquals("Type classe param " + enlParam.getRang() + " pas identique à ",enlParam.getTypeClass(), paramsMethodeEvol.get(i).getTypeParam().getCode());
			// Nom du paramètre
			Assert.assertEquals("Nom param " + enlParam.getRang() + " pas égal à \"valparam " + i+1 + " \"",enlParam.getNomFieldError(), "valParam" + paramsMethodeEvol.get(i).getRang() );
			//IsHypotheseRequise du paramètre
			Assert.assertEquals("isHypotheseRequise du param " + enlParam.getRang() + " pas identique à ",enlParam.isHypotheseRequise(), paramsMethodeEvol.get(i).getMethode().isHypotheseRequise());
			// Objet typeParam du paramètre
			Assert.assertEquals("typeParam associé à paramètre " + enlParam.getRang() + " pas identique",enlParam.getTypeParam(), paramsMethodeEvol.get(i).getTypeParam());
			i++;
		}
		
	}
	public void creationObjetsEtinitialise(){
		
		// initialisation des objets typeEntite
		typeEntiteICF = new TypeEntite();
		typeEntiteICF.setCode("ICF");
		typeEntiteICF.setAge(false);
		typeEntiteICF.setSexe(false);
		typeEntiteICF.setRestrictionAge(false);
		typeEntiteICF.setMax(10.0);
		typeEntiteICF.setMin(0.0);
		typeEntiteICF.setLibelle("Indice Conjoncturelle fécondité (année)");
		typeEntiteQF = new TypeEntite();
		typeEntiteQF.setCode("QF");
		typeEntiteQF.setAge(true);
		typeEntiteQF.setSexe(false);
		typeEntiteQF.setRestrictionAge(true);
		typeEntiteQF.setMax(1.0);
		typeEntiteQF.setMin(0.0);
		typeEntiteQF.setLibelle("Quotient de fécondité (année, âge)");
		typeEntiteQE = new TypeEntite();
		typeEntiteQE.setCode("QE");
		typeEntiteQE.setAge(true);
		typeEntiteQE.setSexe(true);
		typeEntiteQE.setRestrictionAge(false);
		typeEntiteQE.setMax(1.0);
		typeEntiteQE.setMin(0.0);
		typeEntiteQE.setLibelle("Quotient émigration (année,sexe,âge)");
		
		// initialisation des objets composante
		composanteEmigr = new Composante();
		composanteEmigr.setCode("EMIGR");
		composanteFecon = new Composante();
		composanteFecon.setCode("FECON");
		
		// initialisation des typeparam
		typeParamANCIBLE = new TypeParam();
		typeParamANCIBLE.setCode("AN_CIBLE");
		typeParamANCIBLE.setValDef(2040.0);
		typeParamANCIBLE.setEntier(true);
		typeParamANCIBLE.setLibelle("annee de convergence");
		typeParamGAINICF = new TypeParam();
		typeParamGAINICF.setCode("GAIN_ICF");
		typeParamGAINICF.setValDef(0.0);
		typeParamGAINICF.setEntier(false);
		typeParamGAINICF.setLibelle("gain icf entre -1 et +1");
		typeParamGAINEV = new TypeParam();
		typeParamGAINEV.setCode("GAIN_EV");
		typeParamGAINEV.setValDef(0.0);
		typeParamGAINEV.setEntier(false);
		typeParamGAINEV.setLibelle("esp vie entre -20 et +20");
		typeParamCOEFHOM = new TypeParam();
		typeParamCOEFHOM.setCode("COEF_HOM");
		typeParamCOEFHOM.setValDef(1.0);
		typeParamCOEFHOM.setEntier(false);
		typeParamCOEFHOM.setLibelle("coefficient homotetie entre 0 et 5");
		typeParamCOEFTRAN = new TypeParam();
		typeParamCOEFTRAN.setCode("COEF_TRAN");
		typeParamCOEFTRAN.setValDef(0.0);
		typeParamCOEFTRAN.setEntier(false);
		typeParamCOEFTRAN.setLibelle("coefficient translation entre -1 et +1");
		
		
		paramsMethodeFeconIcfGain = new HashSet<TypeParam>();
		paramsMethodeFeconIcfGain.add(typeParamANCIBLE);
		paramsMethodeFeconIcfGain.add(typeParamGAINICF);
		paramsMethodeFeconQfConf = new HashSet<TypeParam>();
		paramsMethodeFeconQfConf.add(typeParamANCIBLE);
		paramsMethodeEmigrQeAff  = new HashSet<TypeParam>();
		paramsMethodeEmigrQeAff.add(typeParamCOEFHOM);
		paramsMethodeEmigrQeAff.add(typeParamCOEFTRAN);
		
		
		// intialisation des méthodes 
		methodeFeconIcfGain = new MethodeEvolution();
		methodeFeconIcfGain.setCode("FECON_ICF_GAIN");
		methodeFeconIcfGain.setComposante(composanteFecon);
		methodeFeconIcfGain.setTypeEntite(typeEntiteICF);
		methodeFeconIcfGain.setHypotheseRequise(false);
		methodeFeconIcfGain.setLibelle("Gain ICF pour des qf");
		methodeFeconIcfGain.setParametres(paramsMethodeFeconIcfGain);
		methodeFeconQfConf = new MethodeEvolution();
		methodeFeconQfConf.setCode("FECON_QF_CONV");
		methodeFeconQfConf.setComposante(composanteFecon);
		methodeFeconQfConf.setTypeEntite(typeEntiteQF);
		methodeFeconQfConf.setHypotheseRequise(true);
		methodeFeconQfConf.setLibelle("Convergence vers une tendance de qf");
		methodeFeconQfConf.setParametres(paramsMethodeFeconQfConf);
		methodeEmigrQeAff = new MethodeEvolution();
		methodeEmigrQeAff.setCode("EMIGR_QE_AFF");
		methodeEmigrQeAff.setComposante(composanteEmigr);
		methodeEmigrQeAff.setTypeEntite(typeEntiteQE);
		methodeEmigrQeAff.setHypotheseRequise(false);
		methodeEmigrQeAff.setLibelle("Transformation affine");
		methodeEmigrQeAff.setParametres(paramsMethodeEmigrQeAff);
		
		// initialisation des objets paramMethodeEvol
		
		paramMethodeEvolFECONQFCONV = new ParamMethodeEvolution();
		paramMethodeEvolFECONQFCONV.setMethode(methodeFeconQfConf) ;
		paramMethodeEvolFECONQFCONV.setTypeParam(typeParamANCIBLE) ;
		paramMethodeEvolFECONQFCONV.setRang(1);
		paramMethodeEvolFECONQFCONV.setLibelle("annee de convergence") ;
		
		paramMethodeEvolFECONICFGAIN1 = new ParamMethodeEvolution() ;
		paramMethodeEvolFECONICFGAIN1.setMethode(methodeFeconIcfGain) ;
		paramMethodeEvolFECONICFGAIN1.setTypeParam(typeParamANCIBLE) ;
		paramMethodeEvolFECONICFGAIN1.setRang(1) ;
		paramMethodeEvolFECONICFGAIN1.setLibelle("annee de convergence") ;

		paramMethodeEvolFECONICFGAIN2 = new ParamMethodeEvolution() ;
		paramMethodeEvolFECONICFGAIN2.setMethode(methodeFeconIcfGain) ;
		paramMethodeEvolFECONICFGAIN2.setTypeParam(typeParamGAINICF) ;
		paramMethodeEvolFECONICFGAIN2.setRang(2) ;
		paramMethodeEvolFECONICFGAIN2.setLibelle("gain icf entre -1 et +1e") ;
		
		paramMethodeEvolEMIGRQEAFF1 = new ParamMethodeEvolution() ;
		paramMethodeEvolEMIGRQEAFF1.setMethode(methodeEmigrQeAff) ;
		paramMethodeEvolEMIGRQEAFF1.setTypeParam(typeParamCOEFHOM) ;
		paramMethodeEvolEMIGRQEAFF1.setRang(1) ;
		paramMethodeEvolEMIGRQEAFF1.setLibelle("homotetie affine 0-5") ;
		
		paramMethodeEvolEMIGRQEAFF2 = new ParamMethodeEvolution() ;
		paramMethodeEvolEMIGRQEAFF2.setMethode(methodeEmigrQeAff) ;
		paramMethodeEvolEMIGRQEAFF2.setTypeParam(typeParamCOEFTRAN) ;
		paramMethodeEvolEMIGRQEAFF2.setRang(2) ;
		paramMethodeEvolEMIGRQEAFF2.setLibelle("translation affine -1-+") ;
		
		paramsMethodeEvolFECONQFCONV = new ArrayList<ParamMethodeEvolution>();
		paramsMethodeEvolFECONQFCONV.add(paramMethodeEvolFECONQFCONV);
		paramsMethodeEvolFECONICFGAIN = new ArrayList<ParamMethodeEvolution>();
		paramsMethodeEvolFECONICFGAIN.add(paramMethodeEvolFECONICFGAIN1);
		paramsMethodeEvolFECONICFGAIN.add(paramMethodeEvolFECONICFGAIN2);
		paramsMethodeEvolEMIGRQEAFF = new ArrayList<ParamMethodeEvolution>();
		paramsMethodeEvolEMIGRQEAFF.add(paramMethodeEvolEMIGRQEAFF1);
		paramsMethodeEvolEMIGRQEAFF.add(paramMethodeEvolEMIGRQEAFF1);
		
		hypoQF = new Hypothese();
		hypoQF.setAgeDebut(10);
		hypoQF.setAgeFin(20);
		hypoQF.setAnneeDebut(2000);
		hypoQF.setAnneeFin(2100);
		hypoQF.setLibelle("hypoQF");
		hypoQF.setNom("hypoQF");
		hypoQF.setSexeDebut(1);
		hypoQF.setSexeFin(1);
		hypoQF.setStandard(false);
		hypoQF.setTypeEntite(typeEntiteQF);
		hypoQF.setUtilisateur(test);
		
		test = new Utilisateur();
		test.setIdep("test");
		
		hypothesesQF = new ArrayList<Hypothese>();
		hypothesesQF.add(hypoQF);
		}

}
