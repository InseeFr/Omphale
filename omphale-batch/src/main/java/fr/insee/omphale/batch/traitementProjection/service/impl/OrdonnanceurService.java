package fr.insee.omphale.batch.traitementProjection.service.impl;

import java.util.ArrayList;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.bean.BeanRapport;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.generationDuPDF.exception.OmphalePopulationNegativeException;


public class OrdonnanceurService implements INeuroneNonDAO {

    
    private int etape=-1;
    private String libelle;
    private IServiceBatch service;
    private int code=CacheDaoManager.STATUS_SUCCES;
    private String message="";
    private boolean isFinProjection=false;
    private boolean acceptTest=false;
    public static OrdonnanceurService lastTraitement=null;
    public static OrdonnanceurService lastTraitementOk=null;
    

    /**
     * lance l'exécution d'un service.
     * Service étant une étape du traitement d'une projection
     * 
     * @return int
     */
    public int execute() {
        BeanRapport messager=CacheDaoManager.beanRapport;
        if (!isFinProjection) messager.etape=this.etape;
        
        if (CacheDaoManager.testMode) {
            if (acceptTest)  {
               try {
                service.executeService();
            } catch (OmphaleConfigException e) {
                e.printStackTrace();
            } catch (OmphalePopulationNegativeException e) {
				e.printStackTrace();
			} catch (OmphaleMetierException e) {
                e.printStackTrace();
            } 
            }
            return this.code;
        }
        
        if (service==null) {
            this.code=CacheDaoManager.STATUS_ERROR_CONFIG;
            this.message=this.libelle+" service manquant";
            if (isFinProjection) {
               if (messager.code != this.code) messager.code=this.code;
               messager.message+="\n"+this.message;
            } else {
                messager.code=this.code;
                messager.message=this.message;
            }
            return this.code;
         }
        
            try {
                service.executeService();
            } catch (OmphaleConfigException e) {
                this.code=CacheDaoManager.STATUS_ERROR_CONFIG;
                this.message=e.getMessage();
                if (isFinProjection) {
                    if (messager.code != this.code) messager.code=this.code;
                    messager.message+="\n"+this.message;
                 } else {
                     messager.code=this.code;
                     messager.message=this.message;
                 }
                 return this.code;
                
            } catch (OmphalePopulationNegativeException e) {
            	this.code=CacheDaoManager.STATUS_ERROR_OMPHALE;
            	 this.message=e.getMessage();
            	 if (isFinProjection) {
                     if (messager.code != CacheDaoManager.STATUS_ERROR_CONFIG) messager.code=this.code;
                     messager.message+="\n"+this.message;
                  } else {
                      messager.code=this.code;
                      messager.message=this.message;
                  }
                  return this.code;
            	
			} catch (OmphaleMetierException e) {
                this.code=CacheDaoManager.STATUS_ERROR_OMPHALE;
                this.message=e.getMessage();
                if (isFinProjection) {
                    if (messager.code != CacheDaoManager.STATUS_ERROR_CONFIG) messager.code=this.code;
                    messager.message+="\n"+this.message;
                 } else {
                     messager.code=this.code;
                     messager.message=this.message;
                 }
                 return this.code;
            }
            if (!isFinProjection) {
                lastTraitementOk=this; 
            }
            return this.code;
    }

    public ArrayList<Object> getDeepMemory() throws Exception {

        ArrayList<Object> services=new ArrayList<Object>();
        OrdonnanceurService serv;
        
        serv=new OrdonnanceurService();
        serv.libelle="Menage par suppression des tables temporaires";
        serv.service=new MenageService();
        serv.etape=0;
        services.add(serv);
        
        serv=new OrdonnanceurService();
        serv.libelle="Execution de la projection";
        serv.service=new ExecuteProjectionService();
        serv.etape=1;
        serv.acceptTest=true;
        services.add(serv);
        
        serv=new OrdonnanceurService();
        serv.libelle="Export des fichiers resultat au format CSV";
        serv.service=new ExportCsvService();
        serv.etape=2;
        services.add(serv);
        
        serv=new OrdonnanceurService();
        serv.libelle="Production du fichier PDF des graphiques";
        serv.service=new GraphiquePdfService();
        serv.etape=3;
        services.add(serv);
                
        serv=new OrdonnanceurService();
        serv.libelle="Production du fichier fichier zip des resultats";
        serv.service=new ZipService();
        serv.etape=4;
        services.add(serv);
    
        if (!("false".equalsIgnoreCase(OmphaleBatchConfig.menageTables))) {
            serv=new OrdonnanceurService();
            serv.libelle="Menage par suppression des tables temporaires";
            serv.service=new MenageService();
            serv.etape=6;
            serv.isFinProjection=true;
            services.add(serv);
        }
        
        serv=new OrdonnanceurService();
        serv.libelle="Mise a jour de la table des projections lancees";
        serv.service=new UpdateBaseService();
        serv.etape=7;
        serv.isFinProjection=true;
        services.add(serv);
        

        
        serv=new OrdonnanceurService();
        serv.libelle="Envoi du mail utilisateur";
        serv.service=new MailService();
        serv.etape=8;
        serv.isFinProjection=true;
        services.add(serv);
        
        return services;
    }

    /**
     * initialise les tables des différents procédures de traitement et de fin
     *@param ArrayList(INeurone)
     * @return void
     */
    public void collabo(ArrayList<INeurone> neurones) throws Exception {
        ArrayList <OrdonnanceurService> tableFin=new ArrayList<OrdonnanceurService>();
        ArrayList <OrdonnanceurService> tableTrt=new ArrayList<OrdonnanceurService>();
        for (int i=0;i<neurones.size();i++) {
          OrdonnanceurService projCur=(OrdonnanceurService) neurones.get(i);
          if (projCur.isFinProjection) tableFin.add(projCur);
          else {
              tableTrt.add(projCur);
              lastTraitement=projCur;
          }
        }
        CacheDaoManager.tableProceduralServiceFin=tableFin;
        CacheDaoManager.tableProceduralServiceTraitement=tableTrt;
        
    }

    public String getMapKey() {
        if (isFinProjection) return "FIN";
        return "TRAITEMENT";
    }

    public void setFinProjection(boolean isFinProjection) {
        this.isFinProjection = isFinProjection;
    }
    public boolean parseNeurone(INeurone modele) throws Exception {
        
        return true;
    }

    public void setCreateSynapseParameters(Object fait) {
        setFait(fait);
        
    }

    public void setDeepFait(Object fait) {
        setFait(fait);
        
    }

    public void setRechercheParameters(Object fait) {
        setFait(fait);
        
    }
    public void setFait(Object fait) {
       this.etape = ((OrdonnanceurService) fait).getEtape();
       this.service=((OrdonnanceurService) fait).getService();
       this.code=((OrdonnanceurService) fait).getCode();
       this.message=((OrdonnanceurService) fait).getMessage();
       this.libelle=((OrdonnanceurService) fait).getLibelle();
       this.isFinProjection=((OrdonnanceurService) fait).isFinProjection;
       this.acceptTest=((OrdonnanceurService) fait).acceptTest;
    }

    public int getEtape() {
        return etape;
    }

    public void setEtape(int etape) {
        this.etape = etape;
    }

    public IServiceBatch getService() {
        return service;
    }

    public void setService(IServiceBatch service) {
        this.service = service;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }


}
