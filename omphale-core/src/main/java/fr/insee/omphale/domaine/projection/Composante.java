package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <h2>Classe métier pour Composante</h2>
 * 
 * <p>permet de définir le type correspondant à une EvolutionNonLocalisee.</p>
 * <p>Dans les spécifications, on parle aussi de type de quotient</p>
 * <ul>
 * 	<li>Décès</li>
 *  <li>Fécondité</li>
 *  <li>Émigration étranger</li>
 *  <li>Immigrants étranger</li>
 *  <li>Taux Actifs/ménages/élèves</li>
 *  <li>Population</li>
 * </ul>
 * 
 * 
 * 
 */
@Entity
@Table(name="COMPOSANTE")
public class Composante implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COMPOSANTE", length=5)
	private String code;
	
	@Column(name="LIBELLE",length=50)
	private String libelle;
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	
}
