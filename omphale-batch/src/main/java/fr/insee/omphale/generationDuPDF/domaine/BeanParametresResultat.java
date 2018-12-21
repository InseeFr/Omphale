package fr.insee.omphale.generationDuPDF.domaine;

import java.io.Serializable;


public class BeanParametresResultat implements Serializable{


   
	private static final long serialVersionUID = 1574006850402153246L;
    
    String idUser;
	String idProjection;
	String idLancement;
	String nomFichierPdf;
	String nomDossier;
	String nomRacineAppliShare;
    String nomBaseFichiers;
	

    String anneeReference; // = "2006";
	String anneeHorizon; // = "2031";
	String prefixe; // = "ZP";
	String calage; // 0, 1
	Integer age100; // = "99";
	Integer ageDebutMere; //		14
	Integer ageFinMere; //		49
	String idZonage; //
	
	String ftpServer;//serveur ftp
    String ftpPort;//serveur ftp
    String ftpUser;//serveur ftp
    String ftpPwd;//serveur ftp
    
    String ficheIdentite;
    
    
    public BeanParametresResultat() {}
     
    
    public String getNomRacineAppliShare() {
		return nomRacineAppliShare;
	}


	public void setNomRacineAppliShare(String nomRacineAppliShare) {
		this.nomRacineAppliShare = nomRacineAppliShare;
	}


	public String getFtpPort() {
        return ftpPort;
    }


    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }


    public String getFtpUser() {
        return ftpUser;
    }


    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }


    public String getFtpPwd() {
        return ftpPwd;
    }


    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

	
	public String getFtpServer() {
        return ftpServer;
    }


    public void setFtpServer(String ftpServer) {
        this.ftpServer = ftpServer;
    }

	
	public String getNomDossier() {
	     return nomDossier;
	}


	public void setNomDossier(String nomDossier) {
	     this.nomDossier = nomDossier;
	}

	
	public String getNomBaseFichiers() {
	     return nomBaseFichiers;
	}


	public void setNomBaseFichiers(String nomBaseFichiers) {
	    this.nomBaseFichiers = nomBaseFichiers;
	}
	    
	
	public String getIdUser() {
		return idUser;
	}


	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}


	public String getAnneeReference() {
		return anneeReference;
	}


	public void setAnneeReference(String anneeReference) {
		this.anneeReference = anneeReference;
	}


	public String getAnneeHorizon() {
		return anneeHorizon;
	}


	public void setAnneeHorizon(String anneeHorizon) {
		this.anneeHorizon = anneeHorizon;
	}


	public String getPrefixe() {
		return prefixe;
	}


	public void setPrefixe(String prefixe) {
		this.prefixe = prefixe;
	}

	public Integer getAge100() {
		return age100;
	}


	public void setAge100(Integer age100) {
		this.age100 = age100;
	}


	public Integer getAgeDebutMere() {
		return ageDebutMere;
	}


	public void setAgeDebutMere(Integer ageDebutMere) {
		this.ageDebutMere = ageDebutMere;
	}


	public Integer getAgeFinMere() {
		return ageFinMere;
	}


	public void setAgeFinMere(Integer ageFinMere) {
		this.ageFinMere = ageFinMere;
	}


	public String getIdZonage() {
		return idZonage;
	}


	public void setIdZonage(String idZonage) {
		this.idZonage = idZonage;
	}


	public String getIdLancement() {
		return idLancement;
	}


	public void setIdLancement(String idLancement) {
		this.idLancement = idLancement;
	}


	public String getNomFichierPdf() {
		return nomFichierPdf;
	}


	public void setNomFichierPdf(String nomFichierPdf) {
		this.nomFichierPdf = nomFichierPdf;
	}


	public String getIdProjection() {
		return idProjection;
	}


	public void setIdProjection(String idProjectionLancee) {
		this.idProjection = idProjectionLancee;
	}

	public String getCalage() {
		return calage;
	}

	public void setCalage(String calage) {
		this.calage = calage;
	}


	public String getFicheIdentite() {
		return ficheIdentite;
	}


	public void setFicheIdentite(String ficheIdentite) {
		this.ficheIdentite = ficheIdentite;
	}

	@Override
	public String toString() {
		return "BeanParametresResultat [idUser=" + idUser + ", idProjection="
					+ idProjection + ", idLancement=" + idLancement
					+ ", nomFichierPdf=" + nomFichierPdf + ", nomDossier="
					+ nomDossier + ", nomRacineAppliShare=" + nomRacineAppliShare
					+ ", nomBaseFichiers=" + nomBaseFichiers + ", anneeReference="
					+ anneeReference + ", anneeHorizon=" + anneeHorizon
					+ ", prefixe=" + prefixe + ", calage=" + calage + ", age100="
					+ age100 + ", ageDebutMere=" + ageDebutMere + ", ageFinMere="
					+ ageFinMere + ", idZonage=" + idZonage + ", ftpServer="
					+ ftpServer + ", ftpPort=" + ftpPort + ", ftpUser=" + ftpUser
					+ ", ftpPwd=" + ftpPwd + ", ficheIdentite=" + ficheIdentite
					+ "]";
	}
	
	
	
}
