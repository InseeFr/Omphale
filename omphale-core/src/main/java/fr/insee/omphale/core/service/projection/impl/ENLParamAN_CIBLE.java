package fr.insee.omphale.core.service.projection.impl;

import java.io.Serializable;

/**
 * Classe pour ENLParam pour l'année cible
 */
public class ENLParamAN_CIBLE extends ENLParam implements Serializable {

	private static final long serialVersionUID = 744248179190659208L;

	/**
	 * Méthode pour contrôler le bon format du paramètre Année cible
	 */
	@Override
	public ENLFieldErrorParam controle() {
		String valParam = this.getValAffichage();
		return  valoriseMessageChampFlag(new ENLFieldErrorParam(), valParam);
	}
	
	public ENLFieldErrorParam valoriseMessageChampFlag(ENLFieldErrorParam beanField, String valParam){
		beanField.setFlag(true);
		if (valParam == null || ("").equals(valParam)) {
			beanField.setMessage("error.evolution.format.valParam.vide");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
		}
		return beanField;
		
		
	}

}
