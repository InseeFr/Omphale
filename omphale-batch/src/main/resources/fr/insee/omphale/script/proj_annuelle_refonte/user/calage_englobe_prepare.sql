//parametres : 

//define zonage_id=2086
//define idep=
//define prefix=zp
//define annee_ref=2006

//define def_projection_etalon=645
//define zonage_etalon=0

//define def_projection_englobante=790


drop table &&prefix._&&idep._dep_impact;
create table &&prefix._&&idep._dep_impact as
select distinct a.englobe groupet, a.englobe zone 
from &&prefix._&&idep._zonage_det_eng a
;

drop table &&prefix._&&idep._zone_impact;
create table &&prefix._&&idep._zone_impact as
select distinct a.englobe groupet, a.detail zone 
from &&prefix._&&idep._zonage_det_eng a
;

drop index &&prefix._&&idep._proj_pyr_cle;
create index &&prefix._&&idep._proj_pyr_cle on &&prefix._&&idep._proj_pyr(age, sexe, annee, zone);

drop table &&prefix._&&idep._groupet_pop;
create table &&prefix._&&idep._groupet_pop
as select b.groupet, a.age, a.sexe, a.annee, sum(a.valeur) population from cb_population a, &&prefix._&&idep._dep_impact b
where a.projection=&&def_projection_englobante. and a.survivant=0 and a.zone=b.zone and a.age >=0
group by b.groupet, a.age, a.sexe, a.annee
;  

create index &&prefix._&&idep._groupet_pop_k on &&prefix._&&idep._groupet_pop(groupet, age, sexe, annee);

drop table &&prefix._&&idep._user_pop;
create table &&prefix._&&idep._user_pop
as select b.groupet, a.age, a.sexe, a.annee, a.zone, a.population from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._zone_impact b
where a.zone=b.zone and a.age >=0
;

create index &&prefix._&&idep._user_pop_cle on &&prefix._&&idep._user_pop(groupet, age, sexe, annee, zone);

drop table &&prefix._&&idep._groupet_pop_age;
create table &&prefix._&&idep._groupet_pop_age as select
a.groupet, a.sexe, a.annee, sum(a.population) population
from &&prefix._&&idep._groupet_pop a
group by a.groupet, a.sexe, a.annee
;
create index &&prefix._&&idep._groupet_pop_age_k on &&prefix._&&idep._groupet_pop_age(groupet, sexe, annee);

drop table &&prefix._&&idep._user_pop_age;
create table &&prefix._&&idep._user_pop_age as select
a.groupet, a.sexe, a.annee, a.zone, sum(a.population) population
from &&prefix._&&idep._user_pop a
group by a.groupet, a.sexe, a.annee, a.zone
;
create index &&prefix._&&idep._user_pop_age_k on &&prefix._&&idep._user_pop_age(groupet, sexe, annee, zone);

drop table &&prefix._&&idep._user_pop_age_zon;
create table &&prefix._&&idep._user_pop_age_zon as select
a.groupet, a.sexe, a.annee, sum(a.population) population
from &&prefix._&&idep._user_pop_age a
group by a.groupet, a.sexe, a.annee
;
create index &&prefix._&&idep._user_pop_age_zon_k on &&prefix._&&idep._user_pop_age_zon(groupet, sexe, annee);

drop table &&prefix._&&idep._total_zone;
create table &&prefix._&&idep._total_zone as select
a.groupet, a.sexe, a.annee, a.zone, a.population*b.population/c.population population
from &&prefix._&&idep._user_pop_age a,  &&prefix._&&idep._groupet_pop_age b, &&prefix._&&idep._user_pop_age_zon c
where a.groupet=b.groupet and a.sexe=b.sexe and a.annee=b.annee
and a.groupet=c.groupet and a.sexe=c.sexe and a.annee=c.annee
;

create index &&prefix._&&idep._total_zone_k on &&prefix._&&idep._total_zone(groupet, sexe, annee, zone);






