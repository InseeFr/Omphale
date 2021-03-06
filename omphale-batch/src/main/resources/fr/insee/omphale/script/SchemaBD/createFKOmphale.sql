
ALTER TABLE ZONAGE add CONSTRAINT ZONAGE_ETR_USER_0 FOREIGN KEY (ID_USER) REFERENCES USER_OMPHALE (ID_USER) ;
  
ALTER TABLE DEF_PROJECTION add CONSTRAINT DEF_PROJECTION_ETR_ZONA_0 FOREIGN KEY (ID_ZONAGE) REFERENCES ZONAGE (ID_ZONAGE) ; 
ALTER TABLE DEF_PROJECTION add CONSTRAINT DEF_PROJECTION_ETR_USER_0 FOREIGN KEY (ID_USER)REFERENCES USER_OMPHALE (ID_USER) ; 
ALTER TABLE DEF_PROJECTION add CONSTRAINT DEF_PROJECTION_ETR_SCNL_0 FOREIGN KEY (ID_SCENARIO)REFERENCES SCENAR_NON_LOC (ID_SCENARIO) ; 
ALTER TABLE DEF_PROJECTION add CONSTRAINT DEF_PROJECTION_ETR_ETALON FOREIGN KEY (ID_PROJ_ETALON)REFERENCES DEF_PROJECTION (ID_PROJECTION) ; 
ALTER TABLE DEF_PROJECTION add CONSTRAINT DEF_PROJECTION_ETR_ENGLOB FOREIGN KEY (ID_PROJ_ENGLOBANTE)REFERENCES DEF_PROJECTION (ID_PROJECTION) ;
  
ALTER TABLE DEPARTEMENT add CONSTRAINT DEPARTEMENT_ETR_REG_0 FOREIGN KEY (ID_REGION)REFERENCES REGION (ID_REGION) ;
   
ALTER TABLE ZONE add CONSTRAINT ZONE_ETR_USER_0 FOREIGN KEY (ID_USER)REFERENCES USER_OMPHALE (ID_USER) ; 
ALTER TABLE ZONE add CONSTRAINT ZONE_ETR_TZS_0 FOREIGN KEY (TYPE_ZONE_STANDARD)REFERENCES TYPE_ZONE_STANDARD (TYPE_ZONE_STANDARD) ;
   
ALTER TABLE DEPARTEMENT_IMPACT add CONSTRAINT DEPARTEMENT_IMPACT_ETR_DEPT_0 FOREIGN KEY (DEPT)REFERENCES DEPARTEMENT (ID_DEPT) ; 
ALTER TABLE DEPARTEMENT_IMPACT add CONSTRAINT DEPARTEMENT_IMPACT_ETR_ZONE_0 FOREIGN KEY (ZONE)REFERENCES ZONE (ID_ZONE) ;
  
ALTER TABLE GROUPE_ETALON add CONSTRAINT GROUPE_ETALON_ETR_ZONA_0 FOREIGN KEY (ZONAGE)REFERENCES ZONAGE (ID_ZONAGE) ;
  
ALTER TABLE DEPT_DE_GROUPET add CONSTRAINT DEPT_DE_GROUPET_ETR_DEPT_0 FOREIGN KEY (DEPT)REFERENCES DEPARTEMENT (ID_DEPT) ; 
ALTER TABLE DEPT_DE_GROUPET add CONSTRAINT DEPT_DE_GROUPET_ETR_GET_0 FOREIGN KEY (ZONAGE, SIGNATURE)REFERENCES GROUPE_ETALON (ZONAGE, SIGNATURE) ;

ALTER TABLE METHODE_EVOL add CONSTRAINT METHODE_EVOL_ETR_TQT_0 FOREIGN KEY (COMPOSANTE)REFERENCES COMPOSANTE (COMPOSANTE) ; 
ALTER TABLE METHODE_EVOL add CONSTRAINT METHODE_EVOL_ETR_TENT_0 FOREIGN KEY (TYPE_ENTITE)REFERENCES TYPE_ENTITE (TYPE_ENTITE) ;
   
ALTER TABLE USER_HYPOTHESE add CONSTRAINT USER_HYPOTHESE_ETR_USER_0 FOREIGN KEY (ID_USER)REFERENCES USER_OMPHALE (ID_USER) ; 
ALTER TABLE USER_HYPOTHESE add CONSTRAINT USER_HYPOTHESE_ETR_TENT_0 FOREIGN KEY (TYPE_ENTITE)REFERENCES TYPE_ENTITE (TYPE_ENTITE) ;
   
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_USER_0 FOREIGN KEY (ID_USER)REFERENCES USER_OMPHALE (ID_USER) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_UHY_0 FOREIGN KEY (ID_HYPOTHESE)REFERENCES USER_HYPOTHESE (ID_HYPOTHESE) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_MEV_0 FOREIGN KEY (METHODE_EVOL)REFERENCES METHODE_EVOL (METHODE_EVOL) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_TPAR_1 FOREIGN KEY (TYPE_PARAM1)REFERENCES TYPE_PARAM (TYPE_PARAM) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_TPAR_2 FOREIGN KEY (TYPE_PARAM2)REFERENCES TYPE_PARAM (TYPE_PARAM) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_TPAR_3 FOREIGN KEY (TYPE_PARAM3)REFERENCES TYPE_PARAM (TYPE_PARAM) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_TPAR_4 FOREIGN KEY (TYPE_PARAM4)REFERENCES TYPE_PARAM (TYPE_PARAM) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_TQT_0 FOREIGN KEY (COMPOSANTE)REFERENCES COMPOSANTE (COMPOSANTE) ; 
ALTER TABLE EVOL_NON_LOC add CONSTRAINT EVOL_NON_LOC_ETR_TENT_0 FOREIGN KEY (TYPE_ENTITE)REFERENCES TYPE_ENTITE (TYPE_ENTITE) ;
   
ALTER TABLE EVOL_LOCALISE add CONSTRAINT EVOL_LOCALISE_ETR_ZONA_0 FOREIGN KEY (ZONAGE)REFERENCES ZONAGE (ID_ZONAGE) ; 
ALTER TABLE EVOL_LOCALISE add CONSTRAINT EVOL_LOCALISE_ETR_ZONE_1 FOREIGN KEY (ZONE)REFERENCES ZONE (ID_ZONE) ; 
ALTER TABLE EVOL_LOCALISE add CONSTRAINT EVOL_LOCALISE_ETR_ZONE_2 FOREIGN KEY (ZONE_DESTINATION)REFERENCES ZONE (ID_ZONE) ; 
ALTER TABLE EVOL_LOCALISE add CONSTRAINT EVOL_LOCALISE_ETR_TQT_0 FOREIGN KEY (COMPOSANTE)REFERENCES COMPOSANTE (COMPOSANTE) ; 
ALTER TABLE EVOL_LOCALISE add CONSTRAINT EVOL_LOCALISE_ETR_EVNL_0 FOREIGN KEY (ID_EVOL_NON_LOC)REFERENCES EVOL_NON_LOC (ID_EVOL_NON_LOC) ; 
ALTER TABLE EVOL_LOCALISE add CONSTRAINT EVOL_LOCALISE_ETR_DPROJ_0 FOREIGN KEY (ID_PROJECTION)REFERENCES DEF_PROJECTION (ID_PROJECTION) ;
  
ALTER TABLE EVOL_DE_SCENAR add CONSTRAINT EVOL_DE_SCENAR_ETR_EVNL_0 FOREIGN KEY (ID_EVOL_NON_LOC)REFERENCES EVOL_NON_LOC (ID_EVOL_NON_LOC) ; 
ALTER TABLE EVOL_DE_SCENAR add CONSTRAINT EVOL_DE_SCENAR_ETR_SCNL_0 FOREIGN KEY (ID_SCENARIO)REFERENCES SCENAR_NON_LOC (ID_SCENARIO) ;
   
ALTER TABLE PARAM_METH_EVOL add CONSTRAINT PARAM_METH_EVOL_ETR_MEV_0 FOREIGN KEY (METHODE_EVOL)REFERENCES METHODE_EVOL (METHODE_EVOL) ; 
ALTER TABLE PARAM_METH_EVOL add CONSTRAINT PARAM_METH_EVOL_ETR_TPAR_0 FOREIGN KEY (TYPE_PARAM)REFERENCES TYPE_PARAM (TYPE_PARAM) ;
 
ALTER TABLE USER_POPULATION add CONSTRAINT USER_POPULATION_ETR_ZONA_0 FOREIGN KEY (ZONAGE)REFERENCES ZONAGE (ID_ZONAGE) ; 
ALTER TABLE USER_POPULATION add CONSTRAINT USER_POPULATION_ETR_DPROJ_0 FOREIGN KEY (PROJECTION)REFERENCES DEF_PROJECTION (ID_PROJECTION) ;

ALTER TABLE ZONE_DE_GROUPET add CONSTRAINT ZONE_DE_GROUPET_ETR_GET_0 FOREIGN KEY (ZONAGE, SIGNATURE)REFERENCES GROUPE_ETALON (ZONAGE, SIGNATURE) ; 
ALTER TABLE ZONE_DE_GROUPET add CONSTRAINT ZONE_DE_GROUPET_ETR_TZON_0 FOREIGN KEY (ZONE)REFERENCES ZONE (ID_ZONE) ;
 
ALTER TABLE ZONE_DE_ZONAGE add CONSTRAINT ZONE_DE_ZONAGE_ETR_ZONA_0 FOREIGN KEY (ZONAGE)REFERENCES ZONAGE (ID_ZONAGE) ; 
ALTER TABLE ZONE_DE_ZONAGE add CONSTRAINT ZONE_DE_ZONAGE_ETR_ZONE_0 FOREIGN KEY (ZONE)REFERENCES ZONE (ID_ZONE) ;
 
ALTER TABLE CB_POPULATION add CONSTRAINT CB_POPULATION_ETR_ZONE_0 FOREIGN KEY (ZONE) REFERENCES ZONE (ID_ZONE) ; 
ALTER TABLE CB_POPULATION add CONSTRAINT CB_POPULATION_ETR_UPOP_0 FOREIGN KEY (PROJECTION, SURVIVANT) REFERENCES USER_POPULATION (PROJECTION, SURVIVANT);

ALTER TABLE CB_DECES add CONSTRAINT CB_DECES_ETR_COM_0 FOREIGN KEY (COMMUNE) REFERENCES COMMUNE (ID_COMMUNE);
 
ALTER TABLE CB_FLUX add CONSTRAINT CB_FLUX_ETR_COM_1 FOREIGN KEY (ORIGINE)REFERENCES COMMUNE (ID_COMMUNE);
ALTER TABLE CB_FLUX add CONSTRAINT CB_FLUX_ETR_COM_2 FOREIGN KEY (DESTINATION)	  REFERENCES COMMUNE (ID_COMMUNE);
 
ALTER TABLE CB_HYPOTHESE add CONSTRAINT CB_HYPOTHESE_ETR_UHY_0 FOREIGN KEY (ID_HYPOTHESE) REFERENCES USER_HYPOTHESE (ID_HYPOTHESE);
  
ALTER TABLE CB_NAISSANCES add CONSTRAINT CB_NAISSANCES_ETR_COM_0 FOREIGN KEY (COMMUNE) REFERENCES COMMUNE (ID_COMMUNE);
  
ALTER TABLE CB_POP_COLLECTE add CONSTRAINT CB_POP_COLLECTE_ETR_COM_0 FOREIGN KEY (COMMUNE) REFERENCES COMMUNE (ID_COMMUNE);
 
ALTER TABLE CB_POP_LEGALE add CONSTRAINT CB_POP_LEGALE_ETR_COM_0 FOREIGN KEY (COMMUNE) REFERENCES COMMUNE (ID_COMMUNE);
ALTER TABLE CB_POP_LEGALE add CONSTRAINT CB_POP_LEGALE_ETR_TPOP_0 FOREIGN KEY (TYPE_POP) REFERENCES TYPE_POP (TYPE_POP); 

ALTER TABLE COMMUNE add CONSTRAINT COMMUNE_ETR_DEPT_0 FOREIGN KEY (ID_DEPT) REFERENCES DEPARTEMENT (ID_DEPT);
ALTER TABLE COMMUNE add CONSTRAINT COMMUNE_ETR_REG_0 FOREIGN KEY (ID_REGION) REFERENCES REGION (ID_REGION);


ALTER TABLE COMMUNE_DE_ZONE add CONSTRAINT COMMUNE_DE_ZONE_ETR_COM_0 FOREIGN KEY (COMMUNE) REFERENCES COMMUNE (ID_COMMUNE);
ALTER TABLE COMMUNE_DE_ZONE add CONSTRAINT COMMUNE_DE_ZONE_ETR_ZONE_0 FOREIGN KEY (ZONE) REFERENCES ZONE (ID_ZONE);

ALTER TABLE COMMUNE_DEPENDANCE add CONSTRAINT COMMUNE_DEPENDANCE_ETR_COM_0 FOREIGN KEY (COMMUNE) REFERENCES COMMUNE (ID_COMMUNE); 
ALTER TABLE COMMUNE_DEPENDANCE add CONSTRAINT COMMUNE_DEPENDANCE_ETR_DCOM_0 FOREIGN KEY (DEPENDANCE) REFERENCES DEPENDANCE_COMMUNE (ID_DEPENDANCE);

 
ALTER TABLE COMMUNE_RESIDUELLE add CONSTRAINT COMMUNE_RESIDUELLE_ETR_COM_0 FOREIGN KEY (COMMUNE) REFERENCES COMMUNE (ID_COMMUNE);
ALTER TABLE COMMUNE_RESIDUELLE add CONSTRAINT COMMUNE_RESIDUELLE_ETR_GET_0 FOREIGN KEY (ZONAGE, SIGNATURE) REFERENCES GROUPE_ETALON (ZONAGE, SIGNATURE);

