package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfStream;
import com.lowagie.text.pdf.PdfWriter;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;


/**
 * Classe java qui permet d'écrire le pdf à partir des données.
 * <br>
 * Etapes :
 * <br>
 * - création, ouverture du Document
 * <br>
 * - définition des font utilisées
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextAFont.result.service.projectionItext.impl.AFont})
 * - écriture de la première page du Pdf
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextBPage1Service.result.service.projectionItext.impl.BPage1Service})
 * <br>
 * - début boucle sur la liste des zones du zonage
 * <br>
 * - écriture du nom de la zone et du titre "Expertise générale"
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextCExpertiseGenerale.result.service.projectionItext.impl.CExpertiseGenerale})
 * <br>
 * - écriture du tableau Population, Indicateurs, Age moyen
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextDTableau1Service.result.service.projectionItext.impl.DTableau1Service})
 * <br>
 * - écriture des graphiques Pyramide des âges, Evolution de la population, Quotients de fécondité, Quotients de décès
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextEGraphiques1Service.result.service.projectionItext.impl.EGraphiques1Service})
 * <br>
 * - écriture du titre "Expertise flux"
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextFExpertiseFlux.result.service.projectionItext.impl.FExpertiseFlux})
 * <br>
 * - écriture du tableau Flux avec les principales zones d'échange
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextGTableau2Service.result.service.projectionItext.impl.GTableau2Service})
 * <br>
 * - écriture du titre "Flux au RP"
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextHFluxAuRP.result.service.projectionItext.impl.HFluxAuRP})
 * <br>
 * - écriture des graphiques flux
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextIGraphiques2Service.result.service.projectionItext.impl.IGraphiques2Service})
 * <br>
 * - fin de la boucle sur la liste des zones du zonage
 * <br>
 * - écriture des signets
 * (classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextJSignets.result.service.projectionItext.impl.JSignets})
 * <br>
 * - fermeture du Document
 * <br>
 * <br>
 * classe appelée par {@link fr.insee.omphale.core.result.service.lancer.LancementPdfService}.
 * <br>
 * et qui utilise des HashMap de données (pour établir les graphiques, tableaux, etc.)
 * renseignés dans {@link fr.insee.omphale.core.result.service.lancer.LancementPdfService}.
 * 
 * @see fr.insee.omphale.core.result.service.lancer.LancementPdfService
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextAFont.result.service.projectionItext.impl.AFont
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextBPage1Service.result.service.projectionItext.impl.BPage1Service
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextCExpertiseGenerale.result.service.projectionItext.impl.CExpertiseGenerale
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextDTableau1Service.result.service.projectionItext.impl.DTableau1Service
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextEGraphiques1Service.result.service.projectionItext.impl.EGraphiques1Service
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextFExpertiseFlux.result.service.projectionItext.impl.FExpertiseFlux
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextGTableau2Service.result.service.projectionItext.impl.GTableau2Service
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextHFluxAuRP.result.service.projectionItext.impl.HFluxAuRP
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextIGraphiques2Service.result.service.projectionItext.impl.IGraphiques2Service
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextJSignets.result.service.projectionItext.impl.JSignets
 */
public class ITextA1ItextService {

	/**
	 * écriture du pdf à partir des données.
	 * 
	 * @param beanParametresResultat {@link fr.insee.omphale.core.result.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @param listeZones identifiants des zones du zonage  
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste par ordre alphabétique des libellé des zones
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. 
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		listeZones.get(0) --> "92"
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		listeZones.get(1) --> "75"
	 * @param zoneLibelle libellés des zones du zonage et des zones d'échange hors zonage
	 * 		<br>     
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		zoneLibelle.get("75") --> "Paris"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		etc.
	 * @param zonesEtudeZoneEch pour chaque zone d'étude, les 5 zones d'échange les plus importantes
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. : 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		zone_etude 75 	
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		List liste = zonesEtudeZoneEch.get("75"); // 5 zones d'échange les plus importantes
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(0) --> "92";
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(1) --> 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(2) --> 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(3) --> 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(4) --> 
	 * 		<br>
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		zone_etude 92 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste = zonesEtudeZoneEch.get("92"); ; // 5 zones d'échange les plus importantes
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(0) --> "75"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(1) --> 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(2) --> 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(3) --> 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(4) --> 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		etc.
	 * @param hashMap contient :
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- des libellés "Flux observés au RP", "au RP"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé du zonage
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si le zonage est un zonage utilisateur 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- la date d'exécution de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si la projection est calée ou non
	 * @param donneesPyramide données graphique Pyramide des âges
	 * @param donneesPopulation données graphique Evolution de la population
	 * @param donneesCourbe données graphique Quotients de fécondité
	 * @param donneesDec données graphique Quotients de décès
	 * @param donneesSoldeFlux données graphique Solde migratoire au RP
	 * @param donneesFluxSortants données graphiques flux avec les principales zones d'échange
	 * 		<br>
	 * 		flux sortants des zones d'étude vers les zones d'échange
	 * @param donneesFluxEntr données graphiques flux avec les principales zones d'échange
	 * 		<br>
	 * 		flux entrants des zones d'étude en provenance des zones d'échange
	 * @param donneesFluxRange données graphiques flux avec les principales zones d'échange
	 * 		<br>
	 * 		range flux sortants, flux entrants
	 * 		<br>
	 * 		permet aux graphiques de flux qui sont en regard d'avoir le même range.
	 * 		<br>
	 * 		Ex. le graphique Flux de Ain vers Rhône est gradué de 0 à 750
	 * 		et le graphique Flux de Rhône vers Ain est gradué aussi de 0 à 750
	 * @param populationAnneeDebut données tableau Population, Indicateurs, Age moyen
	 * @param populationAnneeFin données tableau Population, Indicateurs, Age moyen
	 * @param populationEvol données tableau Population, Indicateurs, Age moyen
	 * @param eDV données tableau Population, Indicateurs, Age moyen
	 * @param iCF données tableau Population, Indicateurs, Age moyen
	 * @param ageMoyenAnneeDebut données tableau Population, Indicateurs, Age moyen
	 * @param ageMoyenAnneeFin données tableau Population, Indicateurs, Age moyen
	 * @param tousAgesPlus5VersAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param tousAgesPlus5VersAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param tousAgesPlus5VersAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param tousAgesPlus5Vers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param tousAgesPlus5DeAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param tousAgesPlus5DeAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param tousAgesPlus5DeAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param tousAgesPlus5De tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @param versAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param versAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param versAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param vers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param deAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param deAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param deAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param de tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @param tousAgesPlus5ToutesZonesVersAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param tousAgesPlus5ToutesZonesVersAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param tousAgesPlus5ToutesZonesVersAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param tousAgesPlus5ToutesZonesVers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param tousAgesPlus5ToutesZonesDeAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param tousAgesPlus5ToutesZonesDeAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param tousAgesPlus5ToutesZonesDeAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param tousAgesPlus5ToutesZonesDe tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @param toutesZonesVersAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param toutesZonesVersAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param toutesZonesVersAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param toutesZonesVers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param toutesZonesDeAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param toutesZonesDeAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param toutesZonesDeAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param toutesZonesDe tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @throws Exception
	 */
	public void write(
			BeanParametresResultat beanParametresResultat,
			List<String> listeZones,
			Map<String, String> zoneLibelle,
			Map<String, List<String>> zonesEtudeZoneEch,
			Map<String, String> hashMap,
			// graphiques
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesPyramide,
			Map<String, ArrayList<Double>> donneesPopulation,
			Map<String, ArrayList<Double>> donneesCourbe,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesDec,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesSoldeFlux,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxSortants,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxEntr,
			Map<String, HashMap<String, Double>> donneesFluxRange,
			// tableaux
			Map<String, Integer> populationAnneeDebut, 
			Map<String, Integer> populationAnneeFin, 
			Map<String, Double> populationEvol,
			Map<String, HashMap<Integer, Double>> eDV,
			Map<String, Double> iCF,
			Map<String, Double> ageMoyenAnneeDebut, 
			Map<String, Double> ageMoyenAnneeFin, 
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebut,
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeFin,
			Map<String, HashMap<String, Double>> tousAgesPlus5Vers,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebut,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeFin,
			Map<String, HashMap<String, Double>> tousAgesPlus5De,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebut,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebutPlus5,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeFin,
			Map<String, HashMap<String, HashMap<String, Double>>> vers,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebut,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebutPlus5,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeFin,
			Map<String, HashMap<String, HashMap<String, Double>>> de,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebut,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebutPlus5,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeFin,
			Map<String, Double> tousAgesPlus5ToutesZonesVers,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebut,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebutPlus5,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeFin,
			Map<String, Double> tousAgesPlus5ToutesZonesDe,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebut,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeFin,
			Map<String, HashMap<String, Double>> toutesZonesVers,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebut,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeFin,
			Map<String, HashMap<String, Double>> toutesZonesDe,
			String messageAvertissement
				) throws Exception {
		
		ITextAFont fontService = new ITextAFont();
		ITextBPage1Service page1Service = new ITextBPage1Service();
		ITextCExpertiseGenerale expertiseGeneraleService = new ITextCExpertiseGenerale();
		ITextDTableau1Service tableau1Service = new ITextDTableau1Service();
		ITextEGraphiques1Service graphiques1Service = new ITextEGraphiques1Service();
		ITextFExpertiseFlux expertiseFluxProjetesService = new ITextFExpertiseFlux();
		ITextGTableau2Service tableau2Service = null;
		ITextHFluxAuRP fluxAuRPService = new ITextHFluxAuRP();
		ITextIGraphiques2Service graphiques2Service = new ITextIGraphiques2Service();
		ITextJSignets signets = new ITextJSignets();
		
		/* création, ouverture du document */
		Document document = new Document();
		
		StringBuffer filename = new StringBuffer();
		filename.append(beanParametresResultat.getNomRacineAppliShare());
		filename.append(beanParametresResultat.getNomFichierPdf());
		
		FileOutputStream fileOutputStream = new FileOutputStream(filename.toString());
		
		PdfWriter pdfWriter = PdfWriter.getInstance(
				document, 
				fileOutputStream);
		
        // taux de compression du pdf
		// sur un exemple : 
		// taux						k octets
        // -						12
		// -1 default_compression  	12
		// 0 no_compression   		37
		// 1 best_speed  			14
		// 2 level_2   				13
		// 3 level_3  				13
		// 4 level_4
		// 5 level_5
		// 6 level_6
		// 7 level_7
		// 8 level_8				12
		// 9 best_compression		12
        int compressionLevel = PdfStream.BEST_COMPRESSION; // PdfStream.NO_COMPRESSION, PdfStream.BEST_COMPRESSION, PdfStream.DEFAULT_COMPRESSION
        pdfWriter.setCompressionLevel(compressionLevel); 
        // compr image, font
        
		// images of different width and height on a same pages
        // néc.
		pdfWriter.setStrictImageSequence(true);
		
		document.open();
			
		/* définition des font utilisées */
		// Les font sont stockées dans un HashMap<String, Font>
		Map<String, Font> font = fontService.getFont();

		/* écriture de la première page */
		//	
		page1Service.setPage1(
				document,			
				font,
				hashMap,
				beanParametresResultat,
				messageAvertissement);

		document.newPage(); // fin première page
		
		/* début boucle sur la liste des zones du zonage */
		for (String zoneId: listeZones) {
			/* écriture du nom de la zone et du titre "Expertise générale" */
			expertiseGeneraleService.setNomZoneExpertiseGenerale(document, zoneId, zoneLibelle, font);		
			
			/* écriture du tableau Population, Indicateurs, Age moyen */
			tableau1Service.setTableau1(
					document,			
					font,
					zoneLibelle,
					zoneId,
					hashMap,
					/* données tableau Population, Indicateurs, Age moyen */
					populationAnneeDebut, 
					populationAnneeFin, 
					populationEvol, 
					eDV,
					iCF,
					ageMoyenAnneeDebut, 
					ageMoyenAnneeFin
					/******************************************************/
					   				   );
			
			/* écriture graphiques Pyramide des âges, Evolution de la population, Quotients de fécondité, Quotients de décès */
			graphiques1Service.setGraphiques1(
					beanParametresResultat,
					pdfWriter, 
					compressionLevel,
					document,			
					font,
					zoneLibelle,
					zoneId,
					hashMap,
					// données graphique Pyramide des âges
					donneesPyramide, 
					// données graphique Evolution de la population
					donneesPopulation,
					// données graphique Quotients de fécondité
					donneesCourbe,
					// données graphique Quotients de décès
					donneesDec
						);
						
			document.newPage();
			
			/* écriture du titre "Expertise flux" */
			expertiseFluxProjetesService.setExpertiseFlux(document, zoneId, font);
			
			/* écriture du tableau Flux avec les principales zones d'échange */
			tableau2Service = new ITextGTableau2Service(
					document,			
					font,
					zoneLibelle,
					zonesEtudeZoneEch,
					zoneId,
					hashMap					
			);
			
			if (zonesEtudeZoneEch != null && !zonesEtudeZoneEch.isEmpty() 
					&& zonesEtudeZoneEch.get(zoneId) != null && !zonesEtudeZoneEch.isEmpty()){
			tableau2Service.setTableau2(
					/* données tableau flux avec les principales zones d'échange */
					tousAgesPlus5VersAnneeDebut,
					tousAgesPlus5VersAnneeDebutPlus5,
					tousAgesPlus5VersAnneeFin,
					tousAgesPlus5Vers,
					tousAgesPlus5DeAnneeDebut,
					tousAgesPlus5DeAnneeDebutPlus5,
					tousAgesPlus5DeAnneeFin,
					tousAgesPlus5De,
					versAnneeDebut,
					versAnneeDebutPlus5,
					versAnneeFin,
					vers,
					deAnneeDebut,
					deAnneeDebutPlus5,
					deAnneeFin,
					de,
					tousAgesPlus5ToutesZonesVersAnneeDebut,
					tousAgesPlus5ToutesZonesVersAnneeDebutPlus5,
					tousAgesPlus5ToutesZonesVersAnneeFin,
					tousAgesPlus5ToutesZonesVers,
					tousAgesPlus5ToutesZonesDeAnneeDebut,
					tousAgesPlus5ToutesZonesDeAnneeDebutPlus5,
					tousAgesPlus5ToutesZonesDeAnneeFin,
					tousAgesPlus5ToutesZonesDe,
					toutesZonesVersAnneeDebut,
					toutesZonesVersAnneeDebutPlus5,
					toutesZonesVersAnneeFin,
					toutesZonesVers,
					toutesZonesDeAnneeDebut,
					toutesZonesDeAnneeDebutPlus5,
					toutesZonesDeAnneeFin,
					toutesZonesDe
					/*************************************************************/
					   				   ); 
		}else{
			tableau2Service.setTitreTableau2(document, zoneLibelle, zoneId, font);
		}

			document.newPage();
			
			/* écriture du titre "Flux au RP" */
			fluxAuRPService.setFluxAuRP(document, zoneId, font);
			
			/* écriture des graphiques flux */
			graphiques2Service.setGraphiques2Bis(
					beanParametresResultat,
					pdfWriter, 
					compressionLevel,
					document,			
					font,
					zoneLibelle,
					zonesEtudeZoneEch,
					zoneId,
					hashMap,
					/* données graphiques flux */
					donneesSoldeFlux,
					donneesFluxSortants,
					donneesFluxEntr,
					donneesFluxRange
					/***************************/
						);
			
			document.newPage();
		/* fin de la boucle sur la liste des zones du zonage */	
		}	

		/* écriture des signets */
		signets.setSignets(pdfWriter, listeZones, zoneLibelle);
		
		document.close();
		fileOutputStream.close();
		pdfWriter.close();
	}
}
