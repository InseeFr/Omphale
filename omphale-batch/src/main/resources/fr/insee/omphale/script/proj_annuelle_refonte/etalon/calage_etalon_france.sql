//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2031
//define age_last=99
//define idhyp=7
//define methode=CALET
//define tq=FR
//define annee_deb=2006
//define annee_fin=2041
//define sexe_deb=1
//define sexe_fin=2
//define age_deb=0
//define age_fin=99

drop table &&prefix._&&idep._mouchard;
create table &&prefix._&&idep._mouchard as select 1 mouchard from dual;
create unique index &&prefix._&&idep._mouchard_cle on &&prefix._&&idep._mouchard(mouchard);
insert into &&prefix._&&idep._mouchard select 1 from &&prefix._&&idep._param_calage_etalon
where calage_trouve = 0;

drop index &&prefix._&&idep._pop_FRANCE_cle;
create index &&prefix._&&idep._pop_FRANCE_cle on &&prefix._&&idep._pop_FRANCE(age, sexe, annee);

drop table &&prefix._&&idep._pop_cal;
create table &&prefix._&&idep._pop_cal as select * from &&prefix._&&idep._proj_pyr
;
drop index &&prefix._&&idep._pop_cal_cle;
create index &&prefix._&&idep._pop_cal_cle on &&prefix._&&idep._pop_cal(age, sexe, annee, zone);

delete from &&prefix._&&idep._pop_cal where age < 0;
delete from &&prefix._&&idep._pop_FRANCE where age < 0;
delete from &&prefix._&&idep._pop_FRANCE where age > &&age_last.;

delete from &&prefix._&&idep._pop_cal
where zone in (select zone from &&prefix._&&idep._zone_hors)
;

drop table &&prefix._&&idep._pop_FRANCE_age;
create table &&prefix._&&idep._pop_FRANCE_age as select
a.sexe, a.annee, sum(a.population) population
from &&prefix._&&idep._pop_FRANCE a
group by sexe, annee
;
drop index &&prefix._&&idep._pop_FRANCE_age_cle;
create index &&prefix._&&idep._pop_FRANCE_age_cle on &&prefix._&&idep._pop_FRANCE_age(sexe, annee);

drop table &&prefix._&&idep._pop_cal_age;
create table &&prefix._&&idep._pop_cal_age as select
a.sexe, a.annee, a.zone, sum(a.population) population
from &&prefix._&&idep._pop_cal a
group by a.sexe, a.annee, a.zone
;

create index &&prefix._&&idep._pop_cal_age_cle on &&prefix._&&idep._pop_cal_age(sexe, annee, zone);

drop table &&prefix._&&idep._pop_cal_age_zon;
create table &&prefix._&&idep._pop_cal_age_zon as select
a.sexe, a.annee, sum(a.population) population
from &&prefix._&&idep._pop_cal_age a
group by a.sexe, a.annee
;

create index &&prefix._&&idep._pop_cal_age_zon_cle on &&prefix._&&idep._pop_cal_age_zon(sexe, annee);

drop table &&prefix._&&idep._total_zone;
create table &&prefix._&&idep._total_zone as select
a.sexe, a.annee, a.zone, a.population*b.population/c.population population
from &&prefix._&&idep._pop_cal_age a, &&prefix._&&idep._pop_FRANCE_age b, &&prefix._&&idep._pop_cal_age_zon c
where a.sexe=b.sexe and a.annee=b.annee
and a.sexe=c.sexe and a.annee=c.annee
;

create index &&prefix._&&idep._total_zone_cle on &&prefix._&&idep._total_zone(sexe, annee, zone);

