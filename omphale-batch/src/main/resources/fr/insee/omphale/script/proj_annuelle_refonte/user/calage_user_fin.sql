//parametres : 

//define zonage_id=2086
//define idep=
//define prefix=zp
//define annee_ref=2006

//define def_projection_etalon=645
//define zonage_etalon=0

drop index &&prefix._&&idep._user_pop_cle;
create index &&prefix._&&idep._user_pop_cle on &&prefix._&&idep._user_pop(age, sexe, annee, zone);

drop index &&prefix._&&idep._proj_pyr_cle;
create index &&prefix._&&idep._proj_pyr_cle on &&prefix._&&idep._proj_pyr(age, sexe, annee, zone);

delete from &&prefix._&&idep._proj_pyr a where (a.age, a.sexe, a.annee, a.zone)
in (select b.age, b.sexe, b.annee, b.zone from &&prefix._&&idep._user_pop b)
;

commit;

insert into &&prefix._&&idep._proj_pyr select age, sexe, annee, zone, population from &&prefix._&&idep._user_pop
;

commit;



--/* Résultats */
--/* si la projection est calée : les populations calées par zone, sexe et âge, */
-- /* 		pour l ensemble des années charnières de projection et des zones d étude (CSV_POPULATIONCAL); */
drop table &&prefix._&&idep._csv_population_cal;
create table &&prefix._&&idep._csv_population_cal
as select * from &&prefix._&&idep._proj_pyr
where age >=0
;

create index &&prefix._&&idep._csv_population_cal_k on &&prefix._&&idep._csv_population_cal(age, sexe, annee, zone);


