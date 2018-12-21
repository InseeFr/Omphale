package fr.insee.omphale.core.service.administration.utilisateurs.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "cn", "description", "personnes" })
public class AnnuaireGroupe {

	private String cn, description;
	private List<AnnuairePersonne> personnes;

	@XmlElement(name = "cn")
	public String getCn() {
		return cn;
	}

	@XmlElement(name = "description")
	public String getDescription() {
		return description;
	}

	@XmlElementWrapper(name = "personnes")
	@XmlElement(name = "personne")
	public List<AnnuairePersonne> getPersonnes() {
		return personnes;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPersonnes(List<AnnuairePersonne> personnes) {
		this.personnes = personnes;
	}
}
