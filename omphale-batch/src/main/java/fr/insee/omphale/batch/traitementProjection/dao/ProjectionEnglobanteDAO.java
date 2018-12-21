package fr.insee.omphale.batch.traitementProjection.dao;

import java.util.ArrayList;

import fr.insee.omphale.batch.cps.INeurone;

/**
 *	hérite de NeuroneDef_Projection
 *	Surcharge des méthodes getWhere() et collabo()
 *
 *
 */
public class ProjectionEnglobanteDAO extends Def_projectionDAO{
    
    public String getWhere() {
        if (CacheDaoManager.testMode)  return " where id_zonage !=0 and englobante=9";
        if (CacheDaoManager.getFirstProjectionLancee().def_projection.isCalageEnglobant()) 
            return " Where ID_PROJECTION="+CacheDaoManager.getFirstProjectionLancee().def_projection.ID_PROJ_ENGLOBANTE;
        else return(" where 1=2");
        }

      public void collabo(ArrayList<INeurone> neurones) throws Exception {
          ArrayList <ProjectionEnglobanteDAO> table=new ArrayList<ProjectionEnglobanteDAO>();
          for (int i=0;i<neurones.size();i++) {
            ProjectionEnglobanteDAO projCur=(ProjectionEnglobanteDAO) neurones.get(i);
            table.add(projCur);
          }
          CacheDaoManager.tableProjectionEnglobante=table;
        }
      
      protected String getItem() {
          return "ProjectionEnglobante";
      }


}
