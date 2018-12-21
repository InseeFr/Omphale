package fr.insee.omphale.core.service.projection.impl;

import java.io.Serializable;
import java.util.regex.Pattern;

import fr.insee.omphale.ihm.util.WebOmphaleConfig;

/**
 * Classe pour ENLParam pour l'année GAIN_EV
 */
public class ENLParamGAIN_EV extends ENLParam implements Serializable {

	private static final long serialVersionUID = -1624522769990838637L;

	/**
	 * Méthode pour contrôler le bon format du paramètre Gain ev
	 */
	@Override
	public ENLFieldErrorParam controle() {
		String valParam = this.getValAffichage();
		return valoriseMessageChampFlag(new ENLFieldErrorParam(), valParam);
	}
	
	
	/**
	 * Ajouter pour pouvoir tester les différents cas d'utilisation
	 */
	public ENLFieldErrorParam valoriseMessageChampFlag(ENLFieldErrorParam beanField, String valParam){
		beanField.setFlag(true);
		if (valParam == null || ("").equals(valParam)) {
			beanField.setMessage("error.evolution.format.valParam.vide");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		}
		if (valParam != null && !("").equals(valParam)
				&& !Pattern.matches("^\\-?[0-9][0-9]$", valParam)) {
			beanField
					.setMessage("error.evolution.format.valParam.caract.gain_ev");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		} else if (!("").equals(valParam)
				&& Double.valueOf(valParam) < WebOmphaleConfig.getConfig()
						.getGainEvMin()
				|| Double.valueOf(valParam) > WebOmphaleConfig.getConfig()
						.getGainEvMax()) {
			beanField
					.setMessage("error.evolution.format.valParam.caract.gain_ev");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		}
		return beanField;
		
	}
	
	
	

}
