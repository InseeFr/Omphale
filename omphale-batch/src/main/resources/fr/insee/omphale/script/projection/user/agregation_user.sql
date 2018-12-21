//parametres : 

//define zonage_id=1000
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
//suppression des donnees de projection (englobante) prealablement associees 

drop table &&prefix._&&idep._zonage_ctl;
create table &&prefix._&&idep._zonage_ctl (numero number);
create unique index &&prefix._&&idep._zonage_ctlk on &&prefix._&&idep._zonage_ctl(numero);
insert into &&prefix._&&idep._zonage_ctl values (1);
insert into &&prefix._&&idep._zonage_ctl select 1 from Zonage where ETAT_VALIDATION != 9 and id_zonage = &&zonage_id.;
drop table &&prefix._&&idep._zonage_ctl;

delete from cb_population where projection=&&def_projection_id.;
delete from user_population where projection=&&def_projection_id.;

drop table &&prefix._&&idep._les_zones;
create table &&prefix._&&idep._les_zones
as select distinct a.zone, 'ZONE' type, b.dept, c.nom, 'F' fm_dom_tom
from zone_de_zonage a, departement_impact b, zone c
where 
a.zonage=&&zonage_id.
and c.id_zone=a.zone
and b.zone=a.zone
;
insert into &&prefix._&&idep._les_zones
select null, 'DEPT', id_dept dept, '_DEP_' || id_dept, 'F' from departement
where id_dept not in
(select dept from dept_de_groupet where zonage=&&zonage_id.);
commit;

update &&prefix._&&idep._les_zones set fm_dom_tom='D' 
where dept in (select id_dept from departement 
where id_region in 
(select id_region from region where fm_dom_tom != 'F')
)
;
commit;

update &&prefix._&&idep._les_zones
set fm_dom_tom='D' where zone is not null and zone in
(select b.zone from &&prefix._&&idep._les_zones b where b.fm_dom_tom='D')
;

commit;






//communes du zonage
//recuperation des communes des zones non standards du zonage ou standards avec commune de zone 4, 5
drop table &&prefix._&&idep._zonage;
create table &&prefix._&&idep._zonage as select b.nom zone, a.commune
from commune_de_zone a, zone b where  a.zone=b.id_zone
and ( b.id_zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_id.)
and exists(
select 1 from zonage where etat_validation =9 and  id_zonage=&&zonage_id.
)
;

//ajout des communes des zones standards sans commune de zone 1,2,3
insert into &&prefix._&&idep._zonage 
select distinct a.nom, b.id_commune from zone a, departement_impact c, commune b
where c.dept = b.id_dept and  c.zone = a.id_zone and a.standard=1 and a.type_zone_standard in (1,2,3)
and ( c.zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_id.)
and exists(
select 1 from zonage where etat_validation =9 and   id_zonage=&&zonage_id.
)
;


//population legale &&annee_ref. pour le zonage
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

//agregation des deces des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere
drop table &&prefix._&&idep._agrege_dec;
create table &&prefix._&&idep._agrege_dec as
select age, sexe, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(deces) deces
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_avg_dec a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.
)
group by age, sexe, cube(zone);

//agregation des naissances des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere
drop table &&prefix._&&idep._agrege_nai;
create table &&prefix._&&idep._agrege_nai as
select age, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(naissances) naissances
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_avg_nai a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.
)
group by age, cube(zone);

//agregation des populatons nees des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere
drop table &&prefix._&&idep._agrege_nai_pop;
create table &&prefix._&&idep._agrege_nai_pop as
select -1 age, sexe, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) population
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_avg_nai_pop a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.
)
group by sexe, cube(zone);


//flux &&annee_ref. pour le zonage

//ajout des departement non impactes
insert into &&prefix._&&idep._zonage
select '_DEP_'||id_dept zone, id_commune commune from commune where id_dept not in
(select dept from dept_de_groupet where zonage=&&zonage_id.);


//immig_etr france metro &&annee_ref. pour le zonage
//agregation de la pop legale des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere
drop table &&prefix._&&idep._agrege_pep;
create table &&prefix._&&idep._agrege_pep as
select age, sexe, type_pop, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) population
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from cb_pop_legale_init a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+) and a.annee=&&annee_ref.  and a.type_pop=5
)
group by age, sexe, type_pop, cube(zone);


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
from &&prefix._&&idep._agrege_pep where zone in (select nom from &&prefix._&&idep._les_zones where fm_dom_tom = 'F')
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

//preparation des operations de calage etalon pour ne pas planter les projections utilisateurs avec scenario standard mixte
drop table &&prefix._&&idep._param_calage_etalon;
create table &&prefix._&&idep._param_calage_etalon as select &&calage. calage_demande, 0 calage_trouve, 0 calage_france from dual
;



