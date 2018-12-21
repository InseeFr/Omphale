package fr.insee.omphale.core.service.projection.impl;

import java.io.Serializable;
import java.util.regex.Pattern;

import fr.insee.omphale.ihm.util.WebOmphaleConfig;

/**
 * Classe pour ENLParam pour l'année COEF_HOM
 */
public class ENLParamCOEF_HOM extends ENLParam implements Serializable {

	private static final long serialVersionUID = 4430324274090996825L;

	/**
	 * Méthode pour contrôler le bon format du paramètre coefficient
	 * d'homothétie
	 */
	@Override
	public ENLFieldErrorParam controle() {
		String valParam = this.getValAffichage();
		return valoriseMessageChampFlag(new ENLFieldErrorParam(), valParam);
	}
	
	public ENLFieldErrorParam valoriseMessageChampFlag(ENLFieldErrorParam beanField, String valParam){
		beanField.setFlag(true);
		if (valParam == null || ("").equals(valParam)) {
			beanField.setMessage("error.evolution.format.valParam.vide");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		}
		if (!("").equals(valParam)
				&& !Pattern.matches("^\\-?[0-9]\\.?([0-9]+)?$", valParam)) {
			beanField
					.setMessage("error.evolution.format.valParam.caract.coef_hom");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		} else if (!("").equals(valParam)
				&& Double.valueOf(valParam) < WebOmphaleConfig.getConfig()
						.getCoefHomMin()
				|| Double.valueOf(valParam) > WebOmphaleConfig.getConfig()
						.getCoefHomMax()) {
			beanField
					.setMessage("error.evolution.format.valParam.caract.coef_hom");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		}
		return beanField;
	}

}
