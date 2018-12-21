package fr.insee.omphale.core.service.projection.impl;

import java.io.Serializable;

/**
 * 
 *   objet permettant de stocker le résultat d'un controle d'ENLParam
 *     stocke le message à renvoyer, le champ sur lequel appliqué le message
 *  le flag pour dire si en erreur ou non 
 *
 */
public class ENLFieldErrorParam implements Serializable{

	private static final long serialVersionUID = -7989069367805307624L;
	private String champ;
	private String message;
	private Boolean flag;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getChamp() {
		return champ;
	}
	public void setChamp(String champ) {
		this.champ = champ;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

}
