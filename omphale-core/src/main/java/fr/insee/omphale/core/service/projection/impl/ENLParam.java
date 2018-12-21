package fr.insee.omphale.core.service.projection.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.insee.omphale.domaine.projection.TypeParam;
import fr.insee.omphale.ihm.util.WebOmphaleConfig;

/***
 * 
 * Classe pour stocker les caractéristiques d'une EvolutionNonLocalisee issue des valeurs choisies dans l'IHM
 * 
 */
public class ENLParam implements Serializable {

	private static final long serialVersionUID = -7256863193364923471L;
	private String code;
	private int rang;
	private String lib;
	private Double val;
	private String valAffichage;
	private String typeClass;
	private TypeParam typeParam;
	private String nomFieldError;
	private boolean hypotheseRequise;
	private boolean entier;
	private boolean tableau;
	private boolean valeur;

	public boolean isEntier() {
		return entier;
	}

	public void setEntier(boolean entier) {
		this.entier = entier;
	}

	public boolean isHypotheseRequise() {
		return hypotheseRequise;
	}

	public void setHypotheseRequise(boolean hypotheseRequise) {
		this.hypotheseRequise = hypotheseRequise;
	}

	public ENLFieldErrorParam controle() {
		return null;
	}

	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}

	public String getNomFieldError() {
		return nomFieldError;
	}

	public void setNomFieldError(String nomFieldError) {
		this.nomFieldError = nomFieldError;
	}

	public String getTypeClass() {
		return typeClass;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getRang() {
		return rang;
	}

	public void setRang(int rang) {
		this.rang = rang;
	}

	public String getLib() {
		return lib;
	}

	public void setLib(String lib) {
		this.lib = lib;
	}

	public Double getVal() {
		return val;
	}

	public void setVal(Double val) {
		this.val = val;
	}

	/**
	 * Méthode pour renvoyer dans un string un format entier ou réel
	 * 
	 * @return String
	 */
	public String getValAffichage() {
		if (valAffichage == null) {
			return "";
		} else {
			if (this.isEntier()) {
				try {
					return "" + Double.valueOf(valAffichage).intValue();
				} catch (NumberFormatException e) {
					return valAffichage;
				}

			} else {
				return valAffichage + "";
			}
		}
	}

	public void setValAffichage(String valAffichage) {
		this.valAffichage = valAffichage;
	}

	/**
	 * Méthode pour modifier la valeur de l'attribut val de ENLParam utiliser
	 * dans ENLGestionParamService
	 * 
	 * @param valParam
	 * @return String
	 */
	public String modifVal(String valParam) {
		if (this.getValAffichage() == null || ("").equals(this.getValAffichage())) {
			this.setValAffichage(val.toString());
		}
		if (valParam == null) {
			valParam = getValAffichage();
		} else if (("").equals(valParam)) {
			this.setValAffichage(null);
		} else if (!this.getValAffichage().equals(valParam)) {
			this.setValAffichage(valParam);
		}
		return valParam;
	}

	public TypeParam getTypeParam() {
		return typeParam;
	}

	public void setTypeParam(TypeParam typeParam) {
		this.typeParam = typeParam;
	}

	public boolean isTableau() {
		return tableau;
	}

	public void setTableau(boolean tableau) {
		this.tableau = tableau;
	}

	public boolean isValeur() {
		return valeur;
	}

	public void setValeur(boolean valeur) {
		this.valeur = valeur;
	}

	public List<String> getTableau(String valeurDebut) {
		int debut;
		int fin;
		List<String> resultat = new ArrayList<String>();
		if (valeurDebut == null) {
			debut = WebOmphaleConfig.getConfig().getAnneeCibleMin();
		} else {
			debut = Integer.valueOf(valeurDebut);
		}
		fin = WebOmphaleConfig.getConfig().getAnneeCibleMax();
		for (int i = debut; i < fin; i++) {
			resultat.add(String.valueOf(i));
		}
		return resultat;
	}

}
