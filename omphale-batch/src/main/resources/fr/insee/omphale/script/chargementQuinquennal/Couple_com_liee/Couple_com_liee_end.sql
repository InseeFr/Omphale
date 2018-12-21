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
where zz_temp_couple_com_liee.commune in (select com.id_commune from commune com)
 			and
	  zz_temp_couple_com_liee.commune_liee in (select com.id_commune from commune com)
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
//on complete les dependances associees aux communes par ceux associes aux  freres
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

drop table ZZ_TEMP_GET;
drop table ZZ_TEMP_ZD;
drop table ZZ_TEMP_ZD2;
drop table ZZ_TEMP_ZD3;
drop table ZZ_TEMP_ZD4;
drop table ZZ_TEMP_ZF;
drop table ZZ_TEMP_ZF0;
drop table ZZ_TEMP_ZGET;

