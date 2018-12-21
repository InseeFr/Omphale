
/**********************************************************************************************/
/* Commune script end *************************************************************************/
/**********************************************************************************************/

delete from zz_temp_commune where id_commune in (select distinct a.id_commune from commune a);
insert into commune select * from zz_temp_commune;
drop table zz_temp_commune;
commit;

/**********************************************************************************************/
/* deces script end *************************************************************************/
/**********************************************************************************************/

create index zz_temp_deces_an on zz_temp_deces (annee);
delete from cb_deces where annee in (select distinct a.annee from zz_temp_deces a);
insert into cb_deces select * from zz_temp_deces;
drop table zz_temp_deces;
drop table zz_temp_deces_ctl;


/**********************************************************************************************/
/* flux script end *************************************************************************/
/**********************************************************************************************/

create index zz_temp_flux_an on zz_temp_flux (annee);
drop index cb_flux_an;
create index cb_flux_an on cb_flux (annee);
delete from cb_flux where annee in (select distinct a.annee from zz_temp_flux a);
insert into cb_flux select * from zz_temp_flux;
drop table zz_temp_flux;
drop table zz_temp_flux_ctl;
drop index cb_flux_an;
commit;


/**********************************************************************************************/
/* naissances script end *************************************************************************/
/**********************************************************************************************/

create index zz_temp_naissances_an on zz_temp_naissances (annee);
delete from cb_naissances where annee in (select distinct a.annee from zz_temp_naissances a);
insert into cb_naissances select * from zz_temp_naissances;
drop table zz_temp_naissances;
drop table zz_temp_naissances_ctl;




/**********************************************************************************************/
/* pop_legale script end *************************************************************************/
/**********************************************************************************************/


create index zz_temp_pop_legale_an on zz_temp_pop_legale (annee, type_pop);
drop index cb_pop_legale_an;
create index cb_pop_legale_an on cb_pop_legale (annee, type_pop);
delete from cb_pop_legale where (annee, type_pop) in (select distinct a.annee, a.type_pop  from zz_temp_pop_legale a);
insert into cb_pop_legale select * from zz_temp_pop_legale;
drop table zz_temp_pop_legale;
drop table zz_temp_pop_legale_ctl;
drop index cb_pop_legale_an;

/**********************************************************************************************/
/* cycles script end *************************************************************************/
/**********************************************************************************************/

//parametres : 
//idep utilisateur
//borne_inf  et borne_sup des qf
//annee_reference ()
//age de regroupement(99 et plus)
//age de decalage flux (
//ancol : nb annees collecte pour un cycle : 5
//ancolq pour les tests, on divise par 4 au lieu de 5 car il manque une annee de donnees pour deces et naissances

//define idep=omphale

//define age_last=99
//define mere_inf=14
//define mere_sup=49
//define ancol=5
//define ancolq=5

//controles

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_pop_legale b where b.type_pop=1)
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_pop_legale b where b.type_pop=5)
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_flux b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-2) not in (select b.annee from cb_deces b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-1) not in (select b.annee from cb_deces b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_deces b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+1) not in (select b.annee from cb_deces b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+2) not in (select b.annee from cb_deces b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-2) not in (select b.annee from cb_naissances b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-1) not in (select b.annee from cb_naissances b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_naissances b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+1) not in (select b.annee from cb_naissances b )
);

insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+2) not in (select b.annee from cb_naissances b )
);

//suppression des cycles existants

truncate table cb_pop_legale_init drop storage;
truncate table cb_flux_init drop storage;
truncate table cb_avg_dec drop storage;
truncate table cb_avg_nai drop storage;
truncate table cb_avg_nai_pop drop storage;

///*************************************************///


//creation des nouveaux cycles


//population legale  pour pyramide initiale

insert into cb_pop_legale_init 
select age, sexe, commune, annee, type_pop, population from cb_pop_legale 
where annee in (select annee from zz_temp_cycles) and age < 99
;
drop table zz_temp_pop_legale_vieux;
create table zz_temp_pop_legale_vieux as
select annee, 99 age, sexe, type_pop, commune, sum(population) population
from cb_pop_legale 
where annee in (select annee from zz_temp_cycles) and age >=99
group by annee, sexe, type_pop, commune
;
insert into cb_pop_legale_init select age, sexe, commune, annee, type_pop, population from zz_temp_pop_legale_vieux;
drop table zz_temp_pop_legale_vieux;



//flux 

insert into cb_flux_init 
select age, sexe, destination, origine, annee, flux  from cb_flux 
where annee in (select annee from zz_temp_cycles) and age < 99
;
drop table zz_temp_flux_init_vieux;
create table zz_temp_flux_init_vieux as
select annee, 99 age, sexe, destination, origine, sum(flux) flux
from cb_flux 
where annee in (select annee from zz_temp_cycles) and age >= 99
group by annee, sexe, destination, origine
;
insert into cb_flux_init
select age, sexe, destination, origine, annee, flux from zz_temp_flux_init_vieux
;
drop table zz_temp_flux_init_vieux;




//deces cycle 

insert into cb_avg_dec select a.age, a.sexe, a.commune, b.annee, (sum(a.deces))/5 deces
from cb_deces a, zz_temp_cycles b
where a.annee >= (b.annee-2) and a.annee <=(b.annee+2)
group by a.age, a.sexe, a.commune, b.annee
;
drop table zz_temp_avg_dec_vieux;
create table zz_temp_avg_dec_vieux as select annee, 99 age, sexe, commune, sum(deces) deces
from cb_avg_dec
where age >=99 
group by annee,  sexe, commune
;

delete from cb_avg_dec where age >=99 ;
insert into cb_avg_dec select age, sexe, commune, annee, deces from zz_temp_avg_dec_vieux;
drop table zz_temp_avg_dec_vieux;

//naissance pour calcul des quotients de fecondite
//naissances cycle 
//on additionne les sexes
 // on groupe les ages extremes de la plage de fecondite and age>=14 and age <=49

insert into cb_avg_nai select a.age, a.commune, b.annee, (sum(a.naissances))/5 naissances
from cb_naissances a, zz_temp_cycles b
where a.annee >= (b.annee-2) and a.annee <=(b.annee+2)
group by a.age,  a.commune, b.annee
;

drop table zz_temp_avg_nai_vieux;
create table zz_temp_avg_nai_vieux as select 49 age,  commune, annee, sum(naissances) naissances
from cb_avg_nai
where age >=49  
group by annee,  commune
;

delete from cb_avg_nai where age >=49 ;
insert into cb_avg_nai select age, commune, annee, naissances from zz_temp_avg_nai_vieux;
drop table zz_temp_avg_nai_vieux;

create table zz_temp_avg_nai_vieux as select 14 age,  commune, annee, sum(naissances) naissances
from cb_avg_nai
where age <=14 
group by annee,  commune
;

delete from cb_avg_nai where age <=14  ;
insert into cb_avg_nai select age, commune, annee, naissances from zz_temp_avg_nai_vieux;
drop table zz_temp_avg_nai_vieux;






//naissance pour calcul des population age -1
//population nee cycle 
//on additionne les ages de la mere

insert into cb_avg_nai_pop select a.sexe, a.commune,b.annee, (sum(naissances))/5 population
from cb_naissances a, zz_temp_cycles b
where a.annee >= (b.annee-2) and a.annee <=(b.annee+2)
group by a.sexe,  a.commune, b.annee
;
commit;
///*************************************************///

drop table zz_temp_cycles;
drop table zz_temp_cycles_ctl;




/**********************************************************************************************/
/* couple_com_liee script end *************************************************************************/
/**********************************************************************************************/


//////controle

insert into zz_temp_couple_com_liee_ctl select 1 from zz_temp_couple_com_liee a
where a.commune not in (select b.id_commune from commune b)
;

insert into zz_temp_couple_com_liee_ctl select 1 from zz_temp_couple_com_liee a
where a.commune_liee not in (select b.id_commune from commune b)
;

commit;

///////vidage
truncate table couple_com_liee drop storage;

///////insertion non circulaire
insert into couple_com_liee select com, liee from (
select LEAST(commune,commune_liee) com,
GREATEST(commune,commune_liee) liee
from zz_temp_couple_com_liee
) group by com, liee
;

///////menage
drop table zz_temp_couple_com_liee;
drop table zz_temp_couple_com_liee_ctl;

//parametres : 

//Traitements


drop table ZZ_temp_zf0;
create table ZZ_temp_zf0 as select commune, commune_liee frere, rownum id_couple from (
select distinct * from couple_com_liee)
;

//debut du script reel

//initialisation des dependances
drop table ZZ_temp_zd;
create table ZZ_temp_zd as select distinct commune, id_couple dependance from ZZ_temp_zf0;
insert into ZZ_temp_zd select distinct frere, id_couple dependance from ZZ_temp_zf0;

//initialisation famille (commune, commune frere) pour les communes qui partagent une dependance
drop table ZZ_temp_zf;
create table ZZ_temp_zf as select distinct commune, frere from ZZ_temp_zf0;
//On complete la famille par transitivite des couples (commune, commune frere) pour des communes qui partagent le même frere

--TYPE_SQL=PLSQL--
DECLARE
a NUMBER;
b NUMBER;
BEGIN
SELECT count(*) INTO a
FROM ZZ_temp_zf;
LOOP
insert into ZZ_temp_zf
select a.commune, b.frere from ZZ_temp_zf a, ZZ_temp_zf b
where a.commune != b.frere and b.frere !=a.frere and b.commune != a.commune and b.commune in
(select c.frere from ZZ_temp_zf c where c.commune=a.commune)
and (a.commune, b.frere) not in (select commune, frere from ZZ_temp_zf)
;
SELECT count(*) INTO b
FROM ZZ_temp_zf;
EXIT WHEN b=a;
a:=b;
END LOOP;
END;
/

--TYPE_SQL=SQL--
//on complete les dependances associees aux communes par celles associées aux  freres
insert into ZZ_temp_zd z
select a.commune, b.dependance from ZZ_temp_zf a, ZZ_temp_zd b
where a.frere=b.commune;
//on complete les dependances associees aux freres par ceux associes aux  communes
insert into ZZ_temp_zd z
select a.frere, b.dependance from ZZ_temp_zf a, ZZ_temp_zd b
where a.commune=b.commune;
//on elimine les doublons
drop table ZZ_temp_zd2;
create table ZZ_temp_zd2 as select distinct commune, dependance from ZZ_temp_zd;
drop table ZZ_temp_zd3;
//on calcule la dependance
create table ZZ_temp_zd3 as select commune, max(dependance) dependance from ZZ_temp_zd2
group by commune;
//on affecte la dependance
drop table ZZ_temp_zd4;
create table ZZ_temp_zd4 as select a.commune, b.dependance from
ZZ_temp_zd2 a, ZZ_temp_zd3 b where a.commune = b.commune;


//creation des dependance etalon
drop table ZZ_temp_get;
create table ZZ_temp_get as select dependance, rownum signature from (select distinct dependance from ZZ_temp_zd4
order by dependance);
//creation des communes de groupe etalon
drop table ZZ_temp_zget;
create table ZZ_temp_zget as select distinct a.signature, b.commune from 
ZZ_temp_zd4 b, ZZ_temp_get a where a.dependance=b.dependance;
//creation des dependance de groupe etalon
delete from commune_dependance;
delete from dependance_commune;
insert into dependance_commune select signature, to_number(to_char(sysdate,'yyyy')), 'calcul par script'
from ZZ_temp_get;
insert into commune_dependance select signature, commune
from ZZ_temp_zget;
commit;

//invalidation des zonages impactés

update zonage w set w.etat_validation=0 where w.id_zonage in 
(
select distinct z.zonage from zone_de_zonage z 
 where z.zone in 
(
select x.zone from
(
select zone, dependance, count(*) nbc from (
select a.zone, a.commune, b.dependance from 
commune_de_zone a, commune_dependance b
where a.commune=b.commune
) group by zone, dependance
) x,

(
select c.dependance, count(*) nbd from commune_dependance c
group by dependance
) y

where x.dependance=y.dependance and x.nbc != y.nbd

)
)
;
commit;



