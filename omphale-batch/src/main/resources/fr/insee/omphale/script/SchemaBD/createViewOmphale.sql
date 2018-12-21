 CREATE OR REPLACE FORCE VIEW COMMUNE_DE_ZSD (ZONE, COMMUNE) AS 
  (SELECT ID_ZONE as ZONE, ID_COMMUNE as COMMUNE  
FROM ZONE,DEPARTEMENT_IMPACT,COMMUNE
where ZONE.type_zone_standard=3 and ZONE.id_zone=DEPARTEMENT_IMPACT.zone and DEPARTEMENT_IMPACT.DEPT= COMMUNE.id_dept);
 


  CREATE OR REPLACE FORCE VIEW COMMUNE_DE_ZSF (ZONE, COMMUNE) AS 
  SELECT ID_ZONE as ZONE, ID_COMMUNE as COMMUNE  
FROM COMMUNE, ZONE where zone.type_zone_standard=1;
 


  CREATE OR REPLACE FORCE VIEW COMMUNE_DE_ZSR (ZONE, COMMUNE) AS 
  SELECT ID_ZONE as ZONE, ID_COMMUNE as COMMUNE  
FROM ZONE,DEPARTEMENT_IMPACT,COMMUNE
where ZONE.type_zone_standard=2 and ZONE.id_zone=DEPARTEMENT_IMPACT.zone and DEPARTEMENT_IMPACT.DEPT=COMMUNE.id_dept;

CREATE OR REPLACE FORCE VIEW COMMUNE_DE_ZSGR (ZONE, COMMUNE) AS 
SELECT ID_ZONE as ZONE, ID_COMMUNE as COMMUNE  
FROM ZONE,DEPARTEMENT_IMPACT,COMMUNE
where ZONE.type_zone_standard=6 and ZONE.id_zone=DEPARTEMENT_IMPACT.zone and DEPARTEMENT_IMPACT.DEPT=COMMUNE.id_dept;
 


  CREATE OR REPLACE FORCE VIEW CYCLE_OUVERT (ANNEE) AS 
  select distinct annee from cb_pop_legale_init;
 

  CREATE OR REPLACE FORCE VIEW VUE_ACTIFS (AGE, SEXE, COMMUNE, ANNEE, POPULATION) AS 
  select age, sexe, commune, annee,  population
from cb_pop_legale_init where type_pop=2;
 

  CREATE OR REPLACE FORCE VIEW VUE_GENERATION (AGE, SEXE, COMMUNE, ANNEE, POPULATION) AS 
  select age, sexe, commune, annee,  population
from cb_pop_legale_init where type_pop=4;
 

  CREATE OR REPLACE FORCE VIEW VUE_IMM_ETR (AGE, SEXE, COMMUNE, ANNEE, POPULATION) AS 
  select age, sexe, commune, annee,  population
from cb_pop_legale_init where type_pop=5;
 

  CREATE OR REPLACE FORCE VIEW VUE_MENAGES (AGE, SEXE, COMMUNE, ANNEE, POPULATION) AS 
  select age, sexe, commune, annee,  population
from cb_pop_legale_init where type_pop=3;
 


  CREATE OR REPLACE FORCE VIEW VUE_POP_REF (AGE, SEXE, COMMUNE, ANNEE, POPULATION) AS 
  select age, sexe, commune, annee,  population
from cb_pop_legale_init where type_pop=1;
 
 