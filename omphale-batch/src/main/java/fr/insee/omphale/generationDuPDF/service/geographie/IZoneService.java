package fr.insee.omphale.generationDuPDF.service.geographie;

import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.domaine.geographie.Zonage;


public interface IZoneService {

	/**
	 * Renvoie les identifiants, libellés des zones du zonage
	 * 
	 * @param zonage
	 * @return HashMap(identifiant de zone, libellé de zone)
	 */
	public Map<String, String> getZoneLibelle(Zonage zonage);
	
	/**
	 * renvoie les identifiants de zoneLibelle
	 * <br>
	 * <br>
	 * classement ordre alphabétique des libellés de zoneLibelle
	 * <br>
	 * <br>
	 * Ex. 
	 * <br>
	 * HashMap&lt.String, String&gt. zoneLibelle = new HashMap&lt.String, String&gt.();
	 * <br>
	 * zoneLibelle.put("464", "Paris")
	 * <br>
	 * zoneLibelle.put("481", "Hauts de Seine")
	 * <br>
	 * List 
	 * liste = getListeZones(hashMapZones);
	 * <br>
	 * liste.get(0) --&gt. "481"
	 * <br>
	 * liste.get(1) --&gt. "464"
	 * @param zoneLibelle 
	 * les identifiants, libellés des zones du zonage
	 * @return
	 */
	public List<String> getListeZones(Map<String, String> zoneLibelle);
	
	/**
	 * ajoute à zoneLibelle les zones d'échange, qui apparaissent dans la liste pour chaque zone d'étude des 5 principales zones d'échange,
	 * et qui sont hors zonage
	 * 
	 * @param idUser
	 * @param prefixe
	 * @param zoneLibelle
	 */
	public void add(
			String idUser,
			String prefixe,
			Map<String, String> zoneLibelle);
}
