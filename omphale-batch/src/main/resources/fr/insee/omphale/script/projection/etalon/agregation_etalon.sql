// role
// 

//parametres : 


//define zonage_id=0
//define idep=
//define prefix=zp
//define annee_ref=2006
//define age_old=80
//define age_last=99
//define mere_inf=14
//define mere_sup=49
//define decal_annee_flux=5
//define type_population_quotient=1

//Traitements
//suppression des donnees de projection prealablement associees  a la projection a traiter
delete from cb_population where projection=&&def_projection_id.;
delete from user_population where projection=&&def_projection_id.;

drop table &&prefix._&&idep._les_zones;
create table &&prefix._&&idep._les_zones
as select distinct a.zone, 'ZONE' type, b.dept, 'DET_' || b.dept nom, 'F' fm_dom_tom
from zone_de_zonage a, departement_impact b
where 
a.zonage=&&zonage_id.
and b.zone=a.zone
;

update &&prefix._&&idep._les_zones set fm_dom_tom='D' 
where dept in (select id_dept from departement 
where id_region in 
(select id_region from region where fm_dom_tom != 'F')
)
;
commit;



//pas besoin dindex
//recuperation des communes des zones standard du zonage
drop table &&prefix._&&idep._zonage;
create table &&prefix._&&idep._zonage as 
select distinct 'DET_' || a.dept zone, b.id_commune commune from departement_impact a, commune b
where a.dept = b.id_dept and ( a.zone)
in (select  zone from zone_de_zonage where  zonage=&&zonage_id.)
and ( a.zone) in (select  id_zone from zone where standard=1 and type_zone_standard in (3))
;

//population legale $$annee_ref. pour le zonage
//agregation de la pop legale des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere
drop table &&prefix._&&idep._agrege_pop;
create table &&prefix._&&idep._agrege_pop as
select age, sexe, type_pop, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) population
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_pop_legale_init a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.
)
group by age, sexe, type_pop, cube(zone);

//agregation des deces des communes du zonage + une zone complementaire à la france entiere plus une zone france entiere
drop table &&prefix._&&idep._agrege_dec;
create table &&prefix._&&idep._agrege_dec as
select age, sexe, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(deces) deces
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_avg_dec a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.
)
group by age, sexe, cube(zone);

//agregation des naissances des communes du zonage + une zone complementaire à la france entiere plus une zone france entiere
drop table &&prefix._&&idep._agrege_nai;
create table &&prefix._&&idep._agrege_nai as
select age, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(naissances) naissances
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_avg_nai a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.
)
group by age, cube(zone);

//agregation des populatons nees des communes du zonage + une zone complementaire à la france entiere plus une zone france entiere
drop table &&prefix._&&idep._agrege_nai_pop;
create table &&prefix._&&idep._agrege_nai_pop as
select -1 age, sexe, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) population
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_avg_nai_pop a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.
)
group by sexe, cube(zone);



//flux $$annee_ref. pour le zonage

//agregation
drop table &&prefix._&&idep._agregation_flux;
create table &&prefix._&&idep._agregation_flux as
select age, sexe, zr, za, sum(flux) flu
from (
 select age, sexe, flux, nvl(b.zone,'_COMPLEMENT') zr, nvl(c.zone,'_COMPLEMENT') za
 from cb_flux_init a, &&prefix._&&idep._zonage b, &&prefix._&&idep._zonage c
 where a.destination=b.commune(+) and a.origine=c.commune(+) and a.annee=&&annee_ref.
)
where zr not like '_DEP_%' or za not like '_DEP_%'
group by   age, sexe, rollup (zr, za)
having grouping_id(zr)=0 and grouping_id(za)=0
;
drop table &&prefix._&&idep._agregation_flux_pop;
create table &&prefix._&&idep._agregation_flux_pop as
select age, sexe, za origine , sum(flux) pop
from (
 select age, sexe, flux, nvl(b.zone,'_COMPLEMENT') zr, nvl(c.zone,'_COMPLEMENT') za
 from cb_flux_init a, &&prefix._&&idep._zonage b, &&prefix._&&idep._zonage c
 where a.destination=b.commune(+) and a.origine=c.commune(+) and a.annee=&&annee_ref.
)
group by   age, sexe, za
;

//csv
delete from &&prefix._&&idep._agregation_flux where zr=za;

delete from &&prefix._&&idep._agrege_pop where zone='_COMPLEMENT';
delete from &&prefix._&&idep._agrege_dec where zone='_COMPLEMENT';
delete from &&prefix._&&idep._agrege_nai where zone='_COMPLEMENT';
delete from &&prefix._&&idep._agrege_nai_pop where zone='_COMPLEMENT';
delete from &&prefix._&&idep._agregation_flux where zr='_COMPLEMENT' or za='_COMPLEMENT';
delete from &&prefix._&&idep._agregation_flux_pop where origine='_COMPLEMENT';


create index &&prefix._&&idep._agrege_pop_cle on &&prefix._&&idep._agrege_pop(age,sexe,zone);


//VUES POUR CALCULS
//remarque : taux actifs, generation, menage = _agrege_pop(type_pop=2, 3, 4)/_agrege_pop(type_pop=1)

DROP VIEW &&prefix._&&idep._VUE_POP_REF;

DROP VIEW &&prefix._&&idep._VUE_POP_NEE;

DROP VIEW &&prefix._&&idep._VUE_POP_NEE_F;

DROP VIEW &&prefix._&&idep._VUE_POP_NEE_R;

 DROP VIEW &&prefix._&&idep._VUE_ACTIFS;
 DROP VIEW &&prefix._&&idep._VUE_MENAGES;
 DROP VIEW &&prefix._&&idep._VUE_GENERATION;
 
 DROP VIEW &&prefix._&&idep._VUE_IMM_ETR;

 DROP VIEW &&prefix._&&idep._VUE_NAI;

 DROP VIEW &&prefix._&&idep._VUE_DEC;

 DROP VIEW &&prefix._&&idep._VUE_NAI_F;

 DROP VIEW &&prefix._&&idep._VUE_DEC_F;

 DROP VIEW &&prefix._&&idep._VUE_POP_REF_F;

 DROP VIEW &&prefix._&&idep._VUE_POP_REF_R;

 DROP VIEW &&prefix._&&idep._VUE_ACTIFS_F;
 DROP VIEW &&prefix._&&idep._VUE_MENAGES_F;
 DROP VIEW &&prefix._&&idep._VUE_GENERATION_F;
 
 DROP VIEW &&prefix._&&idep._VUE_IMM_ETR_F;
drop view &&prefix._&&idep._vue_imm_etr_fm;
 
create view &&prefix._&&idep._vue_pop_ref as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=1 and zone not in ('_TOTAL');

create view &&prefix._&&idep._vue_pop_ref_f as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=1 and zone in ('_TOTAL');

create view &&prefix._&&idep._vue_pop_ref_r as select age, zone, sum(population) population
from &&prefix._&&idep._vue_pop_ref where age < 5 group by zone, age;



create view &&prefix._&&idep._vue_actifs as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=2 and zone not in ('_TOTAL');

create view &&prefix._&&idep._vue_actifs_f as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=2 and zone in ('_TOTAL');

create view &&prefix._&&idep._vue_menages as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=3 and zone not in ('_TOTAL');

create view &&prefix._&&idep._vue_menages_f as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=3 and zone in ('_TOTAL');

create view &&prefix._&&idep._vue_GENERATION as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=4 and zone not in ('_TOTAL');

create view &&prefix._&&idep._vue_GENERATION_f as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=4 and zone in ('_TOTAL');

create view &&prefix._&&idep._vue_imm_etr as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=5 and zone not in ('_TOTAL');

create view &&prefix._&&idep._vue_imm_etr_f as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_pop where type_pop=5 and zone in ('_TOTAL');

create view &&prefix._&&idep._vue_imm_etr_fm as select age, sexe, sum(population) population
from &&prefix._&&idep._agrege_pop where type_pop=5 
and zone in (select nom from &&prefix._&&idep._les_zones where fm_dom_tom = 'F')
group by age, sexe
;

create VIEW &&prefix._&&idep._VUE_DEC as select age, sexe, zone,  deces
from &&prefix._&&idep._agrege_dec where zone not in ('_TOTAL');

create VIEW &&prefix._&&idep._VUE_DEC_F as select age, sexe, zone,  deces
from &&prefix._&&idep._agrege_dec where zone in ('_TOTAL');

create VIEW &&prefix._&&idep._VUE_NAI as select age, zone,  naissances
from &&prefix._&&idep._agrege_nai where zone not in ('_TOTAL');

create VIEW &&prefix._&&idep._VUE_NAI_F as select age, zone,  naissances
from &&prefix._&&idep._agrege_nai where zone in ('_TOTAL');

create VIEW &&prefix._&&idep._VUE_POP_NEE as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_nai_pop where zone not in ('_TOTAL');

create VIEW &&prefix._&&idep._VUE_POP_NEE_F as select age, sexe, zone,  population
from &&prefix._&&idep._agrege_nai_pop where zone in ('_TOTAL');

create VIEW &&prefix._&&idep._VUE_POP_NEE_R as select zone, sum(population) naissances
from &&prefix._&&idep._VUE_POP_NEE group by zone;


//preparation des operations de calage
drop table &&prefix._&&idep._param_calage_etalon;
create table &&prefix._&&idep._param_calage_etalon as select &&calage. calage_demande, 0 calage_trouve, 0 calage_france from dual
;

