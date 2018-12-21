//parametres : 



//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2030


//Traitements
//calcul des quotients survie quinquenaux pour chaque pas de projection

drop table &&prefix._&&idep._quotient_surv;
create table &&prefix._&&idep._quotient_surv
as select age, sexe, annee, zone, 1-qd qs from &&prefix._&&idep._quotient_decp
where annee in (select annee_pas from &&prefix._&&idep._pas)
;
create index &&prefix._&&idep._quotient_surv_cle on &&prefix._&&idep._quotient_surv(age, sexe, annee, zone);

//ajout pour les vieux
insert into &&prefix._&&idep._quotient_decp a
select 100 age, b.sexe, b.annee, b.zone, b.qd from &&prefix._&&idep._quotient_decp b
where b.age=99;
insert into &&prefix._&&idep._quotient_decp a
select 101 age, b.sexe, b.annee, b.zone, b.qd from &&prefix._&&idep._quotient_decp b
where b.age=99;
insert into &&prefix._&&idep._quotient_decp a
select 102 age, b.sexe, b.annee, b.zone, b.qd from &&prefix._&&idep._quotient_decp b
where b.age=99;
insert into &&prefix._&&idep._quotient_decp a
select 103 age, b.sexe, b.annee, b.zone, b.qd from &&prefix._&&idep._quotient_decp b
where b.age=99;
insert into &&prefix._&&idep._quotient_decp a
select 104 age, b.sexe, b.annee, b.zone, b.qd from &&prefix._&&idep._quotient_decp b
where b.age=99;



drop table &&prefix._&&idep._quotient_surv2;
create table &&prefix._&&idep._quotient_surv2
as select age-1 age, sexe, annee-1 annee, zone, 1-qd qs from &&prefix._&&idep._quotient_decp
where annee in (select annee_pas+1 from &&prefix._&&idep._pas)
;


create index &&prefix._&&idep._quotient_surv2_cle on &&prefix._&&idep._quotient_surv2(age, sexe, annee, zone);

drop table &&prefix._&&idep._quotient_surv3;
create table &&prefix._&&idep._quotient_surv3
as select age-2 age, sexe, annee-2 annee, zone, 1-qd qs from &&prefix._&&idep._quotient_decp
where annee in (select annee_pas+2 from &&prefix._&&idep._pas)
;


create index &&prefix._&&idep._quotient_surv3_cle on &&prefix._&&idep._quotient_surv3(age, sexe, annee, zone);

drop table &&prefix._&&idep._quotient_surv4;
create table &&prefix._&&idep._quotient_surv4
as select age-3 age, sexe, annee-3 annee, zone, 1-qd qs from &&prefix._&&idep._quotient_decp
where annee in (select annee_pas+3 from &&prefix._&&idep._pas)
;



create index &&prefix._&&idep._quotient_surv4_cle on &&prefix._&&idep._quotient_surv4(age, sexe, annee, zone);


drop table &&prefix._&&idep._quotient_surv5;
create table &&prefix._&&idep._quotient_surv5
as select age-4 age, sexe, annee-4 annee, zone, 1-qd qs from &&prefix._&&idep._quotient_decp
where annee in (select annee_pas+4 from &&prefix._&&idep._pas)
;


create index &&prefix._&&idep._quotient_surv5_cle on &&prefix._&&idep._quotient_surv5(age, sexe, annee, zone);


update &&prefix._&&idep._quotient_surv a set a.qs=(select a.qs*b.qs from &&prefix._&&idep._quotient_surv2 b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv2)
;
update &&prefix._&&idep._quotient_surv a set a.qs=(select a.qs*b.qs from &&prefix._&&idep._quotient_surv3 b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv3)
;

update &&prefix._&&idep._quotient_surv a set a.qs=(select a.qs*b.qs from &&prefix._&&idep._quotient_surv4 b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv4)
;

update &&prefix._&&idep._quotient_surv a set a.qs=(select a.qs*b.qs from &&prefix._&&idep._quotient_surv5 b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv5)
;

drop table &&prefix._&&idep._quotient_surv2;
drop table &&prefix._&&idep._quotient_surv3;
drop table &&prefix._&&idep._quotient_surv4;
drop table &&prefix._&&idep._quotient_surv5;







