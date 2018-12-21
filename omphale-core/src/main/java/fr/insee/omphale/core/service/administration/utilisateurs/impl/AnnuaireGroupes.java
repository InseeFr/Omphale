package fr.insee.omphale.core.service.administration.utilisateurs.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "groupes")
public class AnnuaireGroupes {

	private AnnuaireGroupe groupe;

	@XmlElement(name = "groupe")
	public AnnuaireGroupe getGroupe() {
		return groupe;
	}

	public void setGroupe(AnnuaireGroupe groupe) {
		this.groupe = groupe;
	}
}
