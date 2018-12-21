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

//define def_projection_etalon=0
//define zonage_etalon=0



drop table &&prefix._&&idep._dep_non_impact;
create table &&prefix._&&idep._dep_non_impact as
select distinct a.zone uzone, b.zone from &&prefix._&&idep._zonage a,
zone_de_zonage b, departement_impact c
where 
a.zone like '_DEP_%'
and b.zonage='&&zonage_etalon.' and b.zone = c.zone and c.dept=substr(a.zone, 6);

drop table &&prefix._&&idep._dep_impact;
create table &&prefix._&&idep._dep_impact as
select a.signature groupet, b.zone from dept_de_groupet a,
zone_de_zonage b, departement_impact c
where 
a.zonage='&&zonage_id.' and b.zonage='&&zonage_etalon.' and b.zone = c.zone and c.dept=a.dept;

drop table &&prefix._&&idep._etalon_surv;
create table &&prefix._&&idep._etalon_surv
as select a.age, a.sexe, a.annee, b.uzone zone, a.valeur survivant from cb_population a, &&prefix._&&idep._dep_non_impact b
where a.projection=&&def_projection_etalon. and a.survivant=1 and a.zone=b.zone
;

drop table &&prefix._&&idep._etalon_pop;
create table &&prefix._&&idep._etalon_pop
as select a.age, a.sexe, a.annee, b.uzone zone, a.valeur survivant from cb_population a, &&prefix._&&idep._dep_non_impact b
where a.projection=&&def_projection_etalon. and a.survivant=0 and a.zone=b.zone
;

drop table &&prefix._&&idep._groupet_pop;

create table &&prefix._&&idep._groupet_pop
as select a.age, a.sexe, a.annee, b.groupet, sum(a.valeur) population from cb_population a, &&prefix._&&idep._dep_impact b
where a.projection=&&def_projection_etalon. and a.survivant=0 and a.zone=b.zone
group by a.age, a.sexe, a.annee, b.groupet
;  