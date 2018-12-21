//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2031
//define idhyp=7
//define methode=CALET
//define tq=FR
//define annee_deb=2006
//define annee_fin=2041
//define sexe_deb=1
//define sexe_fin=2
//define age_deb=0
//define age_fin=99


drop table &&prefix._&&idep._pop_cal_age;
create table &&prefix._&&idep._pop_cal_age as select
a.sexe, a.annee, a.zone, sum(a.population) population
from &&prefix._&&idep._pop_cal a
group by a.sexe, a.annee, a.zone
;

create index &&prefix._&&idep._pop_cal_age_cle on &&prefix._&&idep._pop_cal_age(sexe, annee, zone);

drop table &&prefix._&&idep._ratio_vect;
create table &&prefix._&&idep._ratio_vect as
select a.sexe, a.annee, a.zone, b.population/a.population ratio
from &&prefix._&&idep._pop_cal_age a, &&prefix._&&idep._total_zone b
where a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
;

create index &&prefix._&&idep._ratio_vect_cle on &&prefix._&&idep._ratio_vect(sexe, annee, zone);

drop table &&prefix._&&idep._pop_calx;
create table &&prefix._&&idep._pop_calx as
select a.age, a.sexe, a.annee, a.zone, a.population*b.ratio population
from &&prefix._&&idep._pop_cal a, &&prefix._&&idep._ratio_vect b
where a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
;

create index &&prefix._&&idep._pop_calx_cle on &&prefix._&&idep._pop_calx(age, sexe, annee, zone);

drop table &&prefix._&&idep._pop_cal_zon;
create table &&prefix._&&idep._pop_cal_zon as select
a.age, a.sexe, a.annee, sum(a.population) population
from &&prefix._&&idep._pop_calx a
group by a.age, a.sexe, a.annee
;

create index &&prefix._&&idep._pop_cal_zon_cle on &&prefix._&&idep._pop_cal_zon(age, sexe, annee);


drop table &&prefix._&&idep._ratio_age;
create table &&prefix._&&idep._ratio_age as
select a.age, a.sexe, a.annee, b.population/a.population ratio
from &&prefix._&&idep._pop_cal_zon a, &&prefix._&&idep._pop_FRANCE b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee
;

create index &&prefix._&&idep._ratio_age_cle on &&prefix._&&idep._ratio_age(age, sexe, annee);

truncate table &&prefix._&&idep._pop_cal;
insert into &&prefix._&&idep._pop_cal 
select a.age, a.sexe, a.annee, a.zone, a.population*b.ratio population
from &&prefix._&&idep._pop_calx a, &&prefix._&&idep._ratio_age b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee
;

commit;
