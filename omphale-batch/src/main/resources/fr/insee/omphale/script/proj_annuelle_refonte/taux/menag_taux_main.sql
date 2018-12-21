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





//projection quotients menages

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

drop table &&prefix._&&idep._quotient_meni;
create table &&prefix._&&idep._quotient_meni as 
select
a.age, a.sexe, a.zone, a.qm
from &&prefix._&&idep._quotient_menp a, &&prefix._&&idep._cube c
where a.zone != '_TOTAL' and a.annee=c.annee_deb and a.sexe >=&&sexe_deb. and a.sexe <=&&sexe_fin. and a.age >=&&age_deb. and a.age <=&&age_fin.
and a.zone like c.zone_locale
;

drop table &&prefix._&&idep._quotient_menj;
create table &&prefix._&&idep._quotient_menj as select
a.age, a.sexe, b.annee, a.zone, a.qm
from &&prefix._&&idep._quotient_meni a, annees b, &&prefix._&&idep._cube c
where b.annee >= c.annee_deb and b.annee <= c.annee_fin
;

create unique index  &&prefix._&&idep._quotient_menj_cle   on  &&prefix._&&idep._quotient_menj (age, sexe, annee, zone);


update &&prefix._&&idep._quotient_menp a set 
a.qm=(select b.qm from &&prefix._&&idep._quotient_menj b where a.age=b.age and a.sexe=b.sexe and a.zone=b.zone and a.annee=b.annee)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_menj)
;

commit;