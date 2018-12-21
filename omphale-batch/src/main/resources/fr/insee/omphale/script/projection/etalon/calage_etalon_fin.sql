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



drop index &&prefix._&&idep._proj_pyr_cle;
create index &&prefix._&&idep._proj_pyr_cle on &&prefix._&&idep._proj_pyr(age, sexe, annee, zone);

update &&prefix._&&idep._proj_pyr a
set a.population=(select b.population from &&prefix._&&idep._pop_cal b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone)
where (a.age, a.sexe, a.annee, a.zone) in
(select c.age, c.sexe, c.annee, c.zone from &&prefix._&&idep._pop_cal c)
;


drop table &&prefix._&&idep._csv_population_cal;
create table &&prefix._&&idep._csv_population_cal
as select * from &&prefix._&&idep._proj_pyr
where age >=0
;

create index &&prefix._&&idep._csv_population_cal_k on &&prefix._&&idep._csv_population_cal(age, sexe, annee, zone);
