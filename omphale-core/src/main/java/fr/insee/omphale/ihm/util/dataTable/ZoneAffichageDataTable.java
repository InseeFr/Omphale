package fr.insee.omphale.ihm.util.dataTable;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ZoneAffichageDataTable{

	@Id
	private String id;
	
	private String nom;
	
	private String libelle;
	
	private int typeZoneStandard;
	
	// attribut temporaire
	//utilisé uniquement pour récupéré toutes les régions d'une même zone
	// on stocke ensuite cet identifiant dans la Set<Integer> de la même zone
	private String regionImpactee;
	
	@Transient
	private Set<String> regionsImpactees;


	
	public ZoneAffichageDataTable() {
	}

	public ZoneAffichageDataTable(String id, String nom, String libelle,
			int typeZoneStandard, String regionImpactee) {
		super();
		this.id = id;
		this.nom = nom;
		this.libelle = libelle;
		this.typeZoneStandard = typeZoneStandard;
		this.regionImpactee = regionImpactee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getTypeZoneStandard() {
		return typeZoneStandard;
	}

	public void setTypeZoneStandard(int typeZoneStandard) {
		this.typeZoneStandard = typeZoneStandard;
	}

	public String getRegionImpactee() {
		return regionImpactee;
	}

	public void setRegionImpactee(String regionImpactee) {
		this.regionImpactee = regionImpactee;
	}

	public Set<String> getRegionsImpactees() {
		return regionsImpactees;
	}

	public void setRegionsImpactees(Set<String> regionsImpactees) {
		this.regionsImpactees = regionsImpactees;
	}

	@Override
	public String toString() {
		return "ZoneAffichageDataTable [id=" + id + ", nom=" + nom
				+ ", libelle=" + libelle + ", typeZoneStandard="
				+ typeZoneStandard + ", regionImpactee=" + regionImpactee
				+ ", regionsImpactees=" + regionsImpactees + "]";
	}
	
	

}
