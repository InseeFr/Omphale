package fr.insee.omphale.core.service.administration.utilisateurs.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "personnes")
public class Personnes {

	private AnnuairePersonne personne;

	@XmlElement(name = "personne")
	public AnnuairePersonne getPersonne() {
		return personne;
	}

	public void setPersonne(AnnuairePersonne personne) {
		this.personne = personne;
	}
}

