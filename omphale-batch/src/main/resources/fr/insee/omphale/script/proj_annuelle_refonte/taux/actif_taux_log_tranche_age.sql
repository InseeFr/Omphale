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

//Calcul des taux d’activité projetés en âge quinquennal    
    
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
   
//On modifie dans les tables la valeur max de la tranche d age des 70 ans et plus pour avoir l age de fin (99 ans)
//Attention si la variable age fin est inferieur à 70 ans il y aura un bug dans ces lignes (mais jusqu a present il est fixé à 99)
update AGEQ a set a.MAX = &&age_fin. where a.MAX=200; 
update &&prefix._&&idep._p_ta a set a.MAX=&&age_fin. where a.MAX=200;


drop table &&prefix._&&idep._quotient_acti_ta;
create table &&prefix._&&idep._quotient_acti_ta as select DISTINCT
a.QA, a.sexe, a.zone, b.valeur,a.AGEQ, a.min, a.MAX
from &&prefix._&&idep._p_ta a, cb_hypothese b, &&prefix._&&idep._cube c
where a.sexe=b.sexe and a.annee=b.annee and b.annee=c.annee_deb and b.id_hypothese=&&idhyp. and b.AGE between a.MIN AND a.MAX
and a.zone != '_TOTAL' and a.sexe >=&&sexe_deb. and a.sexe <=&&sexe_fin. and a.MIN >=&&age_deb. and a.MAX <=&&age_fin.
and a.zone like c.zone_locale; 


drop table &&prefix._&&idep._q_actj_ta;
create table &&prefix._&&idep._q_actj_ta as
select DISTINCT a.sexe, c.annee, a.zone, a.AGEQ, a.MIN, a.MAX,

  decode
  (
  nvl((a.QA*(1-a.valeur)*c.valeur+(1-a.QA)*a.valeur*(1-c.valeur)),0),0,0,
  nvl((a.QA*(1-a.valeur)*c.valeur),0)/(a.QA*(1-a.valeur)*c.valeur+(1-a.QA)*a.valeur*(1-c.valeur))
  
  ) QA 


from  &&prefix._&&idep._quotient_acti_ta a, cb_hypothese c
where  a.sexe=c.sexe and c.age BETWEEN a.MIN AND a.MAX and c.id_hypothese=&&idhyp. 
and c.annee in (
select annee from annees, &&prefix._&&idep._cube  where annee >= annee_deb and annee <= annee_fin
)
;

create unique index &&prefix._&&idep._q_actj_ta_cle on &&prefix._&&idep._q_actj_ta (AGEQ, sexe, annee, zone);

update &&prefix._&&idep._p_ta a set 
a.QA =(select b.QA from &&prefix._&&idep._q_actj_ta b where a.AGEQ =b.AGEQ and a.sexe=b.sexe and a.zone=b.zone and a.annee=b.annee)
where (a.AGEQ, a.sexe, a.annee, a.zone) in (select AGEQ, sexe, annee, zone from &&prefix._&&idep._q_actj_ta);

commit;
