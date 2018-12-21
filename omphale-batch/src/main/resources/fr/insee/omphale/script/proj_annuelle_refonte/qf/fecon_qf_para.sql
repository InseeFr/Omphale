//parametres : 



//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2050
//define idhyp=2
//define methode=para
//define tq=qf
//define annee_deb=2006
//define annee_fin=2015
//define age_deb=14
//define age_fin=25
//define mere_inf=14
//define mere_sup=49

//define zone_locale=AIX
//define zone_destination=%

//Traitements
//parallele a cube hypothese


//projection quotients fecondite

drop table &&prefix._&&idep._cube;
create  table &&prefix._&&idep._cube as
select decode(sign(min(annee)-&&annee_deb.),1, min(annee), &&annee_deb.)  annee_deb,
 decode(sign(max(annee)-&&annee_fin.),-1, max(annee), &&annee_fin.)  annee_fin,
 &&age_deb. age_deb, &&age_fin. age_fin,
'&&zone_locale.' zone_locale,
'&&zone_destination.' zone_destination

 from cb_hypothese where id_hypothese=&&idhyp.
;

update &&prefix._&&idep._cube 
set annee_deb = decode(sign(annee_deb-&&annee_ref.),1, annee_deb, &&annee_ref.),
annee_fin = (select decode(sign(annee_fin-annee_hor),-1, annee_fin, annee_hor) from &&prefix._&&idep._nb_pas),
age_deb = decode(sign(age_deb-&&mere_inf.),1, age_deb, &&mere_inf.),
age_fin = decode(sign(age_fin-&&mere_sup.),-1, age_fin, &&mere_sup.)
;

drop table &&prefix._&&idep._quotient_naii;
create table &&prefix._&&idep._quotient_naii as 
select
a.age, a.zone, a.qf, b.valeur
from &&prefix._&&idep._quotient_naip a, cb_hypothese b, &&prefix._&&idep._cube c
where a.age=b.age and a.annee=b.annee and b.annee=c.annee_deb and b.id_hypothese=&&idhyp.
and a.zone != '_TOTAL' and a.age >=c.age_deb and a.age <=c.age_fin
and a.zone like c.zone_locale
;

drop table &&prefix._&&idep._quotient_naij;
create table &&prefix._&&idep._quotient_naij as
select a.age, a.annee, a.zone,
c.valeur * decode(nvl(b.valeur, 0), 0, 0, b.qf/b.valeur) qf 
from &&prefix._&&idep._quotient_naip a, &&prefix._&&idep._quotient_naii b, cb_hypothese c
where a.age=b.age and a.zone=b.zone and b.age=c.age and a.annee=c.annee and c.id_hypothese=&&idhyp. 
and a.annee in (
select annee from annees, &&prefix._&&idep._cube  where annee >= annee_deb and annee <= annee_fin
)
;

create unique index  &&prefix._&&idep._quotient_naij_cle   on  &&prefix._&&idep._quotient_naij (age, annee, zone);

update &&prefix._&&idep._quotient_naip a set 
a.qf=(select b.qf from &&prefix._&&idep._quotient_naij b where a.age=b.age and a.zone=b.zone and a.annee=b.annee)
where (a.age, a.annee, a.zone) in (select age, annee, zone from &&prefix._&&idep._quotient_naij)
;

commit;