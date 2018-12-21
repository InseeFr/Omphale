//parametres : 

//define idep=
//define annee_ref=2006
//define annee_hor=2031
//define prefix=zp


//Traitements


//initialisation par maintient


//initialisation des quotients de deces
 
drop table &&prefix._&&idep._quotient_decp;
create table &&prefix._&&idep._quotient_decp as select
a.age, a.sexe, b.annee, a.zone, a.qd
from &&prefix._&&idep._quotient_dec a, annees b, &&prefix._&&idep._nb_pas c
where a.zone !='_TOTAL' and b.annee >= c.annee_ref and b.annee <= c.annee_hor
;


create index  &&prefix._&&idep._quotient_decp_cle   on  &&prefix._&&idep._quotient_decp (age, sexe, annee, zone);

//initialisation des quotients de fécondite
drop table &&prefix._&&idep._quotient_naip;
create table &&prefix._&&idep._quotient_naip as select
a.age, b.annee, a.zone, a.qf
from &&prefix._&&idep._quotient_nai a, annees b, &&prefix._&&idep._nb_pas c
where a.zone !='_TOTAL' and b.annee >= c.annee_ref and b.annee <= c.annee_hor
;

create index  &&prefix._&&idep._quotient_naip_cle   on  &&prefix._&&idep._quotient_naip (age, annee, zone);

//initialisation des quotients emigration
drop table &&prefix._&&idep._quotient_emip;
create table &&prefix._&&idep._quotient_emip as select
a.age, sexe, b.annee, a.origine, a.destination, a.qe
from &&prefix._&&idep._quotient_emi a, annees b
where a.origine !='_TOTAL' and a.destination !='_TOTAL' and b.annee in (select annee_pas from &&prefix._&&idep._pas)
;

//initialisation des quotients actifs
 
drop table &&prefix._&&idep._quotient_actp;
create table &&prefix._&&idep._quotient_actp as select
a.age, a.sexe, b.annee, a.zone, a.qa
from &&prefix._&&idep._quotient_act a, annees b, &&prefix._&&idep._nb_pas c
where a.zone !='_TOTAL' and b.annee >= c.annee_ref and b.annee <= c.annee_hor
;

//initialisation des quotients actifs
 
drop table &&prefix._&&idep._quotient_menp;
create table &&prefix._&&idep._quotient_menp as select
a.age, a.sexe, b.annee, a.zone, a.qm
from &&prefix._&&idep._quotient_men a, annees b, &&prefix._&&idep._nb_pas c
where a.zone !='_TOTAL' and b.annee >= c.annee_ref and b.annee <= c.annee_hor
;




//initialisation des echanges avec etranger
drop table &&prefix._&&idep._immig_etrp;
create table &&prefix._&&idep._immig_etrp as 
select distinct
a.age, a.sexe, b.annee, a.zone, a.population
from &&prefix._&&idep._agrege_pop a, annees b
where 1=2;

drop table &&prefix._&&idep._emig_etrp;
create table &&prefix._&&idep._emig_etrp as 
select distinct
a.age, a.sexe, b.annee, a.zone, a.population
from &&prefix._&&idep._agrege_pop a, annees b
where 1=2;


//table des anomalies

drop table &&prefix._&&idep._csv_anomalies;
create table &&prefix._&&idep._csv_anomalies (annee_pas number(4), nb_cas number, message varchar2(1000))
;