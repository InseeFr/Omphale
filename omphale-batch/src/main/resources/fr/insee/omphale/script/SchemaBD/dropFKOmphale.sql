
ALTER TABLE ZONAGE drop CONSTRAINT ZONAGE_ETR_USER_0;
  
ALTER TABLE DEF_PROJECTION drop CONSTRAINT DEF_PROJECTION_ETR_ZONA_0 ; 
ALTER TABLE DEF_PROJECTION drop CONSTRAINT DEF_PROJECTION_ETR_USER_0 ; 
ALTER TABLE DEF_PROJECTION drop CONSTRAINT DEF_PROJECTION_ETR_SCNL_0 ; 
ALTER TABLE DEF_PROJECTION drop CONSTRAINT DEF_PROJECTION_ETR_ETALON ; 
ALTER TABLE DEF_PROJECTION drop CONSTRAINT DEF_PROJECTION_ETR_ENGLOB ;
  
ALTER TABLE DEPARTEMENT drop CONSTRAINT DEPARTEMENT_ETR_REG_0 ;
   
ALTER TABLE ZONE drop CONSTRAINT ZONE_ETR_USER_0 ; 
ALTER TABLE ZONE drop CONSTRAINT ZONE_ETR_TZS_0 ;
   
ALTER TABLE DEPARTEMENT_IMPACT drop CONSTRAINT DEPARTEMENT_IMPACT_ETR_DEPT_0 ; 
ALTER TABLE DEPARTEMENT_IMPACT drop CONSTRAINT DEPARTEMENT_IMPACT_ETR_ZONE_0 ;
  
ALTER TABLE GROUPE_ETALON drop CONSTRAINT GROUPE_ETALON_ETR_ZONA_0 ;
  
ALTER TABLE DEPT_DE_GROUPET drop CONSTRAINT DEPT_DE_GROUPET_ETR_DEPT_0 ; 
ALTER TABLE DEPT_DE_GROUPET drop CONSTRAINT DEPT_DE_GROUPET_ETR_GET_0 ;

ALTER TABLE METHODE_EVOL drop CONSTRAINT METHODE_EVOL_ETR_TQT_0 ; 
ALTER TABLE METHODE_EVOL drop CONSTRAINT METHODE_EVOL_ETR_TENT_0 ;
   
ALTER TABLE USER_HYPOTHESE drop CONSTRAINT USER_HYPOTHESE_ETR_USER_0 ; 
ALTER TABLE USER_HYPOTHESE drop CONSTRAINT USER_HYPOTHESE_ETR_TENT_0 ;
   
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_USER_0 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_UHY_0 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_MEV_0 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_TPAR_1 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_TPAR_2 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_TPAR_3 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_TPAR_4 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_TQT_0 ; 
ALTER TABLE EVOL_NON_LOC drop CONSTRAINT EVOL_NON_LOC_ETR_TENT_0 ;
   
ALTER TABLE EVOL_LOCALISE drop CONSTRAINT EVOL_LOCALISE_ETR_ZONA_0 ; 
ALTER TABLE EVOL_LOCALISE drop CONSTRAINT EVOL_LOCALISE_ETR_ZONE_1 ; 
ALTER TABLE EVOL_LOCALISE drop CONSTRAINT EVOL_LOCALISE_ETR_ZONE_2 ; 
ALTER TABLE EVOL_LOCALISE drop CONSTRAINT EVOL_LOCALISE_ETR_TQT_0 ; 
ALTER TABLE EVOL_LOCALISE drop CONSTRAINT EVOL_LOCALISE_ETR_EVNL_0 ; 
ALTER TABLE EVOL_LOCALISE drop CONSTRAINT EVOL_LOCALISE_ETR_DPROJ_0 ;
  
ALTER TABLE EVOL_DE_SCENAR drop CONSTRAINT EVOL_DE_SCENAR_ETR_EVNL_0 ; 
ALTER TABLE EVOL_DE_SCENAR drop CONSTRAINT EVOL_DE_SCENAR_ETR_SCNL_0 ;
   
ALTER TABLE PARAM_METH_EVOL drop CONSTRAINT PARAM_METH_EVOL_ETR_MEV_0 ; 
ALTER TABLE PARAM_METH_EVOL drop CONSTRAINT PARAM_METH_EVOL_ETR_TPAR_0 ;
 
ALTER TABLE USER_POPULATION drop CONSTRAINT USER_POPULATION_ETR_ZONA_0 ; 
ALTER TABLE USER_POPULATION drop CONSTRAINT USER_POPULATION_ETR_DPROJ_0 ;

ALTER TABLE ZONE_DE_GROUPET drop CONSTRAINT ZONE_DE_GROUPET_ETR_GET_0 ; 
ALTER TABLE ZONE_DE_GROUPET drop CONSTRAINT ZONE_DE_GROUPET_ETR_TZON_0 ;
 
ALTER TABLE ZONE_DE_ZONAGE drop CONSTRAINT ZONE_DE_ZONAGE_ETR_ZONA_0 ; 
ALTER TABLE ZONE_DE_ZONAGE drop CONSTRAINT ZONE_DE_ZONAGE_ETR_ZONE_0 ;
 
ALTER TABLE CB_POPULATION drop CONSTRAINT CB_POPULATION_ETR_ZONE_0 ; 
ALTER TABLE CB_POPULATION drop CONSTRAINT CB_POPULATION_ETR_UPOP_0 ;

ALTER TABLE CB_DECES drop CONSTRAINT CB_DECES_ETR_COM_0;
 
ALTER TABLE CB_FLUX drop CONSTRAINT CB_FLUX_ETR_COM_1;
ALTER TABLE CB_FLUX drop CONSTRAINT CB_FLUX_ETR_COM_2; 
 
ALTER TABLE CB_HYPOTHESE drop CONSTRAINT CB_HYPOTHESE_ETR_UHY_0;
  
ALTER TABLE CB_NAISSANCES drop CONSTRAINT CB_NAISSANCES_ETR_COM_0;
  
ALTER TABLE CB_POP_COLLECTE drop CONSTRAINT CB_POP_COLLECTE_ETR_COM_0;
 
ALTER TABLE CB_POP_LEGALE drop CONSTRAINT CB_POP_LEGALE_ETR_COM_0;
ALTER TABLE CB_POP_LEGALE drop CONSTRAINT CB_POP_LEGALE_ETR_TPOP_0;   

ALTER TABLE COMMUNE drop CONSTRAINT COMMUNE_ETR_DEPT_0;
ALTER TABLE COMMUNE drop CONSTRAINT COMMUNE_ETR_REG_0;



ALTER TABLE COMMUNE_DE_ZONE drop CONSTRAINT COMMUNE_DE_ZONE_ETR_COM_0;
ALTER TABLE COMMUNE_DE_ZONE drop CONSTRAINT COMMUNE_DE_ZONE_ETR_ZONE_0;

ALTER TABLE COMMUNE_DEPENDANCE drop CONSTRAINT COMMUNE_DEPENDANCE_ETR_COM_0; 
ALTER TABLE COMMUNE_DEPENDANCE drop CONSTRAINT COMMUNE_DEPENDANCE_ETR_DCOM_0;

 
ALTER TABLE COMMUNE_RESIDUELLE drop CONSTRAINT COMMUNE_RESIDUELLE_ETR_COM_0;
ALTER TABLE COMMUNE_RESIDUELLE drop CONSTRAINT COMMUNE_RESIDUELLE_ETR_GET_0;