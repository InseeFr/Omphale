package fr.insee.omphale.core.service.projection.impl;

import java.io.Serializable;
import java.util.regex.Pattern;

import fr.insee.omphale.ihm.util.WebOmphaleConfig;

/**
 * Classe pour ENLParam pour l'année COEF_TRAN
 */
public class ENLParamCOEF_TRAN extends ENLParam implements Serializable {

	private static final long serialVersionUID = 7580587324339303221L;

	/**
	 * Méthode pour contrôler le bon format du paramètre coefficient
	 * transitionnel
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
					.setMessage("error.evolution.format.valParam.caract.coef_tran");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		} else if (!("").equals(valParam)
				&& Double.valueOf(valParam) < WebOmphaleConfig.getConfig()
						.getCoefTranMin()
				|| Double.valueOf(valParam) > WebOmphaleConfig.getConfig()
						.getCoefTranMax()) {
			beanField
					.setMessage("error.evolution.format.valParam.caract.coef_tran");
			beanField.setChamp(this.getNomFieldError());
			beanField.setFlag(false);
			return beanField;
		}
		return beanField;
	}
}
