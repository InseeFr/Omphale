CREATE TABLE COMPOSANTE (	
		COMPOSANTE VARCHAR2(5 BYTE), 
		LIBELLE VARCHAR2(100 char)
);

CREATE TABLE COUPLE_COM_LIEE (	
		COMMUNE VARCHAR2(5 BYTE), 
		COMMUNE_LIEE VARCHAR2(5 BYTE)
);

CREATE TABLE USER_OMPHALE (	
		ID_USER VARCHAR2(6 BYTE), 
		ROLE NUMBER(1,0), 
		EMAIL VARCHAR2(50 BYTE), 
		LIBELLE VARCHAR2(100 char), 
		IDEP VARCHAR2(6 BYTE)
) ;

CREATE TABLE ZONAGE (	
		ID_ZONAGE VARCHAR2(5 BYTE), 
		ID_USER VARCHAR2(6 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		ANNEE_VALIDATION NUMBER(4,0), 
		ETAT_VALIDATION NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char),
		DATE_CREATION DATE
) ;

CREATE TABLE SCENAR_NON_LOC (	
		ID_SCENARIO NUMBER(15,0), 
		ID_USER VARCHAR2(6 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		STANDARD NUMBER(1,0), 
		VALIDATION NUMBER(1,0), 
		DATE_CREATION DATE, 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE DEF_PROJECTION (	
		ID_PROJECTION NUMBER(15,0), 
		ID_SCENARIO NUMBER(15,0), 
		ID_ZONAGE VARCHAR2(5 BYTE), 
		ANNEE_REFERENCE NUMBER(4,0), 
		ANNEE_HORIZON NUMBER(4,0), 
		ID_USER VARCHAR2(6 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		STANDARD NUMBER(1,0), 
		PYRAMIDE_GENERATION NUMBER(1,0), 
		VALIDATION NUMBER(1,0), 
		CALAGE NUMBER(1,0), 
		ENGLOBANTE NUMBER(1,0), 
		ACTIFS NUMBER(1,0), 
		MENAGES NUMBER(1,0), 
		ELEVES NUMBER(1,0), 
		ID_PROJ_ETALON NUMBER(15,0), 
		ID_PROJ_ENGLOBANTE NUMBER(15,0), 
		DATE_CREATION DATE, 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE REGION (	
		ID_REGION VARCHAR2(2 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		LIBELLE VARCHAR2(100 char), 
		FM_DOM_TOM VARCHAR2(1 BYTE)
) ;

CREATE TABLE DEPARTEMENT (	
		ID_DEPT VARCHAR2(3 BYTE), 
		ID_REGION VARCHAR2(2 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		LIBELLE VARCHAR2(100 char)
);

CREATE TABLE TYPE_ZONE_STANDARD (	
		TYPE_ZONE_STANDARD NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE ZONE (	
		ID_ZONE VARCHAR2(5 BYTE), 
		ID_USER VARCHAR2(6 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		STANDARD NUMBER(1,0), 
		TYPE_ZONE_STANDARD NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE DEPARTEMENT_IMPACT (	
		ZONE VARCHAR2(5 BYTE), 
		DEPT VARCHAR2(3 BYTE) 
) ;

CREATE TABLE DEPENDANCE_COMMUNE (	
		ID_DEPENDANCE NUMBER(4,0), 
		ANNEE NUMBER(4,0), 
		COMMENTAIRE VARCHAR2(100 char)
) ;

CREATE TABLE GROUPE_ETALON (	
		ZONAGE VARCHAR2(5 BYTE), 
		SIGNATURE VARCHAR2(20 BYTE) 
) ;

CREATE TABLE DEPT_DE_GROUPET (	
		ZONAGE VARCHAR2(5 BYTE), 
		SIGNATURE VARCHAR2(20 BYTE), 
		DEPT VARCHAR2(3 BYTE)
) ;

CREATE TABLE TYPE_PARAM (	
		TYPE_PARAM VARCHAR2(10 BYTE), 
		VAL_DEF NUMBER, 
		IS_ENTIER NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE TYPE_ENTITE(	
		TYPE_ENTITE VARCHAR2(5 BYTE), 
		DIMENSION_AGE NUMBER(1,0), 
		DIMENSION_SEXE NUMBER(1,0), 
		RESTRICTION_AGE NUMBER(1,0), 
		VAL_MAX NUMBER, 
		VAL_MIN NUMBER, 
		CTRL_EXHAUST NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE METHODE_EVOL (	
		METHODE_EVOL VARCHAR2(15 BYTE), 
		COMPOSANTE VARCHAR2(5 BYTE), 
		TYPE_ENTITE VARCHAR2(5 BYTE), 
		HYPOTHESE_REQUISE NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE USER_HYPOTHESE (	
		ID_HYPOTHESE NUMBER(15,0), 
		ID_USER VARCHAR2(6 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		TYPE_ENTITE VARCHAR2(5 BYTE), 
		AGE_DEB NUMBER(3,0), 
		SEXE_DEB NUMBER(1,0), 
		ANNEE_DEB NUMBER(4,0), 
		AGE_FIN NUMBER(3,0), 
		SEXE_FIN NUMBER(1,0), 
		ANNEE_FIN NUMBER(4,0), 
		STANDARD NUMBER(1,0), 
		DATE_CREATION DATE, 
		VALIDATION NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char) 
) ;

CREATE TABLE EVOL_NON_LOC (	
		ID_EVOL_NON_LOC NUMBER(15,0), 
		ID_USER VARCHAR2(6 BYTE), 
		NOM VARCHAR2(20 BYTE), 
		TYPE_ENTITE VARCHAR2(5 BYTE), 
		COMPOSANTE VARCHAR2(5 BYTE), 
		ID_HYPOTHESE NUMBER(15,0), 
		METHODE_EVOL VARCHAR2(15 BYTE), 
		AGE_DEB NUMBER(3,0), 
		SEXE_DEB NUMBER(1,0), 
		ANNEE_DEB NUMBER(4,0), 
		AGE_FIN NUMBER(3,0), 
		SEXE_FIN NUMBER(1,0), 
		ANNEE_FIN NUMBER(4,0), 
		TYPE_PARAM1 VARCHAR2(10 BYTE), 
		TYPE_PARAM2 VARCHAR2(10 BYTE), 
		TYPE_PARAM3 VARCHAR2(10 BYTE), 
		TYPE_PARAM4 VARCHAR2(10 BYTE), 
		VAL_PARAM1 NUMBER, 
		VAL_PARAM2 NUMBER, 
		VAL_PARAM3 NUMBER, 
		VAL_PARAM4 NUMBER, 
		STANDARD NUMBER(1,0), 
		DATE_CREATION DATE, 
		COMMENTAIRE VARCHAR2(200 char) 
) ;

CREATE TABLE EVOL_LOCALISE (	
		ID_EVOL_LOCALISE NUMBER(15,0), 
		ID_PROJECTION NUMBER(15,0), 
		ID_EVOL_NON_LOC NUMBER(15,0), 
		ZONAGE VARCHAR2(5 BYTE), 
		COMPOSANTE VARCHAR2(5 BYTE), 
		ZONE VARCHAR2(5 BYTE), 
		ZONE_DESTINATION VARCHAR2(5 BYTE), 
		RANG NUMBER
) ;

CREATE TABLE EVOL_DE_SCENAR (	
		ID_SCENARIO NUMBER(15,0), 
		ID_EVOL_NON_LOC NUMBER(15,0), 
		RANG NUMBER
) ;

CREATE TABLE PARAM_METH_EVOL (	
		METHODE_EVOL VARCHAR2(15 BYTE), 
		TYPE_PARAM VARCHAR2(10 BYTE), 
		RANG NUMBER(1,0), 
		VAL_DEF NUMBER, 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE PROJECTION_LANCEE (	
		ID_PROJECTION_LANCEE NUMBER(15,0), 
		ID_PROJECTION NUMBER(15,0), 
		DATE_LANCEMENT DATE, 
		DATE_EXEC DATE, 
		DONNEES NUMBER(1,0), 
		DATE_DEBUT_EXEC DATE, 
		CODE_RETOUR VARCHAR2(5 BYTE), 
		MESSAGE VARCHAR2(200 BYTE), 
		NBR_ZONE NUMBER(4,0)
) ;
CREATE TABLE TYPE_POP (	
		TYPE_POP NUMBER(1,0), 
		LIBELLE VARCHAR2(100 char)
) ;

CREATE TABLE USER_POPULATION (	
		PROJECTION NUMBER(15,0), 
		SURVIVANT NUMBER(1,0), 
		ZONAGE VARCHAR2(5 BYTE), 
		DATE_CREATION DATE
) ;

CREATE TABLE ZONE_DE_GROUPET (	
		ZONAGE VARCHAR2(5 BYTE), 
		SIGNATURE VARCHAR2(20 BYTE), 
		ZONE VARCHAR2(5 BYTE)
) ;

CREATE TABLE ZONE_DE_ZONAGE (	
		ZONAGE VARCHAR2(5 BYTE), 
		ZONE VARCHAR2(5 BYTE)
) ;

CREATE TABLE CB_DECES (	
	AGE NUMBER(3,0), 
	SEXE NUMBER(1,0), 
	COMMUNE VARCHAR2(5 BYTE), 
	ANNEE NUMBER(4,0), 
	DECES NUMBER
);
  
CREATE TABLE CB_FLUX (
	AGE NUMBER(3,0), 
	SEXE NUMBER(1,0), 
	ORIGINE VARCHAR2(5 BYTE), 
	DESTINATION VARCHAR2(5 BYTE), 
	ANNEE NUMBER(4,0), 
	FLUX NUMBER
);
 
CREATE TABLE CB_HYPOTHESE (
	ID_HYPOTHESE NUMBER(5,0), 
	AGE NUMBER(3,0), 
	SEXE NUMBER(1,0), 
	ANNEE NUMBER(4,0), 
	VALEUR NUMBER
);

CREATE TABLE CB_NAISSANCES (
	AGE NUMBER(3,0), 
	SEXE NUMBER(1,0), 
	COMMUNE VARCHAR2(5 BYTE), 
	ANNEE NUMBER(4,0), 
	NAISSANCES NUMBER
);

CREATE TABLE CB_POP_COLLECTE (
	AGE NUMBER(3,0), 
	SEXE NUMBER(1,0), 
	COMMUNE VARCHAR2(5 BYTE), 
	ANNEE NUMBER(4,0), 
	POPULATION NUMBER
);

CREATE TABLE CB_POP_LEGALE (
	AGE NUMBER(3,0), 
	SEXE NUMBER(1,0), 
	COMMUNE VARCHAR2(5 BYTE), 
	ANNEE NUMBER(4,0), 
	TYPE_POP NUMBER(1,0), 
	POPULATION NUMBER
);

CREATE TABLE CB_POPULATION (
	PROJECTION NUMBER(15,0), 
	SURVIVANT NUMBER(1,0), 
	AGE NUMBER(3,0), 
	SEXE NUMBER(1,0), 
	ANNEE NUMBER(4,0), 
	ZONE VARCHAR2(5 BYTE), 
	VALEUR NUMBER
);


CREATE TABLE ANNEE (
    ANNEE NUMBER(4)
);


CREATE TABLE COMMUNE (
	ID_COMMUNE VARCHAR2(5 BYTE), 
	ID_DEPT VARCHAR2(3 BYTE), 
	ID_REGION VARCHAR2(2 BYTE), 
	LIBELLE VARCHAR2(100 char)
); 

CREATE TABLE COMMUNE_DE_ZONE (	
	ZONE VARCHAR2(5 BYTE), 
	COMMUNE VARCHAR2(5 BYTE)
);


CREATE TABLE COMMUNE_DEPENDANCE (
	DEPENDANCE NUMBER(4,0), 
	COMMUNE VARCHAR2(5 BYTE)
);

CREATE TABLE COMMUNE_RESIDUELLE (	
	ZONAGE VARCHAR2(5 BYTE), 
	SIGNATURE VARCHAR2(20 BYTE), 
	COMMUNE VARCHAR2(5 BYTE)
);

 