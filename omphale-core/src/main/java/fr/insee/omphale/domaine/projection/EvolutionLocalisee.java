/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.GenericGenerator;

import fr.insee.omphale.domaine.geographie.Zone;

/**
 * <h2>Classe métier pour </h2>
 * 
 * 
 * <p>Cette classe est une EvolutionNonLocalisee que l'on localise.</p>
 * <p>On associe cette classe à une Zone précise.</p>
 * 
 * <p>On localise une EvolutionNonLocalisee quand on crée une Projection</p>
 * 
 */
@Entity
@Table(name="EVOL_LOCALISE")
@DiscriminatorValue("0")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula(value="decode(ZONE_DESTINATION,null,0,1)")
public class EvolutionLocalisee implements Serializable {


	private static final long serialVersionUID = 1924882357774375817L;
	
	@Id
	@Column(name="ID_EVOL_LOCALISE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idElGenerator")
	@GenericGenerator(name="idElGenerator", strategy="native",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_EVOL_LOC")})
	private int id;
	
	@ManyToOne
	@JoinColumn(name="COMPOSANTE")
	private Composante composante;
	
	@ManyToOne
	@JoinColumn(name="ZONE")
	private Zone zone;
	
	@ManyToOne
	@JoinColumn(name="ID_EVOL_NON_LOC")
	private EvolutionNonLocalisee evolNonLoc;
	
	@ManyToOne
	@JoinColumn(name="ID_PROJECTION")
	private Projection projection;
	
	@Column(name="RANG")
	private int rang;

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public EvolutionNonLocalisee getEvolNonLoc() {
		return evolNonLoc;
	}

	public void setEvolNonLoc(EvolutionNonLocalisee evolNonLoc) {
		this.evolNonLoc = evolNonLoc;
	}

	public Projection getProjection() {
		return projection;
	}

	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	public int getRang() {
		return rang;
	}

	public void setRang(int rang) {
		this.rang = rang;
	}

	public Composante getComposante() {
		return composante;
	}

	public void setComposante(Composante composante) {
		this.composante = composante;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int compareTo( EvolutionLocalisee el ) {
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    int result = 0;

	    if (this.getRang() == el.getRang()) result = EQUAL;
	    if (this.getRang() < el.getRang()) result = BEFORE;
	    if (this.getRang() > el.getRang()) result = AFTER;
	    
	    return result;
	}
	
	

	
}
