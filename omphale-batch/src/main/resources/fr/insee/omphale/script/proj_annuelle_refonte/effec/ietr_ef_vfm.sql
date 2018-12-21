//parametres : 



//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2031
//define idhyp=5
//define methode=ventil
//define tq=ietr
//define annee_deb=2006
//define annee_fin=2041
//define sexe_deb=1
//define sexe_fin=2
//define age_deb=1
//define age_fin=99

//define zone_locale=AIX
//define zone_destination=%

//Traitements
//ventilation du national cube hypothese


//projection des immigrants de l etranger

drop table &&prefix._&&idep._cube;
create  table &&prefix._&&idep._cube as
select decode(sign(min(annee)-&&annee_deb.),1, min(annee), &&annee_deb.)  annee_deb,
 decode(sign(max(annee)-&&annee_fin.),-1, max(annee), &&annee_fin.)  annee_fin from cb_hypothese where id_hypothese=&&idhyp.
;


update &&prefix._&&idep._cube 
set annee_deb = decode(sign(annee_deb-&&annee_ref.),1, annee_deb, &&annee_ref.),
annee_fin = (select decode(sign(annee_fin-annee_hor),-1, annee_fin, annee_hor) from &&prefix._&&idep._nb_pas)
;
commit;

drop table &&prefix._&&idep._h_immig_etr;
create table &&prefix._&&idep._h_immig_etr as 
select distinct
a.age, a.sexe, a.annee, b.zone, a.valeur*b.population/d.population population
from cb_hypothese a, &&prefix._&&idep._cube c,
&&prefix._&&idep._vue_imm_etr b,
&&prefix._&&idep._vue_imm_etr_fm d
where a.id_hypothese=&&idhyp.
and a.sexe >=&&sexe_deb. and a.sexe <=&&sexe_fin. and a.age >=&&age_deb. and a.age <=&&age_fin.
and a.annee >= c.annee_deb and a.annee <= c.annee_fin
and a.age=b.age and a.sexe=b.sexe and  a.age=d.age and a.sexe=d.sexe
and b.zone in (select nom from &&prefix._&&idep._les_zones where fm_dom_tom = 'F')
;



delete from &&prefix._&&idep._immig_etrp a
where 
a.zone like '&&zone_locale.'
and
a.zone in (select nom from &&prefix._&&idep._les_zones where fm_dom_tom = 'F')
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

insert into &&prefix._&&idep._immig_etrp
select * from &&prefix._&&idep._h_immig_etr
where 
zone like '&&zone_locale.'
;

commit;