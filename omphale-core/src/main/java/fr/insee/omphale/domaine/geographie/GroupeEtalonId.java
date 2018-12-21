package fr.insee.omphale.domaine.geographie;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * <h2>Classe métier pour GroupeEtalon</h2>
 * 
 * <p>Il s'agit de l'identifiant composite de GroupeEtalon.</p>
 *
 */
@Embeddable
public class GroupeEtalonId implements Serializable {

	private static final long serialVersionUID = 4640326176607416326L;
	
	@Column(name="SIGNATURE", length=20)
	private String signature;
	
	@ManyToOne
	@JoinColumn(name="ZONAGE")
	private Zonage zonage;

	public GroupeEtalonId() {

	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Zonage getZonage() {
		return zonage;
	}

	public void setZonage(Zonage zonage) {
		this.zonage = zonage;
	}

}
