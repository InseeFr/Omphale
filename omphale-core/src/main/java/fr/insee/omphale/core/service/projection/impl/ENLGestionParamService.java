package fr.insee.omphale.core.service.projection.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.insee.omphale.core.service.projection.IHypotheseService;
import fr.insee.omphale.core.service.projection.IParamMethodeEvolutionService;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.domaine.projection.ParamMethodeEvolution;
import fr.insee.omphale.domaine.projection.TypeEntite;
import fr.insee.omphale.ihm.util.WebOmphaleConfig;

/**
 * cette classe permet de gérer les paramètres liés  à une évolution non localisée
 * <br>
 * <BR>
 * METHODE fonction 
 * <BR>
 *   - composante
 * <BR>
 * 	 - type_entite
 * <BR>
 *   - hypothese_requise
 * <BR>
 * <BR>
 *  TYPE_PARAM fonction
 * <BR>
 * 	 - type_param
 * <BR>
 *   - val_def
 * <br>
 * 	 - isEntier
 * <BR>
 * <BR>
 *  PARAM_METHOD_EVOL fonction
 * <BR>
 *   - methode
 * <BR>
 *   - type_param
 * <BR>
 *   - rang
 * <BR>
 * 	 - val_def
 *
 */
public class ENLGestionParamService implements Serializable {

	private static final long serialVersionUID = -2940754257459818427L;
	private TypeEntite entite;
	private Hypothese hypothese;
	private String idHypothese;
	private List<Hypothese> hypotheses;
	private MethodeEvolution methode;
	private List<ParamMethodeEvolution> paramMethodeEvols;
	private List<ENLParam> listeParam;
	private int anneeDebut;
	private int anneeFin;
	private int ageDebut;
	private int ageFin;
	private String anneeDebutSelectionnee;
	private String anneeFinSelectionnee;
	private String ageDebutSelectionnee;
	private String ageFinSelectionnee;
	private ENLParam BeanParam1;
	private ENLParam BeanParam2;
	private ENLParam BeanParam3;
	private ENLParam BeanParam4;
	private Boolean age;
	private Boolean sexe;
	private String sexeSelectionne;
	private int sexeDebut;
	private int sexeFin;
	private Boolean hypotheseRequise;
	private int nbParam;
	private List<ENLFieldErrorParam> listeControlParam;

	
	
	
	/**
	 * Méthode pour initialiser l'interface pour gèrer 
	 * <BR>
	 * les paramètres d'une méthode choisie par un utilisateur
	 * @param methode
	 * @param idep void
	 */
	public void initialise(MethodeEvolution methode, String idep, IHypotheseService hypotheseService, IParamMethodeEvolutionService paramMethodeEvolutionService) {
		setMethode(methode);
		setEntite(methode.getTypeEntite());
		setHypotheseRequise(methode.isHypotheseRequise());
		setAge(entite.getAge());
		setSexe(entite.getSexe());
		if (hypotheses == null && hypotheseRequise) {
			setHypotheses(hypotheseService.findByTypeEntitePourENLGestionParams(entite, idep, hypotheseService));
		}
		setParamMethodeEvols(paramMethodeEvolutionService.findByMethodeEvolution(methode));
		initialiseListeDesParams();
	}


	/**
	 * Initialise liste d'objet paramètres
	 * 
	 * @return List(ENLParam)
	 */
	public List<ENLParam> initialiseListeDesParams() {
		// remplissage du tableau BeanParam

		
		setSizeParamMethodeEvols(paramMethodeEvols.size());
		if (paramMethodeEvols != null && !paramMethodeEvols.isEmpty()) {
			listeParam = new ArrayList<ENLParam>();
			// nbParam le get récupére la size de paramMethodeEvols
			for (int i = 0; i < nbParam; i++) {
				// instanciation BeanParam selon son code
				ENLParam param = null;
				// récupération du code du paramétre sélectionné
				String code = paramMethodeEvols.get(i).getTypeParam().getCode();
				if (code.equals("AN_CIBLE")) {
					param = new ENLParamAN_CIBLE();
				}
				if (code.equals("GAIN_ICF")) {
					param = new ENLParamGAIN_ICF();
				}
				if (code.equals("GAIN_EV")) {
					param = new ENLParamGAIN_EV();
				}
				if (code.equals("COEF_HOM")) {
					param = new ENLParamCOEF_HOM();
				}
				if (code.equals("COEF_TRAN")) {
					param = new ENLParamCOEF_TRAN();
				}
				// valorisation des attributs du BeanParam
				param
						.setCode(paramMethodeEvols.get(i).getTypeParam()
								.getCode());
				param.setRang(paramMethodeEvols.get(i).getRang());
				param.setLib(paramMethodeEvols.get(i).getLibelle());
				param.setVal(paramMethodeEvols.get(i).getTypeParam()
						.getValDef());
				param.setEntier(paramMethodeEvols.get(i).getTypeParam()
						.isEntier());
				param.setTypeClass(code);
				param.setNomFieldError("valParam" + (i + 1));
				param.setHypotheseRequise(methode.isHypotheseRequise());
				param.setTypeParam(paramMethodeEvols.get(i).getTypeParam());
				listeParam.add(param);
			}
		}
		initialiseLesBeanParam();
		return listeParam;
	}
	
	/**
	 * initialise les objets BeanParam
	 * 
	 * 
	 */
	public void initialiseLesBeanParam(){
		if (listeParam != null && !listeParam.isEmpty()){
			if (listeParam.size()> 0){
				setBeanParam1(listeParam.get(0));
			}
			if (listeParam.size()> 1){
				setBeanParam2(listeParam.get(1));
			}
			if (listeParam.size()> 2){
				setBeanParam3(listeParam.get(2));
			}
			if (listeParam.size()> 4){
				setBeanParam4(listeParam.get(3));
			}
		}
	}
	
	/**
	 * Initialise l'année de début
	 * 
	 * @param anneeDebut
	 */
	public void initialiseAnneeDebut(String anneeDebut){
		if (anneeDebut == null) {
			if (isHypotheseRequise()){
				if (hypothese != null) {
					setAnneeDebut(hypothese.getAnneeDebut());
				}
				else {
					setAnneeDebut(WebOmphaleConfig.getConfig().getAnneeMin());
				}
			} else {
				setAnneeDebut(WebOmphaleConfig.getConfig().getAnneeMin());
			}
		} else {
			setAnneeDebut(Integer.valueOf(anneeDebut));
		}
	}
	
	/**
	 * Initialise l'année de fin
	 * 
	 * @param anneeFin
	 */
	public void initialiseAnneeFin(String anneeFin){
		if (anneeFin == null) {
			if (isHypotheseRequise()){
				if(hypothese != null){
					setAnneeFin(hypothese.getAnneeFin());
				} else {
					setAnneeFin(WebOmphaleConfig.getConfig().getAnneeMax());
				}
			} else {
				setAnneeFin(WebOmphaleConfig.getConfig().getAnneeMax());
			}
		} else {
			setAnneeFin(Integer.valueOf(anneeFin));
		}
	}
	
	/**
	 * Initialise l'age de début
	 * 
	 * @param ageDebut
	 */
	public  void intialiseAgeDebut(String ageDebut){
		if (ageDebut == null) {
			if (isHypotheseRequise()){
				if(hypothese != null) {
					setAgeDebut(hypothese.getAgeDebut());
				} 
			} else if (isRestrictionAge()) {
				setAgeDebut(WebOmphaleConfig.getConfig().getAgeMinMere());

			} else {
				setAgeDebut(WebOmphaleConfig.getConfig().getAgeMin());
			}
		} else {
			setAgeDebut(Integer.valueOf(ageDebut));
		}
	}
	
	/**
	 * Initialise l'age de fin
	 * 
	 * @param ageFin
	 */
	public void initialiseAgeFin(String ageFin){
		if (ageFin == null) {
			if (isHypotheseRequise()){
				if(hypothese != null) {
				setAgeFin(hypothese.getAgeFin());
				}
			} else if (isRestrictionAge()) {
				setAgeFin(WebOmphaleConfig.getConfig().getAgeMaxMere());

			} else {
				setAgeFin(WebOmphaleConfig.getConfig().getAgeMax());
			}
		} else {
			setAgeFin(Integer.valueOf(ageFin));
		}
	}
	

	/**
	 * initialise l'hypothèse
	 * 
	 * @param idHypothese
	 */
	public void initialiseHypothese(String idHypothese) {
		if (idHypothese != null || !"-1".equals(idHypothese)) {
			setIdHypothese(idHypothese);
			setHypothese(new HypotheseService().findById(Integer
					.valueOf(idHypothese)));
		}
	}

	/**
	 * Contrôle la liste des params
	 * 
	 * @return {@link List}<{@link ENLFieldErrorParam}>
	 */
	public List<ENLFieldErrorParam> controlListeParams() {
		listeControlParam = new ArrayList<ENLFieldErrorParam>();
		for (ENLParam p : listeParam) {
			ENLFieldErrorParam err = p.controle();
			if (!err.getFlag()) {
				listeControlParam.add(err);
			}
		}
		return listeControlParam;
	}
	

	
	/**
	 * Retourne le libelleParam1
	 * 
	 * @return {@link String}
	 */
	public String retourneLibelleParam1() {
		return listeParam.get(0).getLib();
	}

	/**
	 * Retourne le libelleParam2
	 * 
	 * @return {@link String}
	 */
	public String retourneLibelleParam2() {
		return listeParam.get(1).getLib();
	}

	/**
	 * Retourne le libelleParam3
	 * 
	 * @return {@link String}
	 */
	public String retourneLibelleParam3() {
		return listeParam.get(2).getLib();
	}

	/**
	 * Retourne le libelleParam4
	 * 
	 * @return {@link String}
	 */
	public String retourneLibelleParam4() {
		return listeParam.get(3).getLib();
	}

	/**
	 * Retourne la Valeur modifiée pour le programme (et non pour l'affichage) de valParam1
	 * 
	 * @param valParam1
	 * @return {@link String}
	 */
	public String retourneValParam1(String valParam1) {
		return getBeanParam1().modifVal(valParam1);
	}

	/**
	 * Retourne la Valeur modifiée pour le programme (et non pour l'affichage) de valParam2
	 * 
	 * @param valParam2
	 * @return {@link String}
	 */
	public String retourneValParam2(String valParam2) {
		getBeanParam2().modifVal(valParam2);
		return getBeanParam2().getValAffichage();
	}

	/**
	 * Retourne la Valeur modifiée pour le programme (et non pour l'affichage) de valParam3
	 * 
	 * @param valParam3
	 * @return {@link String}
	 */
	public String retourneValParam3(String valParam3) {
		getBeanParam3().modifVal(valParam3);
		return getBeanParam3().getValAffichage();
	}

	/**
	 * Retourne la Valeur modifiée pour le programme (et non pour l'affichage) de valParam4
	 * 
	 * @param valParam4
	 * @return {@link String}
	 */
	public String retourneValParam4(String valParam4) {
		getBeanParam4().modifVal(valParam4);
		return getBeanParam4().getValAffichage();
	}

	/**
	 * Récupère le nombre de paramMethodeEvols
	 * 
	 * @return {@link Integer}
	 */
	public int getNbParam() {
		return paramMethodeEvols.size();
	}


	/**
	 * Initialise la liste des années de début
	 * 
	 * @param anneeFin
	 * @return {@link List}<{@link String}>
	 */
	public List<String> initialiseListeDesAnneesDeDebut(String anneeFin) {
		List<String> listeAnneeDebut = new ArrayList<String>();
		if (anneeFin != null) {
			this.setAnneeFinSelectionnee(anneeFin);
		} else {
			this.setAnneeFinSelectionnee(String.valueOf(this.getAnneeFin()));
		}
		int debut = getAnneeDebut();
		int fin = Integer.valueOf(getAnneeFinSelectionnee());
		for (int i = debut; i < fin + 1; i++) {
			listeAnneeDebut.add(String.valueOf(i));
		}
		return listeAnneeDebut;
	}

	/**
	 * Initialise la liste des années de fin
	 * 
	 * @param anneeDebut
	 * @return {@link List}<{@link String}>
	 */
	public List<String> initialiseListeDesAnneesDeFin(String anneeDebut) {
		List<String> listeAnneeFin = new ArrayList<String>();
		if (anneeDebut != null) {
			this.setAnneeDebutSelectionnee(anneeDebut);
		} else {
			this
					.setAnneeDebutSelectionnee(String.valueOf(this
							.getAnneeDebut()));
		}
		for (int i = Integer.valueOf(anneeDebutSelectionnee); i < this
				.getAnneeFin() + 1; i++) {
			listeAnneeFin.add(String.valueOf(i));
		}
		return listeAnneeFin;
	}



	/**
	 * initialise la liste des ages de début
	 * 
	 * @param ageFin
	 * @return {@link List}<{@link String}>
	 */
	public List<String> initialiseListeDesAgesDeDebut(String ageFin) {
		List<String> listeAgeDebut = new ArrayList<String>();
		if (getAge()) {
			if (ageFin != null) {
				this.setAgeFinSelectionnee(ageFin);
			} else {
				this.setAgeFinSelectionnee(String.valueOf(this.getAgeFin()));
			}
			int debut = getAgeDebut();
			int fin = Integer.valueOf(getAgeFinSelectionnee());
			for (int i = debut; i < fin + 1; i++) {
				listeAgeDebut.add(String.valueOf(i));
			}
		}
		return listeAgeDebut;
	}

	/**
	 * initialise la liste des ages de fin
	 * 
	 * @param ageDebut
	 * @return {@link List}<{@link String}>
	 */
	public List<String> initialiseListeDesAgesDeFin(String ageDebut) {
		List<String> listeAgeFin = new ArrayList<String>();
		if (getAge()) {
			if (ageDebut != null) {
				this.setAgeDebutSelectionnee(ageDebut);
			} else {
				this
						.setAgeDebutSelectionnee(String.valueOf(this
								.getAgeDebut()));
			}
			for (int i = Integer.valueOf(ageDebutSelectionnee); i < this
					.getAgeFin() + 1; i++) {
				listeAgeFin.add(String.valueOf(i));
			}
		}
		return listeAgeFin;
	}

	/**
	 * initialise la liste des sexes
	 * 
	 * @return {@link List}<{@link String}>
	 */
	public List<String> initialiseListeDesSexes() {
		List<String> listeSexes = new ArrayList<String>();
		if (getSexe()) {
			listeSexes.add("Homme & Femme");
			listeSexes.add("Homme");
			listeSexes.add("Femme");

		}
		return listeSexes;
	}

	/**
	 * Converti les libellés du sexe en 2 numéro
	 * 	Ex: "Homme" --> sexeDebut = 1 et sexeFin = 1
	 * 
	 * @param sexe
	 */
	public void conversionHFEnSexeDebEtFin(String sexe) {
		this.setSexeSelectionne(sexe);
		if (getSexe()) {
			if (sexeSelectionne.equals("Homme")) {
				setSexeDebut(1);
				setSexeFin(1);
			}
			if (sexeSelectionne.equals("Femme")) {
				setSexeDebut(2);
				setSexeFin(2);
			}
			if (sexeSelectionne.equals("Homme & Femme")) {
				setSexeDebut(1);
				setSexeFin(2);
			}
		}
	}
	
	
	public ENLParam getBeanParam1() {
		return BeanParam1;
	}

	public ENLParam getBeanParam2() {
		return BeanParam2;
	}

	public ENLParam getBeanParam3() {
		return BeanParam3;
	}

	public ENLParam getBeanParam4() {
		return BeanParam4;
	}
	
	public void setAgeDebut(int ageDebut) {
		this.ageDebut = ageDebut;
	}

	public void setAgeFin(int ageFin) {
		this.ageFin = ageFin;
	}

	public int getAgeDebut() {
		return this.ageDebut;
	}

	public int getAgeFin() {
		return this.ageFin;
	}

	public void setAgeDebutSelectionnee(String ageDebutSelectionnee) {
		this.ageDebutSelectionnee = ageDebutSelectionnee;
	}

	public void setIdHypothese(String idHypothese) {
		this.idHypothese = idHypothese;
	}

	public Boolean getHypotheseRequise() {
		return hypotheseRequise;
	}

	public void setHypotheseRequise(Boolean hypotheseRequise) {
		this.hypotheseRequise = hypotheseRequise;
	}

	public int getSizeParamMethodeEvols() {
		return nbParam;
	}

	private void setSizeParamMethodeEvols(int sizeParamMethodeEvols) {
		this.nbParam = sizeParamMethodeEvols;
	}

	public String getAnneeFinSelectionnee() {
		return anneeFinSelectionnee;
	}

	public void setAnneeFinSelectionnee(String anneeFinSelectionnee) {
		this.anneeFinSelectionnee = anneeFinSelectionnee;
	}

	public String getAgeFinSelectionnee() {
		return ageFinSelectionnee;
	}

	public void setAgeFinSelectionnee(String ageFinSelectionnee) {
		this.ageFinSelectionnee = ageFinSelectionnee;
	}

	public String getAnneeDebutSelectionnee() {
		return anneeDebutSelectionnee;
	}

	public String getAgeDebutSelectionnee() {
		return ageDebutSelectionnee;
	}

	public int getSexeDebut() {
		return sexeDebut;
	}

	public void setSexeDebut(int sexeDebut) {
		this.sexeDebut = sexeDebut;
	}

	public int getSexeFin() {
		return sexeFin;
	}

	public void setSexeFin(int sexeFin) {
		this.sexeFin = sexeFin;
	}

	public List<ENLFieldErrorParam> getListeControlParam() {
		return listeControlParam;
	}

	public List<ParamMethodeEvolution> getParamMethodeEvols() {
		return paramMethodeEvols;
	}

	public List<ENLParam> getListeBeanParam() {
		return listeParam;
	}

	public String getIdHypothese() {
		return idHypothese;
	}

	public String getSexeSelectionne() {
		return sexeSelectionne;
	}

	public void setSexeSelectionne(String sexeSelectionne) {
		this.sexeSelectionne = sexeSelectionne;
	}

	private void setHypotheses(List<Hypothese> hypotheses) {
		this.hypotheses = hypotheses;
	}

	private void setEntite(TypeEntite entite) {
		this.entite = entite;
	}

	public void setParamMethodeEvols(List<ParamMethodeEvolution> paramMethodeEvols) {
		this.paramMethodeEvols = paramMethodeEvols;
	}

	public void setMethode(MethodeEvolution methode) {
		this.methode = methode;
	}
	

	public Boolean getAge() {
		return age;
	}

	public void setAge(Boolean age) {
		this.age = age;
	}

	public Boolean getSexe() {
		return sexe;
	}

	public void setSexe(Boolean sexe) {
		this.sexe = sexe;
	}

	private boolean isRestrictionAge() {
		return entite.isRestrictionAge();
	}

	public boolean isHypotheseRequise() {
		return hypotheseRequise;
	}

	public List<Hypothese> getHypotheses() {
		return hypotheses;
	}

	public Hypothese getHypothese() {
		return hypothese;
	}

	public void setHypothese(Hypothese hypothese) {
		this.hypothese = hypothese;
	}

	public void setAnneeDebut(int anneeDebut) {
		this.anneeDebut = anneeDebut;
	}

	public void setAnneeFin(int anneeFin) {
		this.anneeFin = anneeFin;
	}

	public int getAnneeDebut() {
		return this.anneeDebut;
	}

	public int getAnneeFin() {
		return this.anneeFin;
	}

	public void setAnneeDebutSelectionnee(String anneeDebutSelectionnee) {
		this.anneeDebutSelectionnee = anneeDebutSelectionnee;
	}

	public TypeEntite getEntite() {
		return entite;
	}


	public MethodeEvolution getMethode() {
		return methode;
	}


	public void setBeanParam1(ENLParam beanParam1) {
		BeanParam1 = beanParam1;
	}


	public void setBeanParam2(ENLParam beanParam2) {
		BeanParam2 = beanParam2;
	}


	public void setBeanParam3(ENLParam beanParam3) {
		BeanParam3 = beanParam3;
	}


	public void setBeanParam4(ENLParam beanParam4) {
		BeanParam4 = beanParam4;
	}
	

}
