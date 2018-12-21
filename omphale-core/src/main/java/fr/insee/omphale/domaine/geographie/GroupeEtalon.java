package fr.insee.omphale.domaine.geographie;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * <h2>Classe métier pour GroupeEtalon :</h2>
 * 
 * On trouve dans les spécifications une illustration : 
 * 
 * <p>Considérons un zonage constitué de deux zones z_1 et z_2. </p>
 * 
 * <p>La zone z_1 impacte les départements A et B ; la zone z_2 les départements B et C.</p>
 * 
 * <p>L’ensemble des départements impactés par les zones du zonage sont A, B et C.</p>
 * 
 * <p>Ces départements seront regroupés au sein d’un unique groupe de départements étalon :</p>
 * <ul>
 * 	<li>la règle 1 impose que A et B soient regroupés, ainsi que B et C ;</li>
 * 	<li>la règle 2 impose que B appartienne à un unique groupe de départements étalon, et que donc les deux regroupements potentiels ne doivent en former qu’un.</li>
 * </ul>
 * 
 * <p>Par ailleurs, le système retient sous la forme de zone_de_groupe_etalon que les zones ayant conduit 
 * à ce regroupement sont z_1 (qui a imposé de regrouper A et B),et z_2 (qui a imposé de regroupé B et C).  </p>
 * 
 * 
 * 
 * 
 * 
 * <p>Il crée donc deux zone_de_groupe_etalon qui affectent z_1 et z_2 au groupe étalon précédemment créé.</p>
 * 
 * <p>Si le zonage intégrait une zone z_3 impactant le département D, il conviendrait d’avoir deux groupes de département étalon :</p>
 * <ul>
 * 	<li>l’un constitué de A, B et C ;</li>
 * 	<li>l’autre constitué de D. Un unique groupe étalon constitué de A, B, C et D ne vérifierait pas la règle 3 (groupement minimal).</li>
 * </ul>
 *
 */
@Entity
@Table(name="GROUPE_ETALON")
public class GroupeEtalon {

	@Id
	private GroupeEtalonId id;
	
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.geographie.Zone.class)
	@JoinTable(name="ZONE_DE_GROUPET",
			joinColumns={@JoinColumn(name="SIGNATURE"),@JoinColumn(name="ZONAGE")},
			inverseJoinColumns=@JoinColumn(name="ZONE"))
	private Set<Zone> zones;
	
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.geographie.Commune.class)
	@JoinTable(name="COMMUNE_RESIDUELLE",
			joinColumns={@JoinColumn(name="SIGNATURE"),@JoinColumn(name="ZONAGE")},
			inverseJoinColumns=@JoinColumn(name="COMMUNE"))
	private Set<Commune> communesResiduelles;
	
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.geographie.Departement.class)
	@JoinTable(name="DEPT_DE_GROUPET",
			joinColumns={@JoinColumn(name="SIGNATURE"),@JoinColumn(name="ZONAGE")},
			inverseJoinColumns=@JoinColumn(name="DEPT"))
	private Set<Departement> departements;

	public GroupeEtalon() {

	}

	public GroupeEtalonId getId() {
		return id;
	}

	public void setId(GroupeEtalonId id) {
		this.id = id;
	}

	public Set<Zone> getZones() {
		return zones;
	}

	public void setZones(Set<Zone> zones) {
		this.zones = zones;
	}

	public Set<Commune> getCommunesResiduelles() {
		return communesResiduelles;
	}

	public void setCommunesResiduelles(Set<Commune> communesResiduelles) {
		this.communesResiduelles = communesResiduelles;
	}

	public Set<Departement> getDepartements() {
		return departements;
	}

	public void setDepartements(Set<Departement> departements) {
		this.departements = departements;
	}

	public Zonage getZonage() {
		return id.getZonage();
	}

	public void setZonage(Zonage zonage) {
		this.id.setZonage(zonage);
	}

	
	/**
	 * Cette methode permet d'afficher lors de la creation des zones de communes residuelles
	 * les 10 premieres communes residuelles de cette zone
	 * 
	 * @return String
	 */
	public String getDixPremieresCommunesResiduelles() {
		String dixPremieresCommunesResiduelles = "";
		if (communesResiduelles != null &&!communesResiduelles.isEmpty()){
			List<Commune> listCommunesResiduelles = new ArrayList<Commune>();
			listCommunesResiduelles.addAll(communesResiduelles);
			dixPremieresCommunesResiduelles = listCommunesResiduelles.get(0).getLibelle();
			if (communesResiduelles.size()>10){
				for (int i =1; i < 10; i++){
					dixPremieresCommunesResiduelles = dixPremieresCommunesResiduelles + ", "+listCommunesResiduelles.get(i).getLibelle();
				}
			}
			else{
				for (int i =0; i < listCommunesResiduelles.size(); i++){
					dixPremieresCommunesResiduelles = dixPremieresCommunesResiduelles + ", "+listCommunesResiduelles.get(i).getLibelle();
				}
			}
		}
		else{
			dixPremieresCommunesResiduelles = "0";
		}
		return dixPremieresCommunesResiduelles;
	}
}
