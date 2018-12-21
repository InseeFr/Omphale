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





//projection quotients actifs

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

drop table &&prefix._&&idep._quotient_acti;
create table &&prefix._&&idep._quotient_acti as 
select
a.age, a.sexe, a.zone, a.qa, b.valeur
from &&prefix._&&idep._quotient_actp a, cb_hypothese b, &&prefix._&&idep._cube c
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and b.annee=c.annee_deb and b.id_hypothese=&&idhyp.
and a.zone != '_TOTAL' and a.sexe >=&&sexe_deb. and a.sexe <=&&sexe_fin. and a.age >=&&age_deb. and a.age <=&&age_fin.
and a.zone like c.zone_locale
;

drop table &&prefix._&&idep._quotient_actj;
create table &&prefix._&&idep._quotient_actj as
select a.age, a.sexe, c.annee, a.zone,

  decode
  (
  nvl((a.qa*(1-a.valeur)*c.valeur+(1-a.qa)*a.valeur*(1-c.valeur)),0),0,0,
  nvl((a.qa*(1-a.valeur)*c.valeur),0)/(a.qa*(1-a.valeur)*c.valeur+(1-a.qa)*a.valeur*(1-c.valeur))
  
  ) qa 


from  &&prefix._&&idep._quotient_acti a, cb_hypothese c
where  a.sexe=c.sexe and a.age=c.age and c.id_hypothese=&&idhyp. 
and c.annee in (
select annee from annees, &&prefix._&&idep._cube  where annee >= annee_deb and annee <= annee_fin
)
;

create unique index  &&prefix._&&idep._quotient_actj_cle   on  &&prefix._&&idep._quotient_actj (age, sexe, annee, zone);

update &&prefix._&&idep._quotient_actp a set 
a.qa=(select b.qa from &&prefix._&&idep._quotient_actj b where a.age=b.age and a.sexe=b.sexe and a.zone=b.zone and a.annee=b.annee)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_actj)
;

commit;