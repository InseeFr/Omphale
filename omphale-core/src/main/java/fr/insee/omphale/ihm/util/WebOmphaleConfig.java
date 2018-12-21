package fr.insee.omphale.ihm.util;

import fr.insee.config.InseeConfig;

/**
 * Cette classe n'est chargée qu'une fois
 * 
 * Elle contient les liens vers l'ensemble des paramètres de limitation d'Omphale
 *
 */
public class WebOmphaleConfig {

	private static WebOmphaleConfig instance;

	private int quotaZone;
	private int quotaZonage;
	private int quotaHypothese;
	private int quotaEvolution;
	private int quotaScenario;
	private int quotaProjection;

	private int ageMin;
	private int ageMax;
	private int ageMinMere;
	private int ageMaxMere;

	private int anneeMin;
	private int anneeMax;

	private int anneeCibleMin;
	private int anneeCibleMax;
	private double gainEvMin;
	private double gainEvMax;
	private double gainICFMin;
	private double gainICFMax;
	private double coefHomMin;
	private double coefHomMax;
	private double coefTranMin;
	private double coefTranMax;
	private String standardisationZonage;
	private String standardisationHypothese;
	private String standardisationENL;
	private String standardisationScenario;
	private String standardisationProjection;
	private String standardise;
	private String destandardise;
	private String personnel;
	private String zonesEmploi;
	private String aireUrbaine;
	private String autresZonesStandards;


	private String administrateurs;
	private String utilisateurs;
	private String desactives;
	
	private int pasDeProjection;
	
	private String urlIgesa;
	private String nomApplicationIgesa;
	private String nomGroupeLDAPIgesa;
	private String compteApplicatifIgesa;
	private String motDePasseApplicatifIgesa;
	
	private String version;

	private WebOmphaleConfig() {
		quotaZone = InseeConfig.getConfig().getInt("fr.insee.omphale.quotaZone");
		quotaZonage = InseeConfig.getConfig().getInt("fr.insee.omphale.quotaZonage");
		quotaHypothese = InseeConfig.getConfig().getInt(
				"fr.insee.omphale.quotaHypothese");
		quotaEvolution = InseeConfig.getConfig().getInt(
				"fr.insee.omphale.quotaEvolution");
		quotaScenario = InseeConfig.getConfig().getInt(
				"fr.insee.omphale.quotaScenario");
		quotaProjection = InseeConfig.getConfig().getInt(
				"fr.insee.omphale.quotaProjection");

		ageMin = InseeConfig.getConfig().getInt("fr.insee.omphale.ageMin");
		ageMax = InseeConfig.getConfig().getInt("fr.insee.omphale.ageMax");
		ageMinMere = InseeConfig.getConfig().getInt("fr.insee.omphale.ageMinMere");
		ageMaxMere = InseeConfig.getConfig().getInt("fr.insee.omphale.ageMaxMere");

		anneeMin = InseeConfig.getConfig().getInt("fr.insee.omphale.anneeMin");
		anneeMax = InseeConfig.getConfig().getInt("fr.insee.omphale.anneeMax");

		anneeCibleMin = InseeConfig.getConfig().getInt(
				"fr.insee.omphale.anneeCibleMin");
		anneeCibleMax = InseeConfig.getConfig().getInt(
				"fr.insee.omphale.anneeCibleMax");
		
		gainEvMin = InseeConfig.getConfig().getDouble("fr.insee.omphale.gainEvMin");
		gainEvMax = InseeConfig.getConfig().getDouble("fr.insee.omphale.gainEvMax");
		gainICFMin = InseeConfig.getConfig().getDouble("fr.insee.omphale.gainICFMin");
		gainICFMax = InseeConfig.getConfig().getDouble("fr.insee.omphale.gainICFMax");
		coefHomMin = InseeConfig.getConfig().getDouble("fr.insee.omphale.coefHomMin");
		coefHomMax = InseeConfig.getConfig().getDouble("fr.insee.omphale.coefHomMax");
		coefTranMin = InseeConfig.getConfig().getDouble("fr.insee.omphale.coefTranMin");
		coefTranMax = InseeConfig.getConfig().getDouble("fr.insee.omphale.coefTranMax");
		standardisationZonage = InseeConfig.getConfig().getString("fr.insee.omphale.standardisation.zonage");
		standardisationHypothese = InseeConfig.getConfig().getString("fr.insee.omphale.standardisation.hypothese");
		standardisationENL = InseeConfig.getConfig().getString("fr.insee.omphale.standardisation.enl");
		standardisationScenario = InseeConfig.getConfig().getString("fr.insee.omphale.standardisation.scenario");
		standardisationProjection = InseeConfig.getConfig().getString("fr.insee.omphale.standardisation.projection");
		standardise = InseeConfig.getConfig().getString("fr.insee.omphale.standardisation.standardisé");
		destandardise = InseeConfig.getConfig().getString("fr.insee.omphale.standardisation.destandardisé");
		personnel = InseeConfig.getConfig().getString("fr.insee.omphale.typeZone.personnel");
		zonesEmploi  = InseeConfig.getConfig().getString("fr.insee.omphale.typeZone.zonesEmploi");
		aireUrbaine  = InseeConfig.getConfig().getString("fr.insee.omphale.typeZone.aireUrbaine");
		autresZonesStandards  = InseeConfig.getConfig().getString("fr.insee.omphale.typeZone.autresZonesStandards");

		
    
	    administrateurs = InseeConfig.getConfig().getString("fr.insee.omphale.administrateurs");
	    utilisateurs = InseeConfig.getConfig().getString("fr.insee.omphale.utilisateurs");
	    desactives = InseeConfig.getConfig().getString("fr.insee.omphale.desactives");
	    
	    
	    // Différence entre omphale 2010 (pas quinquennal) et omphale 2017 (pas annuel)
	    pasDeProjection = (InseeConfig.getConfig().getString("fr.insee.omphale.pasDeProjection")==null) ? 5 : InseeConfig.getConfig().getInt("fr.insee.omphale.pasDeProjection");
	    
	    urlIgesa = InseeConfig.getConfig().getString("fr.insee.igesa.url");
	    version = InseeConfig.getConfig().getString("fr.insee.omphale.numero.version");
	    
	    nomApplicationIgesa = InseeConfig.getConfig().getString("fr.insee.igesa.nomApplication");
	    nomGroupeLDAPIgesa = InseeConfig.getConfig().getString("fr.insee.igesa.nomGroupeLDAP");
	    compteApplicatifIgesa= InseeConfig.getConfig().getString("fr.insee.igesa.compteApplicatif");
	    motDePasseApplicatifIgesa = InseeConfig.getConfig().getString("fr.insee.igesa.motDePasseApplicatif");
	    
	    
	    
	}

	public static WebOmphaleConfig getConfig() {
		if (instance == null) {
			instance = new WebOmphaleConfig();
		}
		return instance;
	}

	public int getQuotaZone() {
		return quotaZone;
	}

	public int getQuotaZonage() {
		return quotaZonage;
	}

	public int getQuotaHypothese() {
		return quotaHypothese;
	}

	public int getQuotaEvolution() {
		return quotaEvolution;
	}

	public int getQuotaScenario() {
		return quotaScenario;
	}

	public int getQuotaProjection() {
		return quotaProjection;
	}

	public int getAgeMin() {
		return ageMin;
	}

	public int getAgeMax() {
		return ageMax;
	}

	public int getAgeMinMere() {
		return ageMinMere;
	}

	public int getAgeMaxMere() {
		return ageMaxMere;
	}

	public int getAnneeMin() {
		return anneeMin;
	}

	public int getAnneeMax() {
		return anneeMax;
	}

	public int getAnneeCibleMin() {
		return anneeCibleMin;
	}

	public int getAnneeCibleMax() {
		return anneeCibleMax;
	}

	public double getGainEvMin() {
		return gainEvMin;
	}

	public double getGainEvMax() {
		return gainEvMax;
	}

	public double getGainICFMin() {
		return gainICFMin;
	}

	public double getGainICFMax() {
		return gainICFMax;
	}

	public double getCoefHomMin() {
		return coefHomMin;
	}

	public double getCoefHomMax() {
		return coefHomMax;
	}

	public double getCoefTranMin() {
		return coefTranMin;
	}

	public double getCoefTranMax() {
		return coefTranMax;
	}

	public String getStandardisationZonage() {
		return standardisationZonage;
	}

	public String getStandardisationHypothese() {
		return standardisationHypothese;
	}

	public String getStandardisationENL() {
		return standardisationENL;
	}

	public String getStandardisationScenario() {
		return standardisationScenario;
	}

	public String getStandardise() {
		return standardise;
	}

	public String getDestandardise() {
		return destandardise;
	}

	public String getZonesEmploi() {
		return zonesEmploi;
	}

	public String getAireUrbaine() {
		return aireUrbaine;
	}

	public String getAutresZonesStandards() {
		return autresZonesStandards;
	}

	public String getPersonnel() {
		return personnel;
	}
	
	public String getStandardisationProjection(){
		return standardisationProjection;
	}

	public String getAdministrateurs() {
		return administrateurs;
	}

	public String getUtilisateurs() {
		return utilisateurs;
	}

	public String getDesactives() {
		return desactives;
	}

	public int getPasDeProjection() {
		return pasDeProjection;
	}

	public String getUrlIgesa() {
		return urlIgesa;
	}

	public String getNomApplicationIgesa() {
		return nomApplicationIgesa;
	}

	public String getNomGroupeLDAPIgesa() {
		return nomGroupeLDAPIgesa;
	}

	public String getCompteApplicatifIgesa() {
		return compteApplicatifIgesa;
	}

	public String getMotDePasseApplicatifIgesa() {
		return motDePasseApplicatifIgesa;
	}

	public String getVersion() {
		return version;
	}
	
	
}
