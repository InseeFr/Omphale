/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import fr.insee.omphale.ihm.util.dataTable.AffichageUtilDataTable;

/**
 * <h2>Classe métier pour ProjectionLancee</h2>
 * 
 * 
 * <p>permet d'indiquer si le traitement d'une Projection a déjà été lancé ou pas</p>
 * <p>En effet, un batch (programme asynchrone) récupère les demandes de lancements de Projection et la traite.</p>
 * 
 */
@Entity
@Table(name="PROJECTION_LANCEE")
public class ProjectionLancee implements Serializable {


	private static final long serialVersionUID = 2677342752303976968L;
	
	@Id
	@Column(name="ID_PROJECTION_LANCEE", length=15)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idProjLancGenerator")
	@GenericGenerator(name="idProjLancGenerator", strategy="native",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_PROJECTION_LANCEE")})
	private int id;
	
	@ManyToOne
	@JoinColumn(name="ID_PROJECTION")
	private Projection projection;
	
	@Column(name="DATE_LANCEMENT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLancement;
	
	@Column(name="DATE_DEBUT_EXEC")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDebutExec;
	
	@Column(name="DATE_EXEC")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateExec;
	
	@Column(name="DONNEES", length=1)
	private Integer donnees;
	
	@Column(name="CODE_RETOUR", length=5)
	private String codeRetour;
	
	@Column(name="MESSAGE", length=200)
	private String message;
	
	@Column(name="NBR_ZONE", length=4)
	private Integer nbrZone;

	public ProjectionLancee(){}

	public Integer getDonnees() {
		return donnees;
	}

	public void setDonnees(Integer donnees) {
		this.donnees = donnees;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Projection getProjection() {
		return projection;
	}

	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	public Date getDateLancement() {
		return dateLancement;
	}
	
	public String getDateLancementAffichageDataTable(){
		return AffichageUtilDataTable.getDateAAfficher(dateLancement);
	}

	public void setDateLancement(Date dateLancement) {
		this.dateLancement = dateLancement;
	}

	public Date getDateDebutExec() {
		return dateDebutExec;
	}

	public void setDateDebutExec(Date dateDebutExec) {
		this.dateDebutExec = dateDebutExec;
	}

	public Date getDateExec() {
		return dateExec;
	}

	public void setDateExec(Date dateExec) {
		this.dateExec = dateExec;
	}

	public String getCodeRetour() {
		return codeRetour;
	}

	public void setCodeRetour(String codeRetour) {
		this.codeRetour = codeRetour;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getNbrZone() {
		return nbrZone;
	}

	public void setNbrZone(Integer nbrZone) {
		this.nbrZone = nbrZone;
	}
		
	
}
