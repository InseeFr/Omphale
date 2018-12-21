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
//surcharge par cube hypothese

//projection quotients emigration

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

drop table &&prefix._&&idep._quotient_emij;
create table &&prefix._&&idep._quotient_emij as 
select
b.age, b.sexe, b.annee, a.zone origine, d.zone destination,  b.valeur qe
from
(select distinct origine zone from &&prefix._&&idep._quotient_emip union select distinct destination zone from &&prefix._&&idep._quotient_emip) a,
(select distinct origine zone from &&prefix._&&idep._quotient_emip union select distinct destination zone from &&prefix._&&idep._quotient_emip) d,
cb_hypothese b, &&prefix._&&idep._cube c
where b.annee>=c.annee_deb and b.annee<=c.annee_fin and b.id_hypothese=&&idhyp.
and a.zone !='_TOTAL' and d.zone !='_TOTAL' and b.sexe >=&&sexe_deb. and b.sexe <=&&sexe_fin. and b.age >=&&age_deb. and b.age <=&&age_fin.
and a.zone like c.zone_locale and d.zone like c.zone_destination
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
a.sexe >=&&sexe_deb. 
and
a.sexe <=&&sexe_fin. 
and 
a.age >=&&age_deb. 
and a.age <=&&age_fin.
;

commit;

insert into &&prefix._&&idep._quotient_emip
select * from &&prefix._&&idep._quotient_emij
;

commit;
