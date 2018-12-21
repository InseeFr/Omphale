package fr.insee.omphale.batch.transversal.util;

import org.apache.commons.configuration.Configuration;

import fr.insee.config.InseeConfig;


/**
 * Classe pivot de rassemblement des paramètres properties ou issus des tables
 * Oracle ou pour les tests en dur.
 */
public class OmphaleBatchConfig {
    public static Configuration config = InseeConfig.getConfig();
    /**
     * partage entre projections légères et projections lourdes.
     */
    public static String nbrZoneGrosZonage=config.getString("fr.insee.omphale.zonage.gros");   
    
    //nbrZoneSuperGrosZonage
    
    public static String nbrZoneSuperGrosZonage=config.getString("fr.insee.omphale.zonage.supergros"); 
    
    //heureSuperGrosZonage
    
    public static String heureSuperGrosZonage=config.getString("fr.insee.omphale.zonage.supergros.heure");
    
    //nombre de zones d'échanges en sortie csv
    public static String nbrFluxGrosZonage=config.getString("fr.insee.omphale.zonage.gros.flux"); 
    
    public static String nbrFluxPetitZonage=config.getString("fr.insee.omphale.zonage.petit.flux"); 
    
    
    public static String indicateurEnglobanteFigee="9";
    public static String indicateurEnglobantePotentiel="1";
    /**
     * configuration des repertoires de gestion des données résultat sur le
     * serveur d'application : séparateur de répertoire.
     */
    public static String dirSeparateur =  System.getProperty("file.separator"); 
    
    public static String prefixe_PDF = "ZP";
    



        
    /**
     * nom de la connexion dans le fichier properties.
     */
    public static String nomConnexion = "omphale";
    /**
     * paramètres statistiques généraux. 
     * âge début des vieux pour lesquels on effectue une
     * régression linéaire des quotients de décès.
     */
    public static String age_old = "80";

    /**
     * paramètres statistiques généraux.
     * âge fin des vieux pour lesquels on le regroupement des
     * plus agés.
     */
    public static String age_last = "99";

    /**
     * paramètres statistiques généraux. 
     * âge début de fécondité des femmes.
     */
    public static String mere_inf = "14";

    /**
     * paramètres statistiques généraux.
     * âge fin de fécondité des femmes.
     */
    public static String mere_sup = "49";

    /**
     * proportion des garçons parmi les naissances.
     */
    public static String part_gars = "0.512";

    /**
     * configuration du serveur FTP : adresse IP du serveur.
     */
    public static String ftpServer = config.getString("fr.insee.omphale.ftp.host");

    /**
     * configuration du serveur FTP : port d'écoute du serveur.
     */
    public static String ftpPort = config.getString("fr.insee.omphale.ftp.port");

    /**
     * configuration du serveur FTP : utilisateur de connexion au serveur.
     */
    public static String ftpUser = config.getString("fr.insee.omphale.ftp.username");

    /**
     * configuration du serveur FTP : mot de passe de connexion au serveur.
     */
    public static String ftpPass = config.getString("fr.insee.omphale.ftp.password");
    
    /**
     * configuration du serveur FTP : sous repertoire dedie aux resultats de projection
     */
    public static String ftpDir=config.getString("fr.insee.omphale.ftp.dir");
    

    /**
     * configuration de test du service mail
     * si "false" alors le fichier des graphiques PDF n'est
     * pas fourni en pièce jointe pour faciliter les tests.
     */
    public static String mailPieceJointe=config.getString("fr.insee.omphale.mail.pj");
    /**
     * emetteur par défaut.
     * IDEP (si length==6) ou adresse mail emetteur pour les messages de fin de projection.
     */
    public static String mailFrom=config.getString("fr.insee.omphale.mail.from");
    /**
     * Destinataire par défaut.
     * IDEP (si length==6) ou adresse mail qui surcharge celui des utilisateurs pour les messages de fin de projection.
     * pour faciliter les tests.
     */
    public static String mailDestinataire=config.getString("fr.insee.omphale.mail.dest");
    /**
     * Destinataire lors du chargement du RP .
     * IDEP (si length==6) ou adresse mail.
     */
    public static String mailDestinataireChargementRP=config.getString("fr.insee.omphale.mail.chargement.dest");
    /**
     * Copy par défaut.
     * IDEP (si length==6) ou adresse mail de l'utilisateur en copie des messages de fin de projection (sans la pièce jointe).
     */
    public static String mailCopie=config.getString("fr.insee.omphale.mail.copy");
    /**
     * configuration de test des projections
     */
    public static String menageTables=config.getString("fr.insee.omphale.menage.fin.faire");
    /**
     * code retour dans la table des projections lancées pour signaler une annulation par l'utilisateur.
     * doit être géré par le batch tant que l'IHM laisse à null la date debut d'exécution.
     */
    public static int codeAnnulationProjection=60;
    
    public static String idZonageEtalon="0";
    
    public static int pas_de_projection=(config.getString("fr.insee.omphale.projection.pas")==null) ? 5 : config.getInt("fr.insee.omphale.projection.pas");

    public static String racine_script_projection=(pas_de_projection==1) ? "fr.insee.omphale.script.proj_annuelle_refonte" : "fr.insee.omphale.script.projection";

    public static String racine_script_chargement="fr.insee.omphale.script.chargement";
    
    
    
    /**
     * url du web service IGESA
     */
    public static String IGESA_SEND_SERVICE_URL =  config.getString("fr.insee.omphale.urlIgesa");
    
    /**
     * configuration des repertoires de gestion des données résultat sur le
     * serveur d'application : path du répertoire parent des données.
     */
    public static String APPLISHARE_RacineDepotResultatProjection = config.getString("fr.insee.omphale.data.dir.appliShare");   
    
    /**
     * informations de localisation de depôt des résultats CSV pour les mails envoyes aux utilisateurs
     */

    public static String APPLISHARE_RacineResultatProjectionAffichageMail=config.getString("fr.insee.omphale.depotUtilisateur.appliShare.mail");
    
  
    
    /**
     * SPOC : url du web service
     */
	public static String SPOC_SEND_SERVICE_URL = config.getString("fr.insee.omphale.SPOC_SEND_SERVICE_URL");
	
    /**
     * SPOC : identifiant ayant les droits de se connecter au web service
     */
	public static String SPOC_IDENT = config.getString("fr.insee.omphale.SPOC_IDENT");

	
    /**
     * SPOC : mot de passe de l'identifiant ayant les droits de se connecter au web service
     */
	public static String SPOC_PASSW =  config.getString("fr.insee.omphale.SPOC_PASSW");


    /**
     * SPOC : fichier 
     */
	public static String SPOC_pathFichierXMLAGenerer = config.getString("fr.insee.omphale.SPOC_pathFichierXMLAGenerer");
	
    /**
     * SPOC : key store certificat
     */
    public static String SPOC_TRUST_STORE = config.getString("fr.insee.omphale.SPOC_TRUST_STORE");


    /**
     * SPOC : mot de passe pour key store certificat
     */
	public static String SPOC_TRUST_STORE_PASSWORD = config.getString("fr.insee.omphale.SPOC_TRUST_STORE_PASSWORD");
    
    
}
