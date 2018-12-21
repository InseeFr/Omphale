// projection_user_fin.sql
//
// _csv_population_act <c- _proj_pyr, _quotient_actp
// _csv_population_men <c- _proj_pyr, _quotient_menp
// cb_population <d-
// user_population <d-
// _proj_pyr <d-
// _user_pyr <c- _proj_pyr, zone_de_zonage, zone
// user_population <i-
// cb_population <i- _user_pyr


//&gt. ;

//define zonage_id=0
//define idep=
//define prefix=zp
//define def_projection_id=0


drop index &&prefix._&&idep._proj_pyr_cle;
create index &&prefix._&&idep._proj_pyr_cle on &&prefix._&&idep._proj_pyr(age, sexe, annee, zone);

drop index &&prefix._&&idep._POPOMPHQ_cle;
create index &&prefix._&&idep._POPOMPHQ_cle on &&prefix._&&idep._POPOMPHQ(AGEQ, sexe, annee, zone);


--/* les populations détaillées par zone, sexe et âge,  */
--/* pour l’ensemble des années de projection et des zones d’étude (non calées)  */
--/* (CSV_POPULATION_NCAL, CSV_POPULATION_MEN, CSV_POPULATION_ACT) ; */

DROP TABLE &&prefix._&&idep._csv_population_act;
CREATE TABLE &&prefix._&&idep._csv_population_act AS
SELECT a.AGEQ, a.SEXE, a.ANNEE, a.ZONE, a.pop*b.QA actifs FROM &&prefix._&&idep._POPOMPHQ a, &&prefix._&&idep._p_ta b 
WHERE a.SEXE=b.SEXE AND a.ANNEE=b.ANNEE AND a.ZONE=b.ZONE AND a.AGEQ=b.AGEQ;


--/*  les populations détaillées par zone, sexe et âge,  */
--/* pour l’ensemble des années de projection et des zones d’étude (non calées)  */
--/*  (CSV_POPULATION_NCAL, CSV_POPULATION_MEN, CSV_POPULATION_ACT) ; *  */

drop table &&prefix._&&idep._csv_population_men;
create table &&prefix._&&idep._csv_population_men
as select a.age, a.sexe, a.annee, a.zone, a.population*b.qm menages from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._quotient_menp b
where a.age >=0 and a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone;

create index &&prefix._&&idep._csv_population_men_k on &&prefix._&&idep._csv_population_men(age, sexe, annee, zone);


delete from cb_population where projection=&&def_projection_id.;
delete from user_population where projection=&&def_projection_id.;

delete from &&prefix._&&idep._proj_pyr where age <0;


drop table &&prefix._&&idep._user_pyr;

create table &&prefix._&&idep._user_pyr as
select distinct &&def_projection_id. projection, 0 survivant, 
a.age, a.sexe, a.annee, b.zone, a.population
from &&prefix._&&idep._proj_pyr a, zone_de_zonage b, zone c
where &&potentiel_englobant.=1 and b.zonage=&&zonage_id. and b.zone = c.id_zone and c.nom=a.zone;

insert into user_population
select &&def_projection_id. projection, 0 survivant, &&zonage_id. zonage, sysdate date_creation
from dual where &&potentiel_englobant.=1
;

insert into cb_population
select * from &&prefix._&&idep._user_pyr;

commit;