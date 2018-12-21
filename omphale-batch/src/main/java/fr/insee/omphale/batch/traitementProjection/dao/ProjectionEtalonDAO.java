package fr.insee.omphale.batch.traitementProjection.dao;

import java.util.ArrayList;

import fr.insee.omphale.batch.cps.INeurone;


/**
 *	hérite de NeuroneDef_Projection
 *	Surcharge des méthodes getWhere() et collabo()
 *
 *
 */
public class ProjectionEtalonDAO extends Def_projectionDAO{
    
	
	/** 
	 * sélectionne les def_projection étalon
	 * @return String
	 */
    public String getWhere() {
        if (CacheDaoManager.testMode)  return " where id_zonage=0 and standard=1";
        if (CacheDaoManager.getFirstProjectionLancee().def_projection.ID_PROJ_ETALON != null) 
            return " Where ID_PROJECTION="+CacheDaoManager.getFirstProjectionLancee().def_projection.ID_PROJ_ETALON;
        else return(" where 1=2");
        }

      public void collabo(ArrayList<INeurone> neurones) throws Exception {
          ArrayList <ProjectionEtalonDAO> table=new ArrayList<ProjectionEtalonDAO>();
          for (int i=0;i<neurones.size();i++) {
            ProjectionEtalonDAO projCur=(ProjectionEtalonDAO) neurones.get(i);
            table.add(projCur);
          }
          CacheDaoManager.tableProjectionEtalon=table;
        }
      
 /**
  * renvoie le type de neurone projection
  * <BR>
  * utilisé dans la méthode toString().
  * @return String
  */
    protected String getItem() {
        return "ProjectionEtalon";
    }
}
