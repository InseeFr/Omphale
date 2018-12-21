//parametres : 

//define zonage_id=2086
//define idep=
//define prefix=zp
//define annee_ref=2006

//define def_projection_etalon=645
//define zonage_etalon=0



drop table &&prefix._&&idep._user_pop_age;
create table &&prefix._&&idep._user_pop_age as select
a.groupet, a.sexe, a.annee, a.zone, sum(a.population) population
from &&prefix._&&idep._user_pop a
group by a.groupet, a.sexe, a.annee, a.zone
;

create index &&prefix._&&idep._user_pop_age_k on &&prefix._&&idep._user_pop_age(groupet, sexe, annee, zone);

drop table &&prefix._&&idep._ratio_vect;
create table &&prefix._&&idep._ratio_vect as
select a.groupet, a.sexe, a.annee, a.zone, b.population/a.population ratio
from &&prefix._&&idep._user_pop_age a, &&prefix._&&idep._total_zone b
where a.groupet=b.groupet and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
;

create index &&prefix._&&idep._ratio_vect_k on &&prefix._&&idep._ratio_vect(groupet, sexe, annee, zone);

drop table &&prefix._&&idep._user_popx;
create table &&prefix._&&idep._user_popx as
select a.groupet, a.age, a.sexe, a.annee, a.zone, a.population*b.ratio population
from &&prefix._&&idep._user_pop a, &&prefix._&&idep._ratio_vect b
where a.groupet=b.groupet and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
;

create index &&prefix._&&idep._user_popx_k on &&prefix._&&idep._user_popx(groupet, age, sexe, annee, zone);

drop table &&prefix._&&idep._user_pop_zon;
create table &&prefix._&&idep._user_pop_zon as select
a.groupet, a.age, a.sexe, a.annee, sum(a.population) population
from &&prefix._&&idep._user_popx a
group by a.groupet, a.age, a.sexe, a.annee
;

create index &&prefix._&&idep._user_pop_zon_k on &&prefix._&&idep._user_pop_zon(groupet, age, sexe, annee);


drop table &&prefix._&&idep._ratio_age;
create table &&prefix._&&idep._ratio_age as
select a.groupet, a.age, a.sexe, a.annee, b.population/a.population ratio
from &&prefix._&&idep._user_pop_zon a, &&prefix._&&idep._groupet_pop b
where a.groupet=b.groupet and a.age=b.age and a.sexe=b.sexe and a.annee=b.annee
;

create index &&prefix._&&idep._ratio_age_k on &&prefix._&&idep._ratio_age(groupet, age, sexe, annee);

truncate table &&prefix._&&idep._user_pop;
insert into &&prefix._&&idep._user_pop 
select a.groupet, a.age, a.sexe, a.annee, a.zone, a.population*b.ratio population
from &&prefix._&&idep._user_popx a, &&prefix._&&idep._ratio_age b
where a.groupet=b.groupet and a.age=b.age and a.sexe=b.sexe and a.annee=b.annee
;

commit;
