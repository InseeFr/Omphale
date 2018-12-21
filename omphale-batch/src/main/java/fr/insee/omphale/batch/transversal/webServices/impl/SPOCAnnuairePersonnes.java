package fr.insee.omphale.batch.transversal.webServices.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "personnes")
public class SPOCAnnuairePersonnes {

	private SPOCAnnuairePersonne personne;

	@XmlElement(name = "personne")
	public SPOCAnnuairePersonne getPersonne() {
		return personne;
	}

	public void setPersonne(SPOCAnnuairePersonne personne) {
		this.personne = personne;
	}
}
