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
//parallele a cube hypothese





//projection quotients deces

drop table &&prefix._&&idep._cube;
create  table &&prefix._&&idep._cube as
select decode(sign(min(annee)-&&annee_deb.),1, min(annee), &&annee_deb.)  annee_deb,
 decode(sign(max(annee)-&&annee_fin.),-1, max(annee), &&annee_fin.)  annee_fin,
'&&zone_locale.' zone_locale,
'&&zone_destination.' zone_destination 
 
 from cb_hypothese where id_hypothese=&&idhyp.
;

update &&prefix._&&idep._cube 
set annee_deb = decode(sign(annee_deb-&&annee_ref.),1, annee_deb, &&annee_ref.),
annee_fin = (select decode(sign(annee_fin-annee_hor),-1, annee_fin, annee_hor) from &&prefix._&&idep._nb_pas)
;

drop table &&prefix._&&idep._quotient_deci;
create table &&prefix._&&idep._quotient_deci as 
select
a.age, a.sexe, a.zone, a.qd, b.valeur
from &&prefix._&&idep._quotient_decp a, cb_hypothese b, &&prefix._&&idep._cube c
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and b.annee=c.annee_deb and b.id_hypothese=&&idhyp.
and a.zone != '_TOTAL' and a.sexe >=&&sexe_deb. and a.sexe <=&&sexe_fin. and a.age >=&&age_deb. and a.age <=&&age_fin.
and a.zone like c.zone_locale
;

drop table &&prefix._&&idep._quotient_decj;
create table &&prefix._&&idep._quotient_decj as
select a.age, a.sexe, a.annee, a.zone,
c.valeur * decode(nvl(b.valeur, 0), 0, 0, b.qd/b.valeur) qd 
from &&prefix._&&idep._quotient_decp a, &&prefix._&&idep._quotient_deci b, cb_hypothese c
where a.age=b.age and a.sexe=b.sexe and a.zone=b.zone and b.sexe=c.sexe and b.age=c.age and a.annee=c.annee and c.id_hypothese=&&idhyp. 
and a.annee in (
select annee from annees, &&prefix._&&idep._cube  where annee >= annee_deb and annee <= annee_fin
)
;

create unique index  &&prefix._&&idep._quotient_decj_cle   on  &&prefix._&&idep._quotient_decj (age, sexe, annee, zone);

update &&prefix._&&idep._quotient_decp a set 
a.qd=(select b.qd from &&prefix._&&idep._quotient_decj b where a.age=b.age and a.sexe=b.sexe and a.zone=b.zone and a.annee=b.annee)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_decj)
;

commit;