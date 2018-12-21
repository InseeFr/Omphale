package fr.insee.omphale.domaine.geographie;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <h2>Classe métier pour TypeZoneStandard</h2>
 * 
 * <P>permet de définir le type de la zone standard.</p>
 * <P>Pour rappel, une zone standard est une zone définie par un administrateur du système.</p>
 * <p>Voici les différents types de zone standard possibles :</p>
 * <ul>
 * 	<li>0	Personnel</li>
 * 	<li>1	France</li>
 * 	<li>2	Regions</li>
 * 	<li>3	Departements</li>
 * 	<li>4	Zones emploi</li>
 * 	<li>5	Aire urbaine</li>
 * 	<li>6	Autres zones standards</li>
 * </ul>
 *	
 *	
 *	
 *	
 *	
 *	
 *	

 * <P></p>
 * <P></p>
 * <P></p>
 *
 */
@Entity
@Table(name="TYPE_ZONE_STANDARD")
@SuppressWarnings("serial")
public class TypeZoneStandard implements Serializable{

	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	@Id
	@Column(name="TYPE_ZONE_STANDARD", length=1)
	private int id;

	public TypeZoneStandard() {

	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
