package fr.insee.omphale.domaine.geographie;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import fr.insee.omphale.domaine.Utilisateur;

/**
 * <h2>Classe métier pour ZoneStandardFrance</h2>
 * 
 * 	<p>Cette classe hérite de la classe mère Zone.</p>
 * 
 * <p>La liste des Communes est spécifique à cette Zone standard France.</p>
 *
 */
@Entity
@DiscriminatorValue("1")
@SuppressWarnings("serial")
public class ZoneStandardFrance extends Zone {
	
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.geographie.Commune.class)
	@JoinTable(name="COMMUNE_DE_ZSF",
			joinColumns=@JoinColumn(name="ZONE"),
			inverseJoinColumns=@JoinColumn(name="COMMUNE"))
	private Set<Commune> communes;

	public ZoneStandardFrance() {
		super();
	}

	public ZoneStandardFrance(String id, String nom, String libelle,
			Utilisateur utilisateur, TypeZoneStandard typeZoneStandard,
			Set<Departement> departementsImpactes, Set<Commune> communes) {
		super(id, nom, libelle, utilisateur, typeZoneStandard,
				departementsImpactes);
		this.communes = communes;
	}

	@Override
	public Set<Commune> getCommunes() {
		return communes;
	}

	@Override
	public void setCommunes(Set<Commune> communes) {
		this.communes = communes;
	}
}
