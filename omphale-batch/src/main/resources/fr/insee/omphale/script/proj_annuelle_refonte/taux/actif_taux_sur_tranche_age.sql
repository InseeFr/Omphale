    
//Calcul des taux d’activité projetés en âge quinquennal    
    
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

drop table &&prefix._&&idep._q_actj_ta;
create table &&prefix._&&idep._q_actj_ta as
select DISTINCT b.sexe, b.annee, a.zone, a.AGEQ, a.MIN, a.MAX, b.valeur QA 
from  &&prefix._&&idep._p_ta a, cb_hypothese b, &&prefix._&&idep._cube c
where b.annee>=c.annee_deb and b.annee<=c.annee_fin and b.id_hypothese=&&idhyp.
and a.zone != '_TOTAL' and b.sexe >=&&sexe_deb. and b.sexe <=&&sexe_fin. and b.age >=&&age_deb. and b.age <=&&age_fin.
and a.zone like c.zone_locale
;

delete from &&prefix._&&idep._p_ta a
where 
a.zone like '&&zone_locale.'
and
a.annee >= (select annee_deb from &&prefix._&&idep._cube)
and
a.annee <= (select annee_fin from &&prefix._&&idep._cube)
and
a.sexe >=&&sexe_deb. 
and
a.sexe <=&&sexe_fin. 
and 
a.MIN >=&&age_deb. 
and a.MAX <=&&age_fin.
;

commit;

ALTER TABLE &&prefix._&&idep._q_actj_ta MODIFY "ANNEE" NUMBER;

insert into &&prefix._&&idep._p_ta
select a.SEXE, a.ANNEE, a.ZONE, a.QA, a.AGEQ, a.MIN, a.MAX from &&prefix._&&idep._q_actj_ta a
;

commit;