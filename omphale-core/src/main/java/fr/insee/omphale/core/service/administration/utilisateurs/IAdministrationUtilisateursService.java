package fr.insee.omphale.core.service.administration.utilisateurs;

import fr.insee.omphale.core.service.IUtilisateurService;
import fr.insee.omphale.core.service.administration.utilisateurs.impl.AnnuaireGroupe;
import fr.insee.omphale.core.service.administration.utilisateurs.impl.EIdepLdap;

public interface IAdministrationUtilisateursService {

	public boolean ajoutUtilisateurAuGroupeLDAP(String idepAAjouter ) throws Exception;
	
	public boolean suppressionUtilisateurAuGroupeLDAP(String idepASupprimer) throws Exception ;

	public AnnuaireGroupe recupereGroupeApplicatif(String nomGroupeLDAP, String nomApplication);
	
	public boolean isIdepExiste(String idep);
	
	public boolean isIdepExisteDansGroupeLDAP(String idepAVerifier);
	
	public String afficheAdresseServeurLDAP();
	
	public AnnuaireGroupe getGroupeApplicatif();
	
	/**
	 * recherche en base dans table UserOmphale 
	 * 	si l'utilisateur est présent 
	 * 
	 * @param idep
	 * @param idUser
	 * @return
	 */
	public Boolean isDejaEnBase(String idep, IUtilisateurService utilisateurService);
	
	/**
	 * Recherche à travers le web service Igesa l'uid = idep de l'utilisateur
	 * 
	 * @param idep
	 * @return
	 */
	public String rechercherParWebServiceIgesa(String idep);
	
	public EIdepLdap verificationIdep(String idep, IUtilisateurService utilisateurService) throws Exception;


}
