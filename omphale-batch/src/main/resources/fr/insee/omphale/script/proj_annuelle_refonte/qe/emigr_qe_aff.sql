//parametres : 



//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2031
//define idhyp=1
//define methode=para
//define tq=qd
//define annee_deb=2006
//define annee_fin=2031
//define sexe_deb=1
//define sexe_fin=2
//define age_deb=-1
//define age_fin=99

//define zone_locale=AIX
//define zone_destination=%

//Traitements
//maintient

//projection quotients emigration

drop table &&prefix._&&idep._cube;
create  table &&prefix._&&idep._cube as
select &&annee_deb. annee_deb,
&&annee_fin. annee_fin,
'&&zone_locale.' zone_locale,
'&&zone_destination.' zone_destination
from dual;


update &&prefix._&&idep._cube 
set annee_deb = decode(sign(annee_deb-&&annee_ref.),1, annee_deb, &&annee_ref.),
annee_fin = (select decode(sign(annee_fin-annee_hor),-1, annee_fin, annee_hor) from &&prefix._&&idep._nb_pas)
;

drop table &&prefix._&&idep._quotient_emii;
create table &&prefix._&&idep._quotient_emii as 
select
a.age, a.sexe, a.origine, a.destination, (a.qe*&&COEF_HOM. + &&COEF_TRAN.) qe
from &&prefix._&&idep._quotient_emip a, &&prefix._&&idep._cube c
where a.origine !='_TOTAL' and a.destination !='_TOTAL'  and a.annee=c.annee_deb and a.sexe >=&&sexe_deb. and a.sexe <=&&sexe_fin. and a.age >=&&age_deb. and a.age <=&&age_fin.
and a.origine like c.zone_locale and a.destination like c.zone_destination
;

drop table &&prefix._&&idep._quotient_emik;
create table &&prefix._&&idep._quotient_emik as 
select
a.age, e.sexe, b.zone origine, d.zone destination, &&COEF_TRAN. qe
from 
(select distinct age from &&prefix._&&idep._quotient_emip) a,
(select distinct sexe from &&prefix._&&idep._quotient_emip) e,
(select distinct origine zone from &&prefix._&&idep._quotient_emip union select distinct destination zone from &&prefix._&&idep._quotient_emip) b,
(select distinct origine zone from &&prefix._&&idep._quotient_emip union select distinct destination zone from &&prefix._&&idep._quotient_emip) d,
&&prefix._&&idep._cube c
where b.zone !='_TOTAL' and d.zone !='_TOTAL'  and e.sexe >=&&sexe_deb.
and e.sexe <=&&sexe_fin. and a.age >=&&age_deb. and a.age <=&&age_fin.
and b.zone like c.zone_locale and d.zone like c.zone_destination
and &&COEF_TRAN. != 0
;
delete from &&prefix._&&idep._quotient_emik where (age, sexe, origine, destination) in (select age, sexe, origine, destination from &&prefix._&&idep._quotient_emii)
;
commit;
insert into &&prefix._&&idep._quotient_emik select * from &&prefix._&&idep._quotient_emii
;
commit;

drop table &&prefix._&&idep._quotient_emij;
create table &&prefix._&&idep._quotient_emij as select
a.age, a.sexe, b.annee, a.origine, a.destination, a.qe
from &&prefix._&&idep._quotient_emik a, annees b, &&prefix._&&idep._cube c
where b.annee >= c.annee_deb and b.annee <= c.annee_fin
and b.annee in (select distinct annee from &&prefix._&&idep._quotient_emip where annee not in (select annee_hor from &&prefix._&&idep._nb_pas))
;

delete from &&prefix._&&idep._quotient_emip a
where 
a.origine like '&&zone_locale.'
and
a.destination like '&&zone_destination.'
and
a.annee >= (select annee_deb from &&prefix._&&idep._cube)
and
a.annee <= (select annee_fin from &&prefix._&&idep._cube)
and 
a.annee in (select distinct annee from &&prefix._&&idep._quotient_emip where annee not in (select annee_hor from &&prefix._&&idep._nb_pas))
and
a.sexe >=&&sexe_deb. 
and
a.sexe <=&&sexe_fin. 
and 
a.age >=&&age_deb. 
and
a.age <=&&age_fin.
;

commit;

insert into &&prefix._&&idep._quotient_emip
select * from &&prefix._&&idep._quotient_emij
;
commit;




